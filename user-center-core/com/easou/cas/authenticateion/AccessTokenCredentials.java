package com.easou.cas.authenticateion;

import org.jasig.cas.authentication.principal.Credentials;

public class AccessTokenCredentials implements Credentials {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3236191617020809365L;
	// 登陆验证code
	private String accToken;
	private String easouId;
	private String thirdId;
	private String type;
	private String passwd;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccToken() {
		return accToken;
	}

	public void setAccToken(String accToken) {
		this.accToken = accToken;
	}

	public String getEasouId() {
		return easouId;
	}

	public void setEasouId(String easouId) {
		this.easouId = easouId;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
