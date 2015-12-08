package com.easou.usercenter.util;

import java.util.Date;

import com.easou.common.util.MD5Util;

public class SignatureUtil {
	
	private final static int RADIX = 36;
	private final static int START_POS = 4;
	private final static int END_POS = 24;

	public static String encodeSecert(Long uid, String key) {
		if(null==uid || null==key) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(uid).append(key);
		String secert = MD5Util.md5(sb.toString()).substring(START_POS, END_POS) + Long.toString(uid, RADIX);
		return secert;
	}
	
	public static Long decodeSecert(String secert) {
		if(null==secert || secert.length()<(END_POS - START_POS + 1)) {
			return null;
		}
		String uid = secert.substring(END_POS - START_POS);
		return Long.parseLong(uid, RADIX);
	}
	
	public static void main(String[] args) {
		Date date = new Date();
		String secert = encodeSecert(29999999l,"abcd");
		System.out.println(secert);
		System.out.println(decodeSecert(secert));
	}
}
