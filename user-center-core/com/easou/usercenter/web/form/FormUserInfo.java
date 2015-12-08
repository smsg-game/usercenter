package com.easou.usercenter.web.form;
/**
 * 用户信息
 * 
 * @author damon
 *
 */
public class FormUserInfo {
	
	//private Long id;			// id
	private String nickName;		// 用户名
	private Integer sex;		// 性别
	private Integer birthday;	// 出生年月(数值)
	private String city;		// 城市
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
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
	

}
