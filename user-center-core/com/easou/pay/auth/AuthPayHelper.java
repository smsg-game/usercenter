package com.easou.pay.auth;

import java.util.Map;
import java.util.TreeMap;

import com.easou.common.api.ClientConfig;
import com.easou.common.util.MD5Util;

/**
 * 支付支持类
 * 
 * @author damon
 * @since 2013.01.24
 * @version 1.0
 */
public class AuthPayHelper {
	
	private static AuthPayHelper helper;
	
	private AuthPayHelper(){
		
	}
	
	public static AuthPayHelper getIntance(){
		if(helper==null){
			helper = new AuthPayHelper();
		}
		return helper;
	}
	
	/**
	 * 
	 * 
	 * @param treeMap
	 * @return
	 */
    public String authSign(TreeMap<String, String> treeMap){
    	String tempStr = prepareStringForSign(treeMap);
    	String bStr = tempStr + ClientConfig.getProperty("secertKey");
    	return  MD5Util.md5(bStr);
    }
	
	/**
	 * 将treeMap中的key-value对取出，格式化为 key1=value2&key2=value2的形式 <br>
	 * 抛弃key为null的值对，将value为null的值转化为空字符串<br>
	 * <b>多线程环境中，调用者需保证此方法执行完成前，treeMap不被修改</b>
	 * @param map
	 * @return
	 */
	private String prepareStringForSign(TreeMap<String, String> treeMap) {

		StringBuilder sb = new StringBuilder();
		if (treeMap != null) {
			for(Map.Entry<String,String> entity : treeMap.entrySet()){
				if(entity.getKey()!=null && entity.getValue()!=null){
					sb.append(entity.getKey()).append("=").append(entity.getValue()).append("&");
				}
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
			}
		}
		return sb.toString();
	}
	
    

}
