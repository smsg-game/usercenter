package com.easou.usercenter.util;

/**
 * 日志大小
 * 
 * @author damon
 * @since 2012.07.31
 * @version 1.0
 *
 */
public enum LogSize {
	
	L_SIZE(20),M_SIZE(15),S_SIZE(10);
	
	protected int length;
	
	LogSize(int length){
		this.length = length;
	}

}
