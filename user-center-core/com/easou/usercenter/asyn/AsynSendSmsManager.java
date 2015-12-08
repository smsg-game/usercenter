package com.easou.usercenter.asyn;

import com.easou.usercenter.entity.Sms;

/**
 * 异步发送短信
 * 
 * @author damon
 *
 */
public interface AsynSendSmsManager {
	
	/**
	 * 发送短信
	 * 
	 * @param sms
	 * @return
	 */
	public void asynSendSms(Sms sms);
	
	/**
	 * 发送短信
	 */
	public void synSendSms();

}
