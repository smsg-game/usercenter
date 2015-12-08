package com.easou.usercenter.cache.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisStatusUtil {

	/**
	 * 判定当前机器是否是为主机
	 * 
	 * @param jedis
	 *            redis操作对像
	 * @return
	 */
	public static boolean isMaster(String serviceStr, JedisPool pool,
			boolean hasCheckMater) {
		if (!hasCheckMater) {
			if (serviceStr.indexOf("master@") >= 0)
				return true;
		} else {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				String info = jedis.info();
				pool.returnResource(jedis);
				if (info.indexOf("role:master") > 0) {
					return true;
				}
			} catch (Exception ex) {
				if (jedis != null)
					pool.returnBrokenResource(jedis);
			}
		}
		return false;
	}

	/**
	 * 判定当前服务是否可用
	 * 
	 * @param jedis
	 *            redis操作对象
	 * @return
	 */
	public static boolean hasAvail(JedisPool pool) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String pi = jedis.ping();
			pool.returnResource(jedis);
			if ("PONG".equals(pi)) {
				return true;
			}
		} catch (Exception ex) {
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		}
		return false;
	}
}
