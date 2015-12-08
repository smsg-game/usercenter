package com.easou.usercenter.cache.jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.easou.common.json.JsonUtil;
import com.easou.usercenter.cache.Cache;
import com.easou.usercenter.cache.jedis.test.TestBean2;

public class EJedis implements Cache {
	
	Log log = LogFactory.getLog(EJedis.class);

	private String cluName = "default";

	public String getCluName() {
		return cluName;
	}

	public void setCluName(String cluName) {
		this.cluName = cluName;
	}

	public EJedis() {
	}

	public EJedis(String cluName) {
		this.cluName = cluName;
	}
	
	private EJedisPoolImpl poolImpl = null;
	
	private EJedisPoolImpl getEjedisPool() {
		if(null == this.poolImpl)
			this.poolImpl = JedisManager.getInstance().getEjedisPool(cluName);
		return this.poolImpl;
	}

	@Override
	public <T> T get(String key, Class<T> objCla) {
		Jedis je = null;
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		try {
			je = pool.getResource();
			String result = je.get(key);
			this.returnJedisClient(pool, je);
			if (result == null)
				return null;
			return JsonUtil.parserStrToObject(result, objCla);
		} catch (Exception ex) {
			if (pool != null) {
				this.returnBrokenResource(pool, je);
			}
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 获取字符串数据信息
	 * 
	 * @param key
	 *            数据缓存key值
	 * @return
	 */
	public String get(String key) {
		return get(key, String.class);
	}

	@Override
	public boolean isExist(String key) {
		Jedis je = null;
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		try {
			je = pool.getResource();
			boolean re = je.exists(key);
			this.returnJedisClient(pool, je);
			return re;
		} catch (Exception ex) {
			if (pool != null) {
				this.returnBrokenResource(pool, je);
			}
			throw new RuntimeException(ex);
		}
	}

	@Override
	public Map mget(String[] key, Map<String, Class> cla) {
		Map<JedisPool, List<String>> pools = getEjedisPool().getJedisPool(Mode.READ, key);
		Map resultMap = new HashMap();
		Iterator<JedisPool> it = pools.keySet().iterator();
		while (it.hasNext()) {
			JedisPool jp = it.next();
			List<String> k = pools.get(jp);
			Jedis j = null;
			List<String> re = null;
			try {
				j = jp.getResource();
				re = j.mget(k.toArray(new String[k.size()]));
				this.returnJedisClient(jp, j);
			} catch (Exception ex) {
				this.returnBrokenResource(jp, j);
				throw new RuntimeException(ex);
			}
			if (re != null)
				for (int i = 0; i < re.size(); i++) {
					resultMap.put(k.get(i), JsonUtil.parserStrToObject(re
							.get(i), cla.get(k.get(i))));
				}
		}
		return resultMap;

	}

	/**
	 * 从缓存中同时获取多个类型的数据
	 * 
	 * @param key
	 *            缓存key值集合
	 * @param cla
	 * @return
	 */
	public <T extends Object> Map<String, T> mget(String[] key, Class<T> cla) {
		if (key == null)
			return null;
		Map<String, Class> map = new HashMap<String, Class>();
		for (String k : key) {
			map.put(k, cla);
		}
		return mget(key, map);
	}

	@Override
	public boolean mulSet(Map<String, Object> el) {
		Set<String> keys = el.keySet();
		Map<JedisPool, List<String>> pools = getEjedisPool().getJedisPool(Mode.WRITER, keys
				.toArray(new String[keys.size()]));
		Iterator<JedisPool> it = pools.keySet().iterator();
		while (it.hasNext()) {
			JedisPool po = it.next();
			Jedis j = null;
			String re = "";
			try {
				j = po.getResource();
				List<String> k = pools.get(j);
				String[] ka = new String[k.size() * 2];
				for (int i = 0; i < k.size(); i = i + 2) {
					ka[i] = k.get(i);
					ka[i + 1] = JsonUtil.parserObjToJsonStr(el.get(k.get(i)));
				}
				re = j.mset(ka);
				this.returnJedisClient(po, j);
			} catch (Exception ex) {
				this.returnBrokenResource(po, j);
				throw new RuntimeException(ex);
			}
			if (!hasSu(re)) {
				return hasSu(re);
			}
		}
		return true;
	}

	@Override
	public boolean set(String key, Object value) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			String re = je.set(key, JsonUtil.parserObjToJsonStr(value));
			this.returnJedisClient(pool, je);
			return hasSu(re);
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean set(String key, Object value, int expire) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			String re = je.setex(key, expire, JsonUtil
					.parserObjToJsonStr(value));
			this.returnJedisClient(pool, je);
			return hasSu(re);
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	private boolean hasSu(String re) {
		if (!"OK".equals(re))
			return false;
		else
			return true;
	}

	@Override
	public boolean del(String key) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			long re = je.del(new String[] { key });
			this.returnJedisClient(pool, je);
			if (re >= 0)
				return true;
			return false;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean mdel(String... key) {
		Map<JedisPool, List<String>> pools = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Iterator<JedisPool> it = pools.keySet().iterator();
		while (it.hasNext()) {
			JedisPool pool = it.next();
			Jedis j = null;
			try {
				j = pool.getResource();
				List<String> k = pools.get(pool);
				boolean res = true;
				for (int i = 0; i < k.size(); i++) {
					Long re = j.del(k.toArray(new String[k.size()]));
					if (re <= 0) {
						res = false;
						break;
					}
				}
				this.returnJedisClient(pool, j);
				if (!res)
					return res;
			} catch (Exception ex) {
				this.returnBrokenResource(pool, j);
				throw new RuntimeException(ex);
			}
		}

		return true;
	}

	/**
	 * 设置指定key下的数据失效
	 * 
	 * @param key
	 *            缓存key值
	 * @param sec
	 *            失效时间(秒)
	 */
	public void expire(String key, int sec) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			je.expire(key, sec);
			this.returnJedisClient(pool, je);
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	public void hdel(String key, String field) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			je.hdel(key, field);
			this.returnJedisClient(pool, je);
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	public boolean hexists(String key, String field) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			boolean re = je.hexists(key, field);
			this.returnJedisClient(pool, je);
			return re;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 获取指定key下的指定字段
	 * 
	 * @param <T>
	 * @param key
	 *            缓存key值
	 * @param field
	 *            字段名称
	 * @param t
	 *            字段数据类型
	 * @return
	 */
	public <T extends Object> T hget(String key, String field, Class<T> t) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			String value = je.hget(key, field);
			this.returnJedisClient(pool, je);
			if (value == null)
				return null;
			T re = JsonUtil.parserStrToObject(value, t);
			return re;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	public Map<String, String> hGetAll(String key) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			Map<String, String> res = je.hgetAll(key);
			this.returnJedisClient(pool, je);
			return res;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	public Set<String> hkeys(String key) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			Set<String> set = je.hkeys(key);
			this.returnJedisClient(pool, je);
			return set;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 将map数据按照hash的形式存放到redis
	 * 
	 * @param key
	 *            缓存key值
	 * @param hval
	 *            map数据信息
	 * @return
	 */
	public boolean hmset(String key, Map<String, Object> hval) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			Map<String, String> addMap = new HashMap<String, String>();
			Iterator<String> keys = hval.keySet().iterator();
			while (keys.hasNext()) {
				String k = keys.next();
				Object obj = hval.get(k);
				if (obj != null && !"class".equals(k))
					addMap.put(k, JsonUtil.parserObjToJsonStr(obj));
			}
			String result = je.hmset(key, addMap);
			this.returnJedisClient(pool, je);
			return hasSu(result);
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 将java bean数据对象以redis hash的形式加入缓存
	 * 
	 * @param key
	 *            缓存key值
	 * @param obj
	 *            java bean数据对象
	 * @return
	 */
	public boolean setHBean(String key, Object obj) {
		Map<String, Object> map = null;
		try {
			map = PropertyUtils.describe(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return hmset(key, map);
	}

	/**
	 * 从redis hash结构中获取指定key值的java bean数据对像
	 * 
	 * @param <T>
	 * @param key
	 *            缓存key值
	 * @param cla
	 *            数据对象类型
	 * @return
	 */
	public <T extends Object> T hGetBean(String key, Class<T> cla) {
		// 获取所有数据
		Map<String, String> map = this.hGetAll(key);
		if (map == null || map.size() == 0)
			return null;
		Iterator<String> keys = map.keySet().iterator();
		try {
			// 创建数据对象实例
			T t = cla.newInstance();
			while (keys.hasNext()) {
				String k = keys.next();
				Class cl = PropertyUtils.getPropertyType(t, k);
				if(null==cl) {
					log.error("error properties in redis: " + k);
					continue;
				}
				BeanUtils.setProperty(t, k, JsonUtil.parserStrToObject(map
						.get(k), cl));
			}
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean hset(String key, String field, Object value) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.WRITER, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			Long lon = je.hset(key, field, JsonUtil.parserObjToJsonStr(value));
			this.returnJedisClient(pool, je);
			if (lon == 1) {
				return true;
			}
			return false;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		} finally {

		}
	}

	/**
	 * 更新javabean的指定字段
	 * 
	 * @param key
	 *            缓存key值
	 * @param fieldName
	 *            字段名称
	 * @param value
	 *            字段值
	 * @return
	 */
	public boolean updateHBeanField(String key, String fieldName, Object value) {
		return hset(key, fieldName, value);
	}

	/**
	 * 获取指定key值的有效时间
	 * 
	 * @param key
	 *            缓存key值
	 * @return
	 */
	public Long ttl(String key) {
		JedisPool pool = getEjedisPool().getJedisPool(Mode.READ, key);
		Jedis je = null;
		try {
			je = pool.getResource();
			Long tl = je.ttl(key);
			this.returnJedisClient(pool, je);
			return tl;
		} catch (Exception ex) {
			this.returnBrokenResource(pool, je);
			throw new RuntimeException(ex);
		}

	}

	protected Jedis getJedisClient(JedisPool pool) {
		return pool.getResource();
	}

	/**
	 * 返回jedis客户端
	 * 
	 * @param pool
	 * @param jedis
	 */
	public void returnJedisClient(JedisPool pool, Jedis jedis) {
		if (jedis == null) {
			return;
		}
		pool.returnResource(jedis);
	}

	//
	public void returnBrokenResource(JedisPool pool, Jedis jedis) {
		if (jedis == null || pool == null)
			return;
		pool.returnBrokenResource(jedis);
	}


	public static void main(String[] str) {
		JedisManager manager = JedisManager.getInstance();
		manager.init();
		//
		// TestBean1 bean1 = new TestBean1();
		TestBean2 bean2 = new TestBean2();
		bean2.setS(1);
		bean2.setMk(19);
		try {
			int i = 0;
			while (true) {
				try {
					i++;
					EJedis ejedis = new EJedis();
					ejedis.set("a" + i, bean2);
					System.out.println(ejedis.get("a" + i));
					Thread.sleep(1000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// TestBean1 bean3 = new TestBean1();
			// bean3.setIs(true);
			// bean3.setKk(2);
			//
			// List<String> bl = new ArrayList<String>();
			// bl.add("31");
			// bl.add("32");
			// bean3.setL1(bl);
			//
			// Map<String, String> ma = new HashMap();
			// ma.put("3uuuu", "3sss");
			// ma.put("3iiii", "3ooooo");
			// bean3.setM1(ma);
			//
			// bean3.setName("3ui");
			// bean3.setSex(3);
			//
			// bean3.setBean2(bean2);
			// EJedis ej = new EJedis();
			// boolean a=ej.set("aa",bean3);
			// System.out.println(a);
			// TestBean1 b=ej.get("aa",TestBean1.class);
			// System.out.println(b.getBean2().getMk());
			// ej.set("bb","中国人");
			// String st=ej.get("bb",String.class);
			// List li=new ArrayList();
			// li.add("2");
			// li.add("1");
			// ej.set("s",li);
			//			
			// ej.expire("s",1);

			// System.out.println(ej.isExist("bb"));

			// List l=ej.get("s",List.class);
			// System.out.println(l);
			// String[] st=new String[]{"s","bb","aa"};
			// Map claClass=new HashMap();
			// claClass.put("s",List.class);
			// claClass.put("bb",String.class);
			// claClass.put("aa",TestBean1.class);
			// Map map=ej.mget(st, claClass);
			// System.out.println(map);

			// Map ms=new HashMap();
			// ms.put("is","dd");
			// ms.put("uk","ll");
			// ej.mulSet(ms);
			//
			// Map m = ej.mget(new String[] { "is", "uk" }, String.class);
			// System.out.println(m);
			// ej.set("1", "2");
			// ej.set("3", "4");
			//			
			// System.out.println(ej.get("1"));
			// System.out.println(ej.get("3"));
			//			
			// ej.mdel("1","3");
			// System.out.println(ej.get("1"));
			// System.out.println(ej.get("3"));
			// ej.del("ha");
			// ej.setHBean("ha", bean3);
			// TestBean1 b = ej.hGetBean("ha", TestBean1.class);
			// System.out.println(b.getName());
			// ej.updateHBeanField("ha", "name", "update");
			// System.out.println(ej.hget("ha", "name", String.class));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
