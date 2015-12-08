package com.easou.cas.authenticateion;

import org.jasig.cas.authentication.handler.PasswordEncoder;

import com.easou.common.util.MD5Util;
import com.easou.common.util.StringUtil;

public class Md5PwdEncoder implements PasswordEncoder {

	@Override
	public String encode(String password) {
		// TODO Auto-generated method stub
		if(StringUtil.isEmpty(password)){
			return "";
		}
		return MD5Util.md5(password).toLowerCase();
	}
	
	public static void main(String[] args) {
		String s = "1$";
		String[] infos = s.split("\\$");
		System.out.println(infos.length);
	}

}
