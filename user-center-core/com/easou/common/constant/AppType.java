package com.easou.common.constant;
/**
 * 应用类型
 * APP/WEB/WAP
 * 
 * @author damon
 * @since 2012.05.22
 * @version 1.0
 *
 */
public enum AppType {
	/**
	 * 客户端应用类型
	 */
	APP("app"),
	/**
	 * web应用类型
	 */
	WEB("web"),
	/**
	 * wap应用类型
	 */
	WAP("wap");
	
	public String type;
	
	private AppType(String type){
		this.type = type;
	}
	
	/**
	 * 根据类型值获取类型
	 * 
	 * @param type
	 *     类型值
	 * @return
	 */
	public static AppType getAppType(String type){
		if(APP.type.equalsIgnoreCase(type)){
			return APP;
		}else if(WEB.type.equalsIgnoreCase(type)){
			return WEB;
		}else if(WAP.type.equalsIgnoreCase(type)){
			return WAP;
		}
		return null;
	}

}
