package com.easou.usercenter.datasource;

import java.util.Map;

import com.easou.common.constant.Server;
import com.easou.usercenter.cache.jedis.EJedis;

/**
 * 
 * @author damon
 *
 */
public class DynamicCacheSource {
	
	public Map<CacheSourceCluster,EJedis> eJedisMap;
	
	public Map<CacheSourceCluster, EJedis> geteJedisMap() {
		return eJedisMap;
	}

	public void seteJedisMap(Map<CacheSourceCluster, EJedis> eJedisMap) {
		this.eJedisMap = eJedisMap;
	}

	/**
	 * 获取缓存控制器
	 * 
	 * @param cache
	 * @return
	 */
	public EJedis getEJedis(CacheSourceCluster cache){
		return eJedisMap.get(cache);
	}
	
	
	/**
	 * 
	 * @param server
	 * @return
	 */
	public EJedis getEJedisByServer(Server server){
		if(server==Server.DEFAULT){
			return eJedisMap.get(CacheSourceCluster.DEFAULT_CACHE_SOURCE);
		}else if(server==Server.DIANXIN){
			return eJedisMap.get(CacheSourceCluster.WRITE_CACHE_SOURCE);
		}
		return null;
	}

}
