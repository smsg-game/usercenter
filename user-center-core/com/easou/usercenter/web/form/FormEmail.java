package com.easou.usercenter.web.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

public class FormEmail {

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	private String confirm;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
}
