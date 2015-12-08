package com.easou.usercenter.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.easou.usercenter.cache.jedis.EJedis;

public class BaseDaoImpl extends SqlSessionDaoSupport {

	//@Resource(name="defaultCache")
	protected EJedis cache;

	public EJedis getCache() {
		return cache;
	}

	public void setCache(EJedis cache) {
		this.cache = cache;
	}

}
