package com.easou.usercenter.cache.jedis.config.moudle;

public class Service {
	private String cluName="default";
	private String type = "1";
	private String addr;
	private String weith;
	private String maxActive;
	private String maxIdle;
	private String minIdle;
	private String maxWait;
	private String passwd;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddr() {
		return addr;
	}

	public String getCluName() {
		return cluName;
	}

	public void setCluName(String cluName) {
		this.cluName = cluName;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getWeith() {
		return weith;
	}

	public void setWeith(String weith) {
		this.weith = weith;
	}

	public String getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}

	public String getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}

	public String getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(String minIdle) {
		this.minIdle = minIdle;
	}

	public String getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
