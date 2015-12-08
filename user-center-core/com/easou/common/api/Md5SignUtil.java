package com.easou.common.api;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
/**
 * 签名
 * 
 * @author damon
 * @since 2013.01.24
 * @version 1.0
 *
 */
public class Md5SignUtil {
	


	/*签名*/
	public static String sign(Map<String, String> map,String key) {
		String content = getStringForSign(map);
	    return sign(content, key);
	}
	

	/*签名*/
	public static String sign(String content,String key) {
			String veriCode = 
					MD5.digest(content+key);
			return veriCode;
	}
	
	
	/*验签不区分大小写*/
	public static boolean doCheck(Map<String,String> map,String sign,String key){
		String content = getStringForSign(map);
		return doCheck(content, sign, key);
	}
	
	/*验签不区分大小写*/
	public static boolean doCheck(String content,String sign,String key){
			String veriCode = 
					MD5.digest(content+key);
			if(veriCode.equalsIgnoreCase(sign)) 
				return true;
		return false;
	}
	
	/*获取待签名字符串*/
	public static String getStringForSign(Map<String,String> map){
		StringBuilder sb = new StringBuilder();
		TreeMap<String, String> treeMap = new TreeMap<String, String>(map);
		if (treeMap != null) {
			for(Map.Entry<String,String> entity : treeMap.entrySet()){
				if(entity.getKey()!=null && entity.getValue()!=null){
					sb.append(entity.getKey()).append("=").append(String.valueOf(entity.getValue())).append("&");
				}
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("userName", "test");
		map.put("password", "111111");
		map.put("remember", "false");
		String content=getStringForSign(map);
		String key="abcd";
		System.out.println(content);
		System.out.println(MD5.digest(content+key));
	}
	
}
