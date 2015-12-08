package com.easou.usercenter.cache.jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPool;

import com.easou.usercenter.cache.jedis.config.XmlConfig;
import com.easou.usercenter.cache.jedis.config.moudle.Clus;
import com.easou.usercenter.cache.jedis.config.moudle.Service;

public class JedisManager {
	private Map<String, EJedisPoolImpl> clusters = new HashMap<String, EJedisPoolImpl>();
	private static JedisManager manager = new JedisManager();
	private boolean hashInit = false;

	private JedisManager() {
	};

	public static JedisManager getInstance() {
		if (manager == null) {
			manager = new JedisManager();
		}
		return manager;
	}

	public void init() {
		if (hashInit)
			return;
		XmlConfig con = new XmlConfig();
		try {
			Clus clu = con.parser();
			Map<String, Service> services = clu.getService();
			Iterator<String> keys = services.keySet().iterator();
			while (keys.hasNext()) {
				Service service = services.get(keys.next());
				EJedisPoolImpl pool = new EJedisPoolImpl();
				pool.init(createJedisConfig(service));
				clusters.put(service.getCluName(), pool);
			}
			hashInit = true;
		} catch (Exception e) {
			throw new RuntimeException("redis初使化失败",e);
		}
	}

	private JedisConfig createJedisConfig(Service service) {
		JedisConfig jedisConfig = new JedisConfig();
		if (service.getAddr() == null) {
			throw new RuntimeException("缓存服务器地址必须配置");
		}
		if (service.getMaxActive() != null) {
			jedisConfig.setMaxActive(Integer.valueOf(service.getMaxActive()));
		}
		if (service.getMaxIdle() != null) {
			jedisConfig.setMaxIdle(Integer.valueOf(service.getMaxIdle()));
		}
		if (service.getMaxWait() != null) {
			jedisConfig.setMaxWait(Long.valueOf(service.getMaxWait()));
		}
		if (service.getMinIdle() != null) {
			jedisConfig.setMinIdle(Integer.valueOf(service.getMinIdle()));
		}
		if (service.getPasswd() != null) {
			jedisConfig.setPasswd(service.getPasswd());
		}
		if ("2".equals(service.getType())) {
			jedisConfig.setType(Type.NOCENTER);
			jedisConfig.setChangeMaster(false);
		} else if ("1".equals(service.getType())) {
			jedisConfig.setType(Type.CENTER);
		}
		// 获取所有的主机
		jedisConfig.setServices(service.getAddr().split(";"));
		jedisConfig.setCluName(service.getCluName());
		if (service.getWeith() != null) {
			String[] wei = service.getWeith().split(";");
			Integer[] it = new Integer[wei.length];
			for (int i = 0; i < it.length; i++) {
				it[i] = Integer.valueOf(wei[i]);
			}
			jedisConfig.setWeith(it);
		}
//		String[] sers = jedisConfig.getServices();
//		for (String ser : sers) {
//			if (ser.indexOf("mater@") >= 0) {
//				jedisConfig.setChangeMaster(false);
//			}
//		}
		return jedisConfig;
	}

	public EJedisPoolImpl getEjedisPool(String cluName) {
		return this.clusters.get(cluName);
	}

//	public JedisPool getJedisPool(String cluName, Mode mod, String key) {
//		EJedisPoolImpl po = getEjedisPool(cluName);
//		return po.getJedisPool(mod, key);
//	}
//
//	public Map<JedisPool, List<String>> getJedisPool(String cluName, Mode mod,
//			String... keys) {
//		EJedisPoolImpl po = getEjedisPool(cluName);
//		return po.getJedisPool(mod, keys);
//	}
	
	public static void main(String[] str) {
		JedisManager jm = JedisManager.getInstance();
		jm.init();
//		JedisPool jp = jm.getJedisPool("default", Mode.READ, "jay");
//		System.out.println(jp.toString());
	}
}
