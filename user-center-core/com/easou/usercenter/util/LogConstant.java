package com.easou.usercenter.util;


/**
 * 日志常量
 * 
 * @author damon
 * @since 2012.05.15
 * @version 1.0
 * 
 * 增加了绑定手机号日志
 * @author damon
 * @since 2012.06.12
 *
 */
public class LogConstant extends Constant{
	
	public static int RESULT_SUCCESS = 100;
	public static int RESULT_FAILURE = 200;
	
	/****************************注册模块*******************************/
	/********************注册异常********************/
	/**注册成功*/
	public static String REGIS_RESULT_SUCCESS = "100";
	/**手机号已经被注册*/
	public static String REGIS_RESULT_MSISDN_EXIST = "201";
	/**非法手机号*/
	public static String REGIS_RESULT_MSISDN_ILLEGAL = "202";
	/**发送短信失败*/
	public static String REGIS_RESULT_MSISDN_SMS_FAILURE = "203";
	/**密码不符要求*/
	public static String REGIS_RESULT_MSISDN_PASS_ERR = "204";

	
	/**外部新浪微博用户验证失败*/
	public static String REGIS_RESULT_EXTERNAL_USER_SINA_VERIFICATION_FAILURE="711";
	/**外部新浪微博用户已存在着绑定的梵町用户*/
	public static String REGIS_RESULT_EXTERNAL_USER_SINA_BIND_EXIST="712";

	/**程序异常*/
	public static String REGIS_RESULT_EXCEPTION="901";
	/**输入内容非法字段*/
	//public static String REGIS_RESULT_PASSWORD_ILLEGAL="902";
	/**确认密码与密码不一致*/
	//public static String REGIS_RESULT_PASSWORD_SAME="903";
	/**输入内容非法字段*/
	//public static String REGIS_RESULT_NAME_ILLEGAL="904";
	/**表单验证失败**/
	public static String REGIS_RESULT_VERIFICATION_FAILURE="905";	
	/**用户名已存在*/
	public static String REGIS_RESULT_NAME_EXIST="906";
	/**未激活用户*/
	public static String REGIS_RESULT_NAME_UNACTIVATION="907";	
	
	/********************访问方式*********************/
	/**页面*/
	public static String WAY_PAGE ="100";
	/**接口*/
	public static String WAY_INTERFACE ="200";
	/**短信*/
	public static String WAY_SMS ="300";
	
	/********************注册类型********************/
	/**手机号注册*/
	public static String REGIS_TYPE_MSISDN="200";
	/**登录名注册*/
	public static String REGIS_TYPE_NAME="300";
	/**一键注册*/
	public static String REGIS_TYPE_A_KEY="400";
	/**外部用户注册*/
	public static String REGIS_TYPE_EXTERNAL_USER="700";
	/**外部新浪微博用户注册*/
	public static String REGIS_TYPE_EXTERNAL_USER_SINA_WEIBO="710";
	/**外部腾讯微博用户注册*/
	public static String REGIS_TYPE_EXTERNAL_USER_TENCENT_WEIBO="720";
	/**外部人人网用户登录*/
	public static String REGIS_TYPE_EXTERNAL_USER_RENREN_WEIBO="730";
	/**腾讯用户*/
	public static String REGIS_TYPE_EXTERNAL_USER_QQ = "750";
	

	/**手机号注册请求*/
	public static String REGIS_ACTION_MSISDN_REQUEST="200";
	/**手机号注册验证*/
	public static String REGIS_ACTION_MSISDN_VALIDATE="300";
	
	/****************************登录模块*******************************/
	/********************登录类型********************/
	/**COOKIE登录*/
	public static String LOGIN_TYPE_AUTO="200";
	/**登录名登录*/
	public static String LOGIN_TYPE_NAME="300";
	/**TGC登录*/
	public static String LOGIN_TYPE_TGC="400";
	/**外部用户登录*/
	public static String LOGIN_TYPE_EXTERNAL_USER="700";
	/**外部新浪微博用户登录*/
	public static String LOGIN_TYPE_EXTERNAL_USER_SINA_WEIBO="710";
	/**外部腾讯微博用户登录*/
	public static String LOGIN_TYPE_EXTERNAL_USER_TQQ_WEIBO="720";
	/**外部人人网用户登录*/
	public static String LOGIN_TYPE_EXTERNAL_USER_RENREN_WEIBO="730";
	/**腾讯用户*/
	public static String LOGIN_TYPE_EXTERNAL_USER_QQ = "750";
	/**登录成功*/
	public static String LOGIN_RESULT_SUCCESS="100";
	
	/**密码错误*/
	public static String LOGIN_RESULT_PASSWORD_ERROR="301";
	/**TGC失效*/
	public static String LOGIN_RESULT_TGC_ERROR ="401";
	/**无效验证信息，即没有TGC\COOKIE\用户名（手机）和密码 验证信息之一*/
	public static String LOGIN_RESULT_ERROR ="901";
	/****************************ST验证*******************************/
	/**ST验证成功*/
	public static String ST_VALIDATE_SUCCESS = "100";
	/**ST验证失败*/
	public static String ST_VALIDATE_ERROR = "200";
	/**ST验证失败,未授权*/
	public static String ST_VALIDATE_UNAUTHORIZED_ERROR = "201";
	/****************************SMS模块*******************************/
	
	/****************************手机号绑定模块*******************************/
	/**成功下发短信【绑定请求】*/
	public static String BING_MSISDN_PUSH_SMS_SUCCESS ="200";
	/**下发短信失败【绑定请求】*/
	public static String BING_MSISDN_PUSH_SMS_FAILURE="201";
	/**已经下发过短信【绑定请求】*/
	public static String BING_MSISDN_PUSH_SMS_EXIST="202";
	/**绑定成功【绑定验证】*/
	public static String BING_MSISDN_SUCCESS = "300";
	/**验证码失效【绑定验证】*/
	public static String BING_MSISDN_CODE_FAIL = "301";
	/**302：验证码错误【绑定验证】*/
	public static String BING_MSISDN_CODE_ERROR = "302";
	/**901：手机号已经被绑定【绑定请求、绑定验证】*/
	public static String BING_MSISDN_EXIST = "901";
	/**902：手机号非法【绑定请求、绑定验证】*/
	public static String BING_MSISDN_ILLEGAL = "902";
	/**903：非法操作【绑定请求、绑定验证】*/
	public static String BING_MSISDN_OPERATE_ILLEGAL = "903";

}
