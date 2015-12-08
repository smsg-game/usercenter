package org.jasig.cas.http;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存服务对象
 * 
 * @author river
 * 
 */
public class CacheService {
	
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);
	
	private MemcachedClient client;

	public void setClient(MemcachedClient client) {
		this.client = client;
	}

	private static CacheService cacheService = null;

	private CacheService() {
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static CacheService getInstance() {
		if (cacheService == null) {
			cacheService = new CacheService();
		}
		return cacheService;
	}

	/**
	 * 将数据添加到缓存中
	 * 
	 * @param key
	 *            缓存key值
	 * @param value
	 *            缓存值
	 * @param effectTime
	 *            缓存失效时间
	 * @return
	 */
	public boolean add(String key, Object value, int effectTime) {
//		java.util.Calendar ca = Calendar.getInstance();
//		ca.add(Calendar.SECOND, effectTime);
		try {
			return client.set(key, effectTime, value);
		} catch (Throwable e) {
			log.error("add to memcached id:" + key + " ERROR! ", e);
		}
		return false;
	}

	/**
	 * 从缓存中获取数据值
	 * 
	 * @param key
	 *            缓存key值
	 * @return
	 */
	public Object get(String key) {
		try {
			return client.get(key);
		} catch (Throwable e) {
			log.error("get from memcached id:" + key + " ERROR! ", e);
		}
		return null;
	}
	
}
