package com.easou.usercenter.service;

import com.easou.cas.auth.EucToken;

/**
 * 鉴权校验
 * 
 * @author damon
 * @since 2012.07.02
 * @version 1.0
 *
 */
//@Service
public interface AuthVerifyService {

	/**
	 * 鉴权信息
	 * 
	 * @param token
	 *     鉴权信息
	 * @return
	 */
	public Long verify(EucToken token);
}
