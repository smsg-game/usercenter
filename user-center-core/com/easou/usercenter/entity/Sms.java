package com.easou.usercenter.entity;

import java.util.Date;

import com.easou.common.constant.SMSType;

/**
 * 消息实体
 * 
 * @author damon
 * @since 2012.11.21
 * @version 1.0
 */
public class Sms {
	
	/**短信内容*/
	private String content;
	/**写入日志的短信内容*/
	private String contentLog;
	/**创建时间*/
	private Date createDate;
	/**发送手机号*/
	private String mobile;
	
	private String channel;
	private String esid;
	private SMSType type;
	private String uid;
	
	public Sms(){
		this.createDate = new Date(System.currentTimeMillis());
	}
	
	
	
	public String getContentLog() {
		return contentLog;
	}



	public void setContentLog(String contentLog) {
		this.contentLog = contentLog;
	}



	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	public String getEsid() {
		return esid;
	}



	public void setEsid(String esid) {
		this.esid = esid;
	}



	public SMSType getType() {
		return type;
	}



	public void setType(SMSType type) {
		this.type = type;
	}



	public String getUid() {
		return uid;
	}



	public void setUid(String uid) {
		this.uid = uid;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	/*public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}*/
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
