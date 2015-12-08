package org.jasig.cas.authentication;

/**
 * 验证结果信息类
 * 
 * @author river
 * 
 */
public class AuthenticationResult {
	/**
	 * 验证结果信息
	 */
	private boolean success;
	/**
	 * 注册登录
	 * 如果用户是通过接受短信密码后第一次登陆的，此标识为true
	 */
	private  boolean isRegist;

	public boolean isRegist() {
		return isRegist;
	}

	public void setRegist(boolean isRegist) {
		this.isRegist = isRegist;
	}

	public boolean isSuccess() {
		return success;
	}

	/**
	 * 验证后用户信息对象
	 */
	private Object authInfo;

	public AuthenticationResult(boolean result, Object info) {
		this.success = result;
		this.authInfo = info;
		this.isRegist = false;
	}
	
	public AuthenticationResult(boolean result, Object info,boolean isRegist) {
		this.success = result;
		this.authInfo = info;
		this.isRegist = isRegist;
	}

	public Object getAuthInfo() {
		return authInfo;
	}

}
