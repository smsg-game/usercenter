package com.easou.usercenter.service.impl;

import java.util.Random;

import com.easou.common.constant.CachePrefixConstant;
import com.easou.usercenter.cache.Cache;
import com.easou.usercenter.cache.jedis.EJedis;
import com.easou.usercenter.service.RegisterService;

public class RegisterServiceImpl implements RegisterService {
	
	private static int queueNo = new Random(System.currentTimeMillis()).nextInt(1000);
	
	private EJedis cache;

	public EJedis getCache() {
		return cache;
	}

	public void setCache(EJedis cache) {
		this.cache = cache;
	}
	
	synchronized private static int genQueueNo() {
		queueNo++;
		if(queueNo>100000000) {
			queueNo = 0;
		}
		return queueNo;
	}

	@Override
	public String genBookNum(String username) {
		String queueNo = genQueueNo()+"";
		boolean rst = cache.set(CachePrefixConstant.QUEUE_USER + username, queueNo, Cache.FIVE_MINUTE);
		if(rst) {
			return queueNo;
		} else {
			return null;
		}
	}

	@Override
	public String getBookNum(String username) {
		return cache.get(CachePrefixConstant.QUEUE_USER + username);
	}

	@Override
	public boolean delBookNum(String username) {
		return cache.del(CachePrefixConstant.QUEUE_USER + username);
	}
}
