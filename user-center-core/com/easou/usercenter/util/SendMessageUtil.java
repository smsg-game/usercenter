package com.easou.usercenter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.easou.common.api.EucHttpClient;
import com.easou.common.constant.SMSType;
import com.easou.common.json.JsonUtil;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.asyn.AsynSendSmsManager;
import com.easou.usercenter.asyn.impl.AsynSendSmsManagerImpl;
import com.easou.usercenter.entity.Sms;
public class SendMessageUtil {
	private static final String CONFIG_FILE_NAME = "smsContent.properties";
	private static final Logger log = Logger.getLogger(SendMessageUtil.class);
	//private static Properties prop;
	
	private static boolean enable;
	private static String baseUrl;
	private static boolean isAsynSend;
	//是否使用链接池
	private static boolean isUserPool;
	
	static{
		Properties prop = StringUtil.getProperties(CONFIG_FILE_NAME);
		if(prop.get("enable")!=null
				&&"true".equals(prop.get("enable").toString().trim())){
			enable = true;
		}else{
			enable = false;
		}
		baseUrl = prop.get("url").toString().trim();
		//是否异步发送
		if(prop.get("isAsynSend")!=null
				&&"false".equals(prop.get("isAsynSend").toString().trim())){
			isAsynSend = false;
		}else{
			isAsynSend = true;
		}
		if(prop.get("isUserPool")!=null
				&&"false".equals(prop.get("isUserPool").toString().trim())){
			isUserPool = false;
		}else{
			isUserPool = true;
		}
	}
	
	public static boolean isAsynSend() {
		return isAsynSend;
	}

	
	

	/**
	 * @descreption send the given message to the given mobile phone.<br>
	 *              This method is based on the <code>url</code> property <br>
	 *              of file <code>smsContent.propertis</code> which is used as
	 *              the base http url
	 * @param mobile
	 *            {@link String} the mobile phone number.
	 * @param message
	 *            {@link String} the message caller want to send.
	 * @return {@link Boolean} <code>true</code> if send successed,or
	 *         <code>false</code>.
	 */
	public static boolean send(String mobile, String message) {
		return send(null,mobile,message,null,null,null,null);
	}
	
	public static boolean send(String uid,String mobile, String message,String messageLog,String channel,String esid,SMSType type) {
		boolean result = true;
		/*if (null == prop) {
			prop = StringUtil.getProperties(CONFIG_FILE_NAME);
		}
		String enable = prop.getProperty("enable");*/
		long start = System.currentTimeMillis();
		if (enable) {
			try {
				  String url="";
				 if(baseUrl.indexOf("td.easou.com")!=-1){
					 url = baseUrl + mobile + "&content="
						+ URLEncoder.encode(message, "UTF-8");
				 }else{
					 url = baseUrl + mobile + "&Text="
						+ URLEncoder.encode(message+"【梵町搜索】", "UTF-8");
				 }
				String json = null;
				if(isUserPool){//使用链接池
					json = EucHttpClient.httpGet(url, null);
				}else{
					json = request(url);
				}
				if(url.indexOf("td.easou.com")!=-1){
				String resultCode = "100";
				MessageBean bean = parse(json);
				if (!"1000".equals(bean.getSubmitStatus()) || !"1000".equals(bean.getProcessStatus())) {
					log.error("Send fail: mobile=" + mobile + " error json" + json);
					result = false;
					resultCode = "200";
				}
				BizLogUtil.smsLog(uid,mobile, messageLog, bean.getSubmitStatus(),channel,esid,resultCode,type,(System.currentTimeMillis()-start));
			  }
			} catch (UnsupportedEncodingException e) {
				result = false;
				BizLogUtil.smsLog(uid,mobile, messageLog, "901",channel,esid,"200",type,(System.currentTimeMillis()-start));
				e.printStackTrace();
			}
		}
		if (log.isInfoEnabled()) {
			log.info("mobile: " + mobile + "[" + enable + "]" + message + " time: " + (System.currentTimeMillis() - start) + "ms");
		}
		return result;
	}
	
