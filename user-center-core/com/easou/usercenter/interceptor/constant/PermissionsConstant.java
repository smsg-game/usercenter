package com.easou.usercenter.interceptor.constant;
/**
 * 请求权限常量
 * 
 * @author damon
 * @since 2012.05.13
 *
 */
public class PermissionsConstant {
	
	//写请求
	public static String[] WRITE_PERMISSIONS_REQUEST = new String[]{"/api/requestBindMobile",
		"/api/applyBindMobile","/api/updateUser","/api/updatePasswd","/user/sbind","/user/bind",
		"/user/bindcfm","/user/changepass","/pass/sforget","/pass/findbym","/pass/findbyn",
		"/pass/resetpass","/pass/anwques","/pass/newpwd","/nregist","/mregist","/aregist","/smsregist"};
	//读请求
	//public static String[] READ_PERMISSIONS_REQUEST = new String[]{"/api/getOccupations",
	//	"/api/getUserById","/api/getUserByMobile","/oregist"};

}
