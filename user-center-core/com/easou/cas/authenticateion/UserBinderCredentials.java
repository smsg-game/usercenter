package com.easou.cas.authenticateion;

import org.jasig.cas.authentication.principal.Credentials;

/**
 * 用户绑定信息对象
 * 
 * @author river
 * 
 */
public class UserBinderCredentials implements Credentials{
	// 用户名
	private String userName;
	// 用户密码
	private String pwd;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
