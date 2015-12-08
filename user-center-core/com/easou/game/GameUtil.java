package com.easou.game;

import javax.servlet.http.HttpServletRequest;
/**
 * GAME公共类
 * 
 * @author damon
 *
 */
public class GameUtil {
	
	/**
	 * 获取鉴权地址，后面扩展时需要根据相对路径跳转到对应不同版本的URL鉴权地址中去
	 * 
	 * @param request
	 * @return
	 */
	public static String getAuthURL(HttpServletRequest request){
		return GameConfig.getProperty("auth.url");
	}

}
