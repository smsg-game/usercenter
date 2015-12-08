package com.easou.cas.auth;

import com.easou.common.api.JUser;

/**
 * 鉴权返回结果
 * 
 * @author damon
 * @since 2012.06.27
 * @version 1.0
 *
 */
public class EucAuthResult implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4782288831244671067L;

	private EucToken token;
	/*U信息*/
	private EucUCookie u;
	/*ESID*/
	private String esid;
	/*是否是注册登录*/
	private boolean isRegist;
	
	private JUser user;
	
	public EucUCookie getU() {
		return u;
	}

	public void setU(EucUCookie u) {
		this.u = u;
	}

	public boolean isRegist() {
		return isRegist;
	}

	public void setRegist(boolean isRegist) {
		this.isRegist = isRegist;
	}

	public String getEsid() {
		return esid;
	}

	protected void setEsid(String esid) {
		this.esid = esid;
	}

	/**
	 * 构造函数
	 * 
	 * @param jUser
	 * @param token
	 * @param esid
	 * @param isRegist
	 *     注册登录标识
	 */
	public EucAuthResult(EucToken token,JUser user,String esid,boolean isRegist){
		this.token = token;
		this.user = user;
		this.esid = esid;
		this.isRegist = isRegist;
	}
	
	
	/**
	 * 构造函数
	 * 
	 * @param jUser
	 * @param token
	 * @param esid
	 * @param isRegist
	 *     注册登录标识
	 */
	public EucAuthResult(EucToken token,JUser user,EucUCookie u,String esid,boolean isRegist){
		this.token = token;
		this.user = user;
		this.u = u;
		this.esid = esid;
		this.isRegist = isRegist;
	}

	/**
	 * 通行证
	 * 
	 * @param token
	 */
	public EucToken getToken() {
		return token;
	}
	
	protected void setToken(EucToken token) {
		this.token = token;
	}

	/**
	 * 用户信息
	 */
	public JUser getUser() {
		return user;
	}

	public void setUser(JUser user) {
		this.user = user;
	}
}
