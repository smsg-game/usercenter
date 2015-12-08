package com.easou.common.constant;



/**
 * 登录类型
 * 
 * @author damon
 * @since 2012.05.16
 *
 */
public enum LoginType {
	/**
	 * 用户名密码登录
	 */
	DEFAULT("default"),
	/**
	 * TGC登录
	 */
	TGC("tgc"),
	/**
	 * 自动登录
	 */
	AUTO("auto"),
	/**
	 * 外部登录
	 */
	EXT("ext"),
	/**
	 * 无类型
	 */
	NULL("");
	
	private LoginType(String type){
		this.type = type;
	}
	public String type;
	
	public static LoginType getLoginType(String type){
		if(DEFAULT.type.equalsIgnoreCase(type)){
			return DEFAULT;
		}else if(EXT.type.equalsIgnoreCase(type)){
			return EXT;
		}else if(TGC.type.equalsIgnoreCase(type)){
			return TGC;
		}else if(AUTO.type.equalsIgnoreCase(type)){
			return AUTO;
		}else{
			return NULL;
		}
	}
}
