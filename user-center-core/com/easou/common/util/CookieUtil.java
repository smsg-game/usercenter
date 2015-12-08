package com.easou.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 用户中心-应用COOKIE处理类
 * 
 * @author damon
 * @since 2012.04.28
 *
 */
public class CookieUtil {
	
	public static String COOKIE_TGC = "EASOUTGC";
	
	public static String COOKIE_U = "U";
	
	public static String DEFAULT_DOMAIN = "easou.com";
	
	
	private static final Log LOG = LogFactory.getLog(CookieUtil.class);
	
	/**
	 * 根据cookie名称获取cookie值
	 * 
	 * @param request 请求对象
	 * @param cookieName cookie名称
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request,String cookieName){
		Cookie[] cos = request.getCookies();
		if (cos == null)
			return null;
		for (Cookie co : cos) {
			if (cookieName.equals(co.getName())) {
				if(LOG.isDebugEnabled()){
					LOG.debug("find the cookie["+cookieName+"] is exist,and value["+co.getValue()
							+"],domain["+co.getDomain()+"],path["+co.getPath()+"]");
				}
				return co;
			}
		}
		return null;
	}
	
	/**
	 * 创建一个cookie
	 * 
	 * @param name
	 *            cookie名称
	 * @param value
	 *            值
	 * @param domain
	 *            域
	 * @param domain
	 *            路径
	 * @param maxAge
	 *            失效时间
	 * @return
	 */
	public static void addCookie(String name, String value,String domain,String path,Integer maxAge,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		if(domain!=null){
		    cookie.setDomain(domain);
		}
		if(path!=null){
		    cookie.setPath(path);
		}
		if (maxAge != null){
			cookie.setMaxAge(maxAge);
		}
		LOG.debug("success to add cookie "+name+"["+value+"] to domain["+domain+"]");
		response.addCookie(cookie);
	}
	
	/**
	 * 删除cookie
	 * 
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
		if(getCookie(request,cookieName)!=null){
			if(LOG.isDebugEnabled()){
			    LOG.debug("success to remove cookie "+cookieName+"["+getCookie(request,cookieName)!=null?getCookie(request,cookieName).getValue():null+"]");
			}
			    addCookie(cookieName, "", getAppDomain(request),null, 0, response);
		}
	}
	

	
	/**
	 * 检查应用请求是否在.easou.com域下
	 * 
	 * @param request
	 * @return
	 */
	public static String getAppDomain(HttpServletRequest request) {
		if (request.getServerName().indexOf(CookieUtil.DEFAULT_DOMAIN) != -1) {
			return CookieUtil.DEFAULT_DOMAIN;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("this app service domain[" + request.getServerName()
					+ "] is not in [.easou.com]");
		}
		// 请求地址
		return request.getServerName();
	}
	
}
