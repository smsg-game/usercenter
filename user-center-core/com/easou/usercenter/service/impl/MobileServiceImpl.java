package com.easou.usercenter.service.impl;

import com.easou.common.constant.CachePrefixConstant;
import com.easou.common.util.MD5Util;
import com.easou.common.util.RandomKeyGenerator;
import com.easou.usercenter.cache.Cache;
import com.easou.usercenter.cache.jedis.EJedis;
import com.easou.usercenter.service.MobileService;

//@Service
public class MobileServiceImpl implements MobileService {

	//@Resource(name = "defaultCache")
	private EJedis cache;

	public EJedis getCache() {
		return cache;
	}

	public void setCache(EJedis cache) {
		this.cache = cache;
	}

	@Override
	public String getVeriCodeByMobile(String mobile) {
		return cache.get(CachePrefixConstant.VERI_CACHE_PREFIX + mobile);
	}

	@Override
	public String setVeriCodeByMobile(String mobile) {
		String key = RandomKeyGenerator.genNumber(6);
		if (cache.set(CachePrefixConstant.VERI_CACHE_PREFIX + mobile, key,
				Cache.THREE_MINUTE)) {
			return key;
		} else {
			return "";
		}
	}

	@Override
	public boolean delVeriCodeByMobile(String mobile) {
		return cache.del(CachePrefixConstant.VERI_CACHE_PREFIX + mobile);
	}

	@Override
	public String registerMobile(String mobile, String pass) {
		if (null != cache.get(CachePrefixConstant.MOBREG_CACHE_PREFIX + mobile)) {
			return REG_REPEAT_ERR;
		}
		if (cache.set(CachePrefixConstant.MOBREG_CACHE_PREFIX + mobile, MD5Util
				.md5(pass).toLowerCase(), Cache.THREE_MINUTE)) {
			return SUCCESS;
		} else {
			return CACHE_ERROR;
		}
	}
	
	@Override
	public String getRegisterMobile(String mobile) {
		return cache.get(CachePrefixConstant.MOBREG_CACHE_PREFIX + mobile);
	}
	
	@Override
	public boolean delRegisterMobile(String mobile) {
		return cache.del(CachePrefixConstant.MOBREG_CACHE_PREFIX + mobile);
	}

	@Override
	public String getEmailByVeriCode(String code) {
		return cache.get(CachePrefixConstant.EMAIL_VERI_CACHE_PREFIX + code);
	}

	@Override
	public boolean setVeriCodeByEmail(String code,String email,String userId,String lan) {
		if (cache.set(CachePrefixConstant.EMAIL_VERI_CACHE_PREFIX + code, email+":"+userId+":"+lan,
				Cache.ONE_DAY)) {
			return true;
		} else {
			return false;
		}
	}
}
