package com.easou.common.util;

import com.easou.common.api.MD5;


public class MD5Util {

	/**
	 * MD5加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @return
	 */
	public static String md5(String str) {
		if (str == null) {
			return null;
		}
		try {
		return MD5.digest(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(md5("111111"));
	}
}
