package com.easou.usercenter.asyn.helper;

import com.easou.common.constant.SMSType;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.entity.Sms;
import com.easou.usercenter.util.SendMessageUtil;

/**
 * 发送短信处理类
 * 
 * @author damon
 * @since 2012.11.21
 * @version 1.0
 */
public class SendSmsHelper {

	/**
	 * 是否是异步发送
	 */
	private static boolean isAsynSend = SendMessageUtil.isAsynSend();

	//private static AsynSendSmsManager asynSendSmsManager = new AsynSendSmsManagerImpl();

	public static void sendSms(String mobile, String message) {
		sendSms(null, mobile, message,null,null, null, null);
	}

	public static void sendSms(String uid, String mobile, String message,
			String messageLog,String channel, String esid, SMSType type) {
		if(isAsynSend){
			asynSendSms(uid, mobile, message,messageLog,channel,esid,type);
		}else{
			SendMessageUtil.send(uid, mobile, message,messageLog, channel, esid, type);
		}
	}
	
	public static void sendNewSms(String uid, String mobile, String message,
			String messageLog,String channel, String esid, SMSType type){
		SendMessageUtil.sendNewMsg(uid, mobile, message, messageLog, channel, esid, type);
	}

	/**
	 * 异步发送短信
	 * 
	 * @param uid
	 * @param mobile
	 * @param message
	 * @param channel
	 * @param esid
	 * @param type
	 */
	private static void asynSendSms(String uid, String mobile, String message,
			String messageLog,String channel,String esid, SMSType type) {
		Sms sms = new Sms();
		sms.setUid(uid);
		sms.setMobile(mobile);
		sms.setContent(message);
		sms.setContentLog(messageLog);
		sms.setChannel(channel);
		sms.setEsid(esid);
		sms.setType(type);
		ServiceFactory.getInstance().getAsynSendSmsManager().asynSendSms(sms);
	}

}
