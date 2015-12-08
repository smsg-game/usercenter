package com.easou.usercenter.entity;

import java.util.Date;

public class EucUser {
	
	private Long id;					// id
	private String name;			// 用户名
	private String nickName;	// 用户昵称
	private String mobile;		// 手机号
	private Integer status;		// 状态
	private Integer sex;			// 性别
	private Integer occuId;		// 职业id
	private Integer birthday;	// 出生年月(数值)
	private String city;			// 城市
	private String question;	// 问题
	private String answer;		// 答案
	private String passwd;		// 密码
	private Date bindTime;		// 手机绑定时间
	private Date registerTime;	// 注册时间
	private Date lastLoginTime;	// 最后登录时间
	private String randomPasswd; // 随机密码
	private String email="";
	private String qn;

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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQn() {
		return qn;
	}

	public void setQn(String qn) {
		this.qn = qn;
	}
	
}
