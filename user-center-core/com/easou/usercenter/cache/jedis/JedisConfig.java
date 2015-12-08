package com.easou.usercenter.cache.jedis;

public class JedisConfig {
	// 每个service的簇名
	private String cluName;

	// jedis服务器ip与端口:127.0.0.1:8080
	private String[] services;
	
	// redis服务器权重配置
	private Integer[] weith;

	// 最大活动连接数
	private Integer maxActive;

	// 最大空闲连接:连接池中容许保持空闲状态的最大连接数量,超过的空闲连接将被释放,如果设置为负数表示不限制
	private Integer maxIdle;

	// 最小空闲连接:连接池中容许保持空闲状态的最小连接数量,低于这个数量将创建新的连接,如果设置为0则不创建
	private Integer minIdle;

	// 最大等待时间:当没有可用连接时,连接池等待连接被归还的最大时间(以毫秒计数),超过时间则抛出异常,如果设置为-1表示无限等待
	private Long maxWait;

	// 模型(1.中心式,2.无中心式)
	private Type type = Type.CENTER;
	
	//是否需要做主机异常检查，如果只有一台主机的情况下应为false，不再切换主机
	private boolean changeMaster=true;
	
	private String passwd;

 
	public boolean isChangeMaster() {
		return changeMaster;
	}

	public void setChangeMaster(boolean changeMaster) {
		this.changeMaster = changeMaster;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String[] getServices() {
		return services;
	}

	public void setServices(String[] services) {
		this.services = services;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Long maxWait) {
		this.maxWait = maxWait;
	}

	public Integer[] getWeith() {
		return weith;
	}

	public void setWeith(Integer[] weith) {
		this.weith = weith;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getCluName() {
		return cluName;
	}

	public void setCluName(String cluName) {
		this.cluName = cluName;
	}
}