	private static MessageBean parse(String json) {
		MessageBean bean = JsonUtil.parserStrToObject(json, MessageBean.class);
		return bean;
	}
	

	private static String request(String url) {
		URL urlStr = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		int state = -1;
		if (url == null || url.trim().length() <= 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			urlStr = new URL(url);
			connection = (HttpURLConnection) urlStr.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestMethod("GET");
			state = connection.getResponseCode();
			if (state == 200) {
				in = new BufferedReader(new InputStreamReader(connection
						.getInputStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					sb.append(line.trim());
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			if (null != connection)
				connection.disconnect();
		}
		return sb.toString();
	}
	
	public static String MD5(String encryptContent) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(encryptContent.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			result = buf.toString().toUpperCase();

		} catch (NoSuchAlgorithmException e) {

		}
		return result;
	}
	private static final String MESSAGE_1 = "【梵町游戏】您于%s申请了手机绑定。绑定验证码为:%s，请您尽快完成绑定步骤。如非本人操作，请忽略。";
	private static final String MESSAGE_2 = "【梵町游戏】您于%s申请了密码找回。您的新密码为:%s，为了保证您账号的安全，请您尽快修改密码";	
	
	private static final String userName="fdyx";
	private static final String passwd="ab8888";
	// 密码采用md5加密
	public static String createSubmitXml(String mobileNum,String code,SMSType type) {
		Date now = new Date();
		// 2008-6-16 20:54:53
		DateFormat dtFormat = DateFormat.getDateInstance();
		String sendtime = dtFormat.format(now);
		String content = "";
		DateFormat format1 = new java.text.SimpleDateFormat("yyyy年MM月dd日");
		String contentTime = format1.format(now);
        if(type==SMSType.BIND){
        	content = String.format(MESSAGE_1,contentTime,code);
        }else{
        	content = String.format(MESSAGE_2,contentTime,code);
        }
		StringBuffer sp = new StringBuffer();
		sp.append(String
				.format("<Group Login_Name=\"%s\" Login_Pwd=\"%s\" InterFaceID=\"0\" OpKind=\"0\">",
						userName, MD5(passwd)));
		sp.append(String.format("<E_Time>%s</E_Time>", sendtime));
		sp.append("<Item>");
		sp.append("<Task>");
		sp.append("<Recive_Phone_Number>"+mobileNum+"</Recive_Phone_Number>");
		sp.append("<Content><![CDATA["+content+"]]></Content>");
		sp.append("<Search_ID>1</Search_ID>");
		sp.append("</Task>");
		sp.append("</Item>");
		sp.append("</Group>");
		return sp.toString();
	}
	public static boolean sendNewMsg(String uid,String mobile, String message,String messageLog,String channel,String esid,SMSType type){
		long start = System.currentTimeMillis();
		String resultCode = "-1";
		try {
			String uri = "http://userinterface.vcomcn.com/Opration.aspx";
			// 提交的数据
			String pstContent = createSubmitXml(mobile,message,type);
			System.out.println(pstContent);
			resultCode = Http.post(uri, pstContent);
			BizLogUtil.smsLog(uid,mobile, messageLog, "123",channel,esid,resultCode,type,(System.currentTimeMillis()-start));
			System.out.print("\nexec end!");
		} catch (Exception e) {
			resultCode="-2";
			BizLogUtil.smsLog(uid,mobile, messageLog, "901",channel,esid,resultCode,type,(System.currentTimeMillis()-start));
			e.printStackTrace();
		}
		if(resultCode.equals("00")){
			return true;
		}else{
			return false;
		}
	}
	public static void main(String[] args) {
//		send("13543289769","这是一个通道测试短信,如果您接收到该短信,请忽略之");
		sendNewMsg("", "15919820372", "xxxx", "messageLog", "channel", "esid", SMSType.FORGET_PASS);
	}
}
