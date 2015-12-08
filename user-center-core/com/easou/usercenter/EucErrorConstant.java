package com.easou.usercenter;
/**
 * 错误提示
 * 
 * @author damon
 * @since 2013.02.17
 * @version 1.0
 */
public class EucErrorConstant {
	//更新失败
	public static Integer UPDATE_ERROR_CODE = 9;
	//更新失败
	public static String UPDATE_ERROR_DESC = "更新失败";
	//用户不存在
	public static Integer USER_NOT_EXIST_CODE = 1;
	//用户不存在
	public static String USER_NOT_EXIST_DESC = "用户不存在";
	//昵称已存在
	public static Integer NICK_NAME_ERROR_CODE = 2;
	//昵称已存在
	public static String NICK_NAME_EXIST_DESC = "该昵称已被注册";
	//城市名错误
	public static Integer CITY_NOT_EXIST_CODE = 3;
	//城市名错误
	public static String CITY_NOT_EXIST_DESC = "城市名称不正确";
	//性别错误
	public static Integer SEX_ERROR_CODE = 4;
	//性别错误
	public static String SEX_ERROR_DESC = "性别有误";

	//昵称已存在
	public static Integer NICK_NAME_NOT_CHANGE_CODE = 5;
	
	public static String NICK_NAME_NOT_CHANGE_DESC = "请输入新的匿名";
}
