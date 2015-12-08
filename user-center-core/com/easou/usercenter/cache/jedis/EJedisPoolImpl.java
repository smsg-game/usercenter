package com.easou.usercenter.cache.jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.easou.usercenter.socket.MClient;

/**
 * jedis连接池
 * 
 * @author river
 * 
 */
public class EJedisPoolImpl {
	Log log = LogFactory.getLog(EJedisPoolImpl.class);
	// 所属集群名称
	private String cluName;
	
	private HashMap<Host, JedisPool> pools = new HashMap<Host, JedisPool>();
	
	// 写操作池
	private Host writeHost;
	
	// 读操作池
	private List<Host> readHost = new ArrayList<Host>();
	
	JedisConfig jedisConfig = null;	

	private String serverStatus;
	
	private String passwd;

	/**
	 * 验证参数的合法性
	 */
	protected void validate(String[] service, Integer[] wei, Integer maxac,
			Integer maxI, Integer minI) {
		// 验证服务器配置
		if (service == null || service.length <= 0) {
			throw new RuntimeException("redis服务器列表不可为空");
		}

		// 验证权重配置
		if (wei != null) {
			if (wei.length != service.length - 1) {
				throw new RuntimeException("从机的weith配置个数与从机个数不一致,with size:"
						+ wei.length + " service size:" + service.length);
			}
		}
	}

	/**
	 * 初使化连接池
	 * 
	 * @param wconfig
	 *            数据读取缓存配置
	 * @param rconfig
	 *            数据写入缓存配置
	 */
	public void init(JedisConfig config) {
		if (log.isDebugEnabled()) {
			log.debug("开始初使化redis缓存池信息.....:" + config.isChangeMaster());
		}
		this.jedisConfig = config;
		this.cluName = config.getCluName();
		this.passwd = config.getPasswd();
		initJedisPool(config);
		if(log.isDebugEnabled()) {
			log.debug("初始化完成.. writePool: " + writeHost + " readHost size: " + readHost.size() + " pools size:"+pools.size());
			for (Iterator<Host> it = pools.keySet().iterator(); it.hasNext();) {
				Host h = (Host) it.next();
				log.debug("连接池服务器：" + h.getIp() + ":" +h.getPort());
			}
		}
		PoolMonitor monitor = new PoolMonitor();
		monitor.start();
	}

	/**
	 * 缓存池信息初使化
	 */
	protected void initJedisPool(JedisConfig config) {
		if (log.isDebugEnabled()) {
			log.debug("service:" + config.getServices());
			log.debug("wei:" + config.getWeith());
			log.debug("MaxActive: " + config.getMaxActive());
			log.debug("MaxIdle: " + config.getMaxIdle());
			log.debug("MinIdle: " + config.getMinIdle());
			log.debug("MaxWait: " + config.getMaxWait());
			log.debug("type: " + config.getType());
		}
		String[] service = config.getServices();
		Integer[] wei = config.getWeith();
		// 验证参数的合法性
		validate(service, wei, config.getMaxActive(), config.getMaxIdle(),
				config.getMinIdle());
		
		int sSize = service.length;
		for (int i = 0; i < sSize; i++) {
			JedisPoolConfig jcon = new JedisPoolConfig();
			if (config.getMaxActive() != null) {
				jcon.setMaxActive(config.getMaxActive());
			}
			if (config.getMaxIdle() != null) {
				jcon.setMaxIdle(config.getMaxIdle());
			}
			if (config.getMinIdle() != null) {
				jcon.setMinIdle(config.getMinIdle());
			}
			jcon.setMaxWait(config.getMaxWait());
			String[] se = parserSerivce(service[i]);
			JedisPool pool = new JedisPool(jcon, se[0], Integer.parseInt(se[1]));
			Host host = new Host();
			host.setIp(se[0]);
			host.setPort(Integer.parseInt(se[1]));
			boolean isMaster = isMaster(pool);
			pools.put(host, pool);
			if(Type.CENTER.equals(config.getType())) {
				if(isMaster) {
					if(null==writeHost) {
						// 如果主机为空
						writeHost=host;
					} else {
						log.error("主机已存在," + host.getIp() + "加入写池失败..");
					}
				}
				readHost.add(host);
			} else if(Type.NOCENTER.equals(config.getType())) {
				// 一般限制非中心类型缓存池只能有一台服务器
				writeHost=host;
				readHost.add(host);
			}
		}
		
		if (writeHost == null) {
			log.warn("master 服务器个数为0,可能会造成数据无法写入");
		}

		if (readHost == null || readHost.size() == 0) {
			log.warn("slave 服务器个数为0,可能会造成数据无法读取");
		}
	}

