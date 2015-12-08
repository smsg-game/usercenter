package com.easou.common.constant;
/**
 * 短信类型
 * 
 * @author damon
 *
 */
public enum SMSType {
	REGISTER("100"),
	BIND("200"),
	FORGET_PASS("400");
	
	public String value;
	
	SMSType(String value){
		this.value = value;
	}

}
