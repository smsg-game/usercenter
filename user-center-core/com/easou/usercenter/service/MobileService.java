package com.easou.usercenter.service;

public interface MobileService {
	
	public final static String SUCCESS = "success";
	public final static String SMS_ERROR = "sms_error";	// 短信发送错误
	public final static String REG_REPEAT_ERR = "reg_repeat_err";	// 重复注册错误
	public final static String CACHE_ERROR = "cache_error";	// 缓存写入错误

	public String getVeriCodeByMobile(String mobile);
	public String setVeriCodeByMobile(String mobile);
	/**
	 * 获取邮件绑定验证码
	 * @param email
	 * @return
	 */
	public String getEmailByVeriCode(String code);
	/**
	 * 设置邮件绑定验证码
	 * @param email
	 * @return
	 */
	public boolean setVeriCodeByEmail(String code,String email,String userId,String lan);
	
	public boolean delVeriCodeByMobile(String mobile);
	public String registerMobile(String mobile, String pass);
	public String getRegisterMobile(String mobile);
	public boolean delRegisterMobile(String mobile);
}
