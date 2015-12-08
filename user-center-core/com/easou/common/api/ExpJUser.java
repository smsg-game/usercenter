package com.easou.common.api;

/**
 * 扩展JUser对象
 * 
 * @author damon
 * @since 2012.11.26
 * @version 1.0
 *
 */
public class ExpJUser extends JUser{
	
	/**
	 * 用户密码
	 */
	private String passwd;

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	

}
