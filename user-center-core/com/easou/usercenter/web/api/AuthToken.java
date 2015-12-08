package com.easou.usercenter.web.api;

import com.easou.cas.auth.EucToken;
/**
 * 验证票据
 * 
 * @author damon
 * @since 2012.07.05
 * @version 1.0
 *
 */
public class AuthToken{
	
	/**
	 * 用户 ID
	 */
	private Long id;
	
	private EucToken token;
	
	
	public Long getId() {
		return id;
	}


	protected void setId(Long id) {
		this.id = id;
	}
	

	public EucToken getToken() {
		return token;
	}


	protected void setToken(EucToken token) {
		this.token = token;
	}


	protected  AuthToken(Long id,EucToken token){
		this.id = id;
		this.token = token;
	}

}
