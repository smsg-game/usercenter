package com.easou.usercenter.service.impl;

import com.easou.common.util.MD5Util;
import com.easou.usercenter.cache.jedis.EJedis;

public class UrlTransService {
	
	private EJedis cache;

	public EJedis getCache() {
		return cache;
	}

	public void setCache(EJedis cache) {
		this.cache = cache;
	}

	public String saveUrl(String url) {
		if(null==url || "".equals(url)) {
			return "";
		}
		String md5Url = MD5Util.md5(url);
		cache.set(md5Url, url);
		return md5Url;
	}
	
	public String getUrl(String key) {
		return cache.get(key);
	}
}
