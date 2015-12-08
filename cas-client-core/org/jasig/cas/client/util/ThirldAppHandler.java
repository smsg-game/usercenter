package org.jasig.cas.client.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.validation.Assertion;

/**
 * 用户中心-第三方应用接入辅助类
 * 
 * @author damon
 * @since 2012.04.28
 * 
 */
public class ThirldAppHandler {

	protected static final Log log = LogFactory.getLog(ThirldAppHandler.class);

	// 梵町域名
	private static String DOMAIN_DEFAULT = ".easou.com";

	/**
	 * 移除第三方应用登录cookie标识
	 * 
	 * @param request
	 * @param response
	 */
	public static void removeThirldLoginTag(HttpServletRequest request,
			HttpServletResponse response) {

		if (checkThirldAppDomain(request)) {
			// 删除已登录标识
			CookieUtil.removeCookie(request, response,
					CookieUtil.ALREADY_LOGIN_TAG);
			CookieUtil.removeCookie(request, response,
					CookieUtil.AUTO_LOGIN_TAG);
		}

	}

	/**
	 * 添加第三方应用登录cookie标识
	 * 
	 * @param request
	 * @param response
	 * @param assertion
	 * 
	 */
	private static void addThirldLoginTag(HttpServletRequest request,
			HttpServletResponse response, Assertion assertion,
			String cookieValue) {

		if (checkThirldAppDomain(request)) {

//			CookieUtil.addCookie(CookieUtil.ALREADY_LOGIN_TAG, cookieValue,
//					request.getServerName(), "/", null, response);
//			if (log.isDebugEnabled()) {
//				log.debug("success to add cookie "
//						+ CookieUtil.ALREADY_LOGIN_TAG + "[" + cookieValue
//						+ "] to app service[" + request.getServerName() + "]");
//			}
//			if (assertion != null) {
//				String acookieAge = (String) assertion.getPrincipal()
//						.getAttributes().get("acookieAge");
//				if (acookieAge != null) {
//					Integer a = Integer.valueOf(acookieAge);
//					// 自动登录标识
//					CookieUtil.addCookie(CookieUtil.AUTO_LOGIN_TAG,
//							cookieValue, request.getServerName(), "/",
//							a < 24 * 60 * 60 ? 0 : a - 24 * 60 * 60, response);
//					if (log.isDebugEnabled()) {
//						log.debug("success to add cookie "
//								+ CookieUtil.AUTO_LOGIN_TAG + "[" + cookieValue
//								+ "] to app service[" + request.getServerName()
//								+ "]");
//					}
//				}
//			}
		}
	}

	/**
	 * 添加第三方应用已登录cookie标识
	 * 
	 * @param request
	 * @param response
	 * @param assertion
	 * 
	 */
//	public static void addThirldSuccLoginTag(HttpServletRequest request,
//			HttpServletResponse response, Assertion assertion) {
//		log.debug("this is thirld app,to update user'static is logined");
//		addThirldLoginTag(request, response, assertion,
//				CookieUtil.LOGIN_STAT_VALUE);
//
//	}

	/**
	 * 添加第三方应用未登录cookie标识
	 * 
	 * @param request
	 * @param response
	 * @param assertion
	 * 
	 */
	public static void addThirldUnLoginTag(HttpServletRequest request,
			HttpServletResponse response, Assertion assertion) {
		log.debug("this is thirld app,to update user'static is unlogin");
		addThirldLoginTag(request, response, assertion,
				CookieUtil.UNLOGIN_STAT_VALUE);
//		CookieUtil.removeCookie(request, response, CookieUtil.AUTO_LOGIN_TAG);

	}

	/**
	 * 检查应用请求是否在.easou.com域下
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkThirldAppDomain(HttpServletRequest request) {
		if (request.getServerName().indexOf(DOMAIN_DEFAULT) != -1) {
			return false;
		}
		if (log.isDebugEnabled()) {
			log.debug("this app service domain[" + request.getServerName()
					+ "] is not in [.easou.com]");
		}
		// 请求地址
		return true;
	}

	/**
	 * 判断第三方应用是否需要到SSO请求验证
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isRedirectToCasServer(HttpServletRequest request) {
		// 自动登录标识
		Cookie autoLogin = CookieUtil.getCookie(request,
				CookieUtil.AUTO_LOGIN_TAG);
		// 已登录标识
		Cookie alreadyLogin = CookieUtil.getCookie(request,
				CookieUtil.ALREADY_LOGIN_TAG);
		// 登录标识为空(表明为第一次请求服务），且为第三方应用
		if (autoLogin == null && alreadyLogin == null
				&& ThirldAppHandler.checkThirldAppDomain(request)) {
			return true;
		}
		return false;
	}

}
