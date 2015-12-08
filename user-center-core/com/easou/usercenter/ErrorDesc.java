package com.easou.usercenter;

/**
 * 错误描述
 * 
 * @author damon
 * @since 2013.02.16
 * @version 1.0
 */
public class ErrorDesc {
	//错误编码
	private int code;
	//错误描述
	private String desc;
	
	public ErrorDesc(int code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public ErrorDesc build(int code, String desc) {
		this.code = code;
		this.desc = desc;
		return this;
	}

}
