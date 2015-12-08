package com.easou.usercenter.web.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class MailConfig {
	
	private static MailConfig ins;

	private static Properties prop;
	private String mailSender;
	private String mailSmtpServer;
	private String mailSmtpUser;
	private String mailSmtpPasswd;
	private String[] mailConfirmTitle;
	private String[] mailConfirmContent;
	private String[] mailBackTitle;
	private String[] mailBackContent;
	private String[] mailConfirmResult;

	public static final String TAG = "#TAG#";
	public static MailConfig instance() {
		synchronized (MailConfig.class) {
			if (ins == null) {
				ins = new MailConfig();
			}
		}

		return ins;
	}

	private MailConfig() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("mail.properties"));
			mailSender = prop.getProperty("mail.sender");
			mailSmtpServer= prop.getProperty("mail.smtp.server");
			mailSmtpUser= prop.getProperty("mail.smtp.user");
			mailSmtpPasswd= prop.getProperty("mail.smtp.passwd");
			mailConfirmTitle = prop.getProperty("mail.confirm.title").split(TAG);
			mailConfirmContent = prop.getProperty("mail.confirm.content").split(TAG);
			mailBackTitle =  prop.getProperty("mail.back.title").split(TAG);
			mailBackContent = prop.getProperty("mail.back.content").split(TAG);
			mailConfirmResult = prop.getProperty("mail.confirm.result").split(TAG);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public String getMailSender() {
		return mailSender;
	}
	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}

	public String getMailSmtpServer() {
		return mailSmtpServer;
	}

	public void setMailSmtpServer(String mailSmtpServer) {
		this.mailSmtpServer = mailSmtpServer;
	}
	public String getMailSmtpUser() {
		return mailSmtpUser;
	}
	public String getMailSmtpPasswd() {
		return mailSmtpPasswd;
	}
	public String getMailConfirmTitle(Languge lan) {
		return mailConfirmTitle[lan.intValue()];
	}
	public String getMailConfirmContent(Languge lan) {
		return mailConfirmContent[lan.intValue()];
	}
	public String getMailBackTitle(Languge lan) {
		return mailBackTitle[lan.intValue()];
	}
	public String getMailBackContent(Languge lan) {
		return mailBackContent[lan.intValue()];
	}
	public String getMailConfirmResult(Languge lan) {
		return mailConfirmResult[lan.intValue()];
	}
}