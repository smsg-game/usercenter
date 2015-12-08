package com.easou.common.api;

public class JUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3853138146789650522L;
	private Long id; // id
	private String name; // 用户名
	private String nickName; // 用户昵称
	private String mobile; // 手机号
	private Integer status; // 状态
	private Integer sex; // 性别
	private Integer occuId; // 职业id
	private Integer birthday; // 出生年月(数值)
	private String city; // 城市
	private String randomPasswd; // 随机密码
	
	private String email="";//邮箱

	public String getRandomPasswd() {
		return randomPasswd;
	}

	public void setRandomPasswd(String randomPasswd) {
		this.randomPasswd = randomPasswd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getOccuId() {
		return occuId;
	}

	public void setOccuId(Integer occuId) {
		this.occuId = occuId;
	}

	public Integer getBirthday() {
		return birthday;
	}

	public void setBirthday(Integer birthday) {
		this.birthday = birthday;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
