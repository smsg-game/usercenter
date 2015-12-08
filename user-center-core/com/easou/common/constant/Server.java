package com.easou.common.constant;
/**
 * 服务器类型
 * 
 * @author damon
 * @since 2012.05.07
 * @version 1.0
 *
 */
public enum Server {
	
	DIANXIN("dx","电信"),DEFAULT("default","默认");
	
	private Server(String type,String name){
		this.type = type;
		this.name = name;
	}
    
	public final String type;
	
	public final String name;
	
	/**
	 * 根据类型获取服务器
	 * 
	 * @param type
	 * @return
	 */
	public static Server getServerByType(String type){
		if(Server.DIANXIN.type.equalsIgnoreCase(type)){
			return Server.DIANXIN;
		}
		return Server.DEFAULT;
	}
	

}
