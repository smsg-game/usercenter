package com.easou.usercenter.util;

import com.easou.usercenter.util.mail.MailSenderInfo;
import com.easou.usercenter.util.mail.SimpleMailSender;
import com.easou.usercenter.web.utils.MailConfig;

public class SendMailUtil {
	
//	private final static String SMTP_USERNAME = "alert@fantingame.com";
//	private final static String SMTP_PASS = "ftft123456";
//	private final static String FORM_ADDRESS = "alert@fantingame.com";
//	private final static String HOST = "smtp.exmail.qq.com";

	public static void send(String address, String subject, String content) {
		MailSenderInfo mailInfo = new MailSenderInfo();    
		MailConfig config = MailConfig.instance();
	     mailInfo.setMailServerHost(config.getMailSmtpServer());    
	     mailInfo.setMailServerPort("25");
	     mailInfo.setValidate(true);    
	     mailInfo.setUserName(config.getMailSmtpUser());    
	     mailInfo.setPassword(config.getMailSmtpPasswd());	//您的邮箱密码
	     mailInfo.setFromAddress(config.getMailSender());
	     mailInfo.setToAddress(address);
	     mailInfo.setSubject(subject);
	     mailInfo.setContent(content);
	     SimpleMailSender.sendHtmlMail(mailInfo);
	}
	
	public static void main(String[] args) {
		send("252493618@qq.com","梵町用户","test");
	}
}