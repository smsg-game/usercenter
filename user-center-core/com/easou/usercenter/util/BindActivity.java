package com.easou.usercenter.util;
/**
 * 绑定动作
 * 
 * @author damon
 * @since 2012.06.12
 * @version 1.0
 * 
 * 增加直接绑定
 * @author damon
 * @since 2012.11.12
 * @version 1.0.1
 */
public enum BindActivity {
	/**
	 * 绑定请求
	 */
	REQUEST("200"),
	/**
	 * 绑定验证
	 */
	VERIFICATION("300"),
	
	/**
	 * 直接绑定
	 */
	DIRECTLY("400");
	
	protected String type;
	
	private BindActivity(String type){
		this.type = type;
	}
}
