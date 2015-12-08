package com.easou.usercenter.web.user;

import org.apache.log4j.Logger;

public class ArrestValidation {
	
	private Logger log = Logger.getLogger(getClass());
	
	private String userId;
	private String backUrl;
	private String backChn;
	private String retMessage;

	private boolean isLogin = false;

    public ArrestValidation(boolean isLog) {
    	this.isLogin = isLog;
    }

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBackChn() {
		return backChn;
	}

	public void setBackChn(String backChn) {
		this.backChn = backChn;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	
	public String getRetMessage() {
		return retMessage;
	}

	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}
	
}
