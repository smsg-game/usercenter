package com.easou.usercenter.web.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

public class FormUser {

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private String confirm;
	
	@NotBlank
	private String mobile;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
}