	/**
	 * 将服务器字符器解释成ip加端口的形式
	 * 
	 * @param serivceStr
	 *            服务器字符串
	 * @return
	 */
	private String[] parserSerivce(String serivceStr) {
		if (serivceStr.indexOf(":") <= 0) {
			throw new RuntimeException("服务器配置格式错误serviceStr: " + serivceStr);
		}
		int maIndex = serivceStr.indexOf("master@");
		if (maIndex >= 0) {
			serivceStr = serivceStr.substring(maIndex + 6);
		}

		String[] re = serivceStr.split(":");
		return re;
	}
	
	private boolean isMaster(JedisPool pool) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			if(null != this.passwd&&!"".equals(this.passwd)) {
				String result = jedis.auth(passwd);
				log.debug(result);
			}
			String info = jedis.info();
			pool.returnResource(jedis);
			if (info.indexOf("role:master") > 0) {
				return true;
			}
		} catch (Exception ex) {
			log.error(ex, ex);
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		}
		return false;
	}

	/**
	 * 获取jedis pool
	 * 
	 * @param mod
	 *            读写模式
	 * @param key
	 *            缓存key值
	 * @return
	 */
	public JedisPool getJedisPool(Mode mod, String key) {
		Host host = null;
		switch (mod) {
		case WRITER:
			if(null == writeHost) {
				return null;
			}
			host = writeHost;
			break;
		case READ:
			if(0 == readHost.size()) {
				return null;
			}
			host = readHost.get(getJedisIndex(readHost.size(), key));
		}
		JedisPool pool = pools.get(host);
		Jedis je = null;
		try {
			je = pool.getResource();
			if(null!=passwd&&!"".equals(passwd))
				je.auth(passwd);
			pool.returnResource(je);
		} catch (Exception ex) {
			if (pool != null) {
				pool.returnBrokenResource(je);
			}
			throw new RuntimeException("缓存连接池无效");
		}
		return pool;
	}

	public Map<JedisPool, List<String>> getJedisPool(Mode mod, String... keys) {
		Map<JedisPool, List<String>> jedisMap = new HashMap<JedisPool, List<String>>();
		switch (this.jedisConfig.getType()) {
		case CENTER:
			JedisPool pool = getJedisPool(mod, keys[0]);
			jedisMap.put(pool, Arrays.asList(keys));
			break;
		case NOCENTER:
			for (String key : keys) {
				pool = getJedisPool(mod, keys[0]);
				List<String> k = jedisMap.get(key);
				if (k == null) {
					k = new ArrayList<String>();
					jedisMap.put(pool, k);
				}
				k.add(key);
			}
		}
		return jedisMap;
	}

	protected int getJedisIndex(int serviceListSize, String key) {
		int hashCode = key.hashCode();
		if (hashCode < 0) {
			return 0;
		} else {
			return hashCode % serviceListSize;
		}
	}

	public String getCluName() {
		return cluName;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	class PoolMonitor extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.currentThread().sleep(5000);
					log.debug(jedisConfig.getCluName() + "]开始检测服务器config...");
					MClient mclient=new MClient("conf");
					mclient.execute();
					String result = mclient.getResult();
					if(result!=null && !result.equals(serverStatus)) {
						// 获得状态或者状态发生改变时才更新缓存池
						updatePools(getStatus(result));
						serverStatus = result;
					}
				} catch (Exception e) {
					log.error("检测失败!", e);
				}
			}
		}
		
		private HashMap<String,String> getStatus(String result) {
			if(null == result) {
				return null;
			} else {
				HashMap<String,String> hm=new HashMap<String,String>();
				String[] hosts = result.split(";");
				if(hosts.length > 0) {
					for (int i = 0; i < hosts.length; i++) {
						String[] ser = hosts[i].split("@");
						if(ser.length==2) {
							hm.put(ser[1], ser[0]);
						}
					}
				}
				return hm;
			}
		}
		
		private void updatePools(HashMap<String,String> poolsStatus) {
			if(null==poolsStatus) {
				return;
			}
			writeHost = null;
			readHost.clear();
			for (Iterator<Host> iterator = pools.keySet().iterator(); iterator.hasNext();) {
				Host host = iterator.next();
				String key=host.getIp() + ":" + host.getPort();
				String status = poolsStatus.get(key);
				if(null!=status) {
					// 连接池中主机
					if (Type.CENTER.equals(jedisConfig.getType())) {
						// 中心式
						if ("master".equals(status)) {
							writeHost = host;
							readHost.add(host);
						} else if ("slave".equals(status)) {
							readHost.add(host);
						}
					} else {
						// 非中心式，通常只有一台主机
						writeHost = host;
						readHost.add(host);
					}
				}
			}
			if(log.isDebugEnabled()) {
				log.debug(jedisConfig.getCluName() + "]writePool: " + writeHost + " readHost size: " + readHost.size());
			}
		}
	}
}
