package com.easou.cas.auth;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;

/**
 * 鉴权提供者
 * 
 * @author damon
 * @since 2012.06.28
 * @version 1.0
 * 
 */
public class EucSimpleAuthHelper implements EucAuthHelper {

	protected static final Log LOG = LogFactory.getLog(EucAuthHelper.class);

	/** 默认配置文件路径 */
	private static String DEFAULT_PROPERTIES_PATH = "/client.properties";
	
	private static String DEFAULT_AUTH_URL_KEY = "user.center.auth.uri";
	/** 默认鉴权地址 */
	private static String DEFAULT_AUTH_URI = "/login";
	
	private static String DEFAULT_VALIDATE_URI_KEY = "user.center.validate.uri";
	/** 默认校验地址 */
	private static String DEFAULT_VALIDATE_URI = "/serviceValidate";

	private static String DEFAULT_LOGOUT_URI_KEY = "user.center.logout.uri";
	/** 默认校验地址 */
	private static String DEFAULT_LOGOUT_URI = "/logout";
	
	private static String DEFAULT_SSO_URL_KEY = "authUrl";
	
	private static String DEFAULT_SSO_URL = "http://sso.fantingame.com";
	
	/** 鉴权URL */
	private String authUrl;
	/** 校验URL */
	private String validateUrl;
	/** 登出*/
	private String logoutUrl;
	/**SSO地址*/
	private String ssoUrl;
	/**
	 * 鉴权对象
	 */
	private EucAuthManager eucAuth;

	/**
	 * 校验对象
	 */
	private EucValidateServiceTicketManager eucValidateServiceTicketManager;
	
	private EucLogoutManager eucLogoutManager;
	
	private static EucAuthHelper instance;
	
	public final static EucAuthHelper getInstance(){
		if(instance==null){
			instance = new EucSimpleAuthHelper();	
		}
		return instance;
	}
	
	private EucSimpleAuthHelper(){
		eucAuth = new EucAuthManager();
		eucValidateServiceTicketManager = new EucValidateServiceTicketManager();
		eucLogoutManager = new EucLogoutManager();
		initProperty(DEFAULT_PROPERTIES_PATH);
	}
	

	/**
	 * 初始化
	 * 
	 * @param propertiesPath
	 *            配置文件路径
	 */
	private void initProperty(String propertiesPath) {
		InputStream is = EucSimpleAuthHelper.class
				.getResourceAsStream(propertiesPath);
		Properties properties = new Properties();
		String authUri = null;
		String validateUri = null;
		String logoutUri = null;
		try {
			properties.load(is);
		    ssoUrl = properties.getProperty(DEFAULT_SSO_URL_KEY);
		    authUri = properties.getProperty(DEFAULT_AUTH_URL_KEY);
		    validateUri = properties.getProperty(DEFAULT_VALIDATE_URI_KEY);
		    logoutUri = properties.getProperty(DEFAULT_LOGOUT_URI_KEY);
		} catch (Exception e) {
			LOG.warn("配置文件client.properties不存在");
		} finally {
			if (ssoUrl == null || "".equals(ssoUrl)) {
				ssoUrl = DEFAULT_SSO_URL;
				LOG.info("init SSO url with default url[" + ssoUrl
						+ "] for user center");
			}
			if (authUri == null || "".equals(authUri)) {
				authUrl = ssoUrl+DEFAULT_AUTH_URI;
				LOG.info("init auth url with default url[" + authUrl
						+ "] for user center");
			}else{
				authUrl = ssoUrl+authUri;
			}
			if (validateUrl == null || "".equals(validateUrl)) {
				validateUrl = ssoUrl+DEFAULT_VALIDATE_URI;
				LOG.info("init validate url with default url[" + validateUrl
						+ "] for user center");
			}else{
				validateUrl = ssoUrl+validateUri;
			}
			if (logoutUrl == null || "".equals(logoutUrl)) {
				logoutUrl = ssoUrl+DEFAULT_LOGOUT_URI;
				LOG.info("init logout url with default url[" + logoutUrl
						+ "] for user center");
			}else{
				logoutUrl = ssoUrl+logoutUri;
			}
		}
	}

	/**
	 * 登录鉴权
	 * 
	 * @param request
	 * @param response
	 * @param auth
	 *            鉴权信息
	 */
	public void auth(HttpServletRequest request, HttpServletResponse response,
			EucAuth auth,RequestInfo info) throws EucParserException {
		auth(request, response, auth, authUrl,info);
	}
	
	/**
	 * 登录鉴权
	 * 
	 * @param request
	 * @param response
	 * @param auth
	 *            鉴权信息
	 * @param authUrl
	 *            鉴权地址
	 */
	public void auth(HttpServletRequest request, HttpServletResponse response,
			EucAuth auth,String authUrl,RequestInfo info) throws EucParserException {
		eucAuth.auth(request, response, auth, authUrl,info);
	}

	/**
	 * 验证Service票据
	 * 
	 * @param serviceTicket
	 * @param secertKey
	 * @param validateUrl
	 * @return
	 */
	public EucApiResult<EucAuthResult> validateServiceTicket(String service,
			String serviceTicket,RequestInfo info) throws EucParserException {
		return eucValidateServiceTicketManager.validateServiceTicket(
				validateUrl, service, serviceTicket,info);
	}
	
	/**
	 * 退出登录
	 * 
	 * @param token
	 * @param service
	 * @throws EucParserException
	 */
	public void logout(HttpServletResponse response,String service,RequestInfo info)throws EucParserException{
		eucLogoutManager.logout(response,logoutUrl,service,info);
	}
	
}
