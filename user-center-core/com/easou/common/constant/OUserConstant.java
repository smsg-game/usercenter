package com.easou.common.constant;

import java.util.HashMap;

public class OUserConstant {
	
	public static HashMap<String, String> ohm = new HashMap<String, String>();
	
	public final static String THIRDPART_INFO_SESSION = "tinfo";
	
	public final static String ENCRYPT_SERVICE_TAG = "enService";

	public final static String SINA_TYPE = "1";
	
	public final static String TQQ_TYPE = "2";
	
	public final static String RENREN_TYPE = "3";
	
	public final static String QQ_TYPE = "5";
	
	public final static String SINA_TYPE_WAP1 = "11";
	
	public final static String TQQ_TYPE_WAP1="21";
	
	public final static String RENREN_TYPE_WAP1="31";
		
	public final static String QQ_TYPE_WAP1="51";
	
	static {
		ohm.put(SINA_TYPE, "新浪微博");
		ohm.put(TQQ_TYPE, "腾讯微博");
		ohm.put(RENREN_TYPE, "人人网");
		ohm.put(QQ_TYPE, "腾讯QQ");
		ohm.put(SINA_TYPE_WAP1, "新浪微博");
		ohm.put(TQQ_TYPE_WAP1, "腾讯微博");
		ohm.put(RENREN_TYPE_WAP1, "人人网");
		ohm.put(QQ_TYPE_WAP1, "腾讯QQ");
	}

	public static String getThirdName(String netId) {
		return ohm.get(netId);
	}
	
	/** 应用认证授权的URL*/
	public static final String RENREN_OAUTH_AUTHORIZE_URL  = "renren.oauthAuthorizeURL";
	/** 应用获取Access_token的URL*/
	public static final String RENREN_OAUTH_ACCESS_TOKEN_URL = "renren.accessTokenURL";
	/** 应用获取人人网session key的URL*/
	public static final String RENREN_API_SESSIONKEY_URL = "renren.apiSessionKeyURL";
	/**从人人网重定向到用户中心的URL*/
	public static final String RENREN_REDIRECT_URI = "renren.redirect_URI";
	/**向人人网发送鉴权信息的client ID*/
	public static final String RENREN_CLIENT_ID = "renren.client_ID";
	/**向人人网发送鉴权信息的client SERCRET*/
	public static final String RENREN_CLIENT_SERCRET = "renren.client_SERCRET";
	
	/** 应用认证授权的URL*/
	public static final String QQ_OAUTH_AUTHORIZE_URL  = "qq.oauthAuthorizeURL";
	/** 应用获取Access_token的URL*/
	public static final String QQ_OAUTH_ACCESS_TOKEN_URL = "qq.accessTokenURL";
	/** 应用获取人人网session key的URL*/
	public static final String QQ_API_SESSIONKEY_URL = "qq.apiSessionKeyURL";
	/**从QQ网重定向到用户中心的URL*/
	public static final String QQ_REDIRECT_URI = "qq.redirect_URI";
	/**向QQ网发送鉴权信息的client ID*/
	public static final String QQ_CLIENT_ID = "qq.client_ID";
	/**向QQ网发送鉴权信息的client SERCRET*/
	public static final String QQ_CLIENT_SERCRET = "qq.client_SERCRET";
	
	/** 应用认证授权的URL*/
	public static final String TQQ_OAUTH_AUTHORIZE_URL  = "tqq.oauthAuthorizeURL";
	/** 应用获取Access_token的URL*/
	public static final String TQQ_OAUTH_ACCESS_TOKEN_URL = "tqq.accessTokenURL";
	/**从人人网重定向到用户中心的URL*/
	public static final String TQQ_REDIRECT_URI = "tqq.redirect_URI";
	/**向人人网发送鉴权信息的client ID*/
	public static final String TQQ_CLIENT_ID = "tqq.client_ID";
	/**向人人网发送鉴权信息的client SERCRET*/
	public static final String TQQ_CLIENT_SERCRET = "tqq.client_SERCRET";
	/**获取QQ信息的接口服务器*/
	public static final String TQQ_API_SERVER = "tqq.apiServer";
	
	/**网易微博受权KEY**/
	public static final String NEASE_OAUTH_CONSUMERKEY = "tblog4j.oauth.consumerKey";
	/**网易微博受权密钥**/
	public static final String NEASE_OAUTH_CONSUMERSERCERT = "tblog4j.oauth.consumerSecret";
	/**网易微博重定向地址**/
	public static final String NEASE_REDIRECTURI = "tblog4j.redirectURI";
}
