package com.easou.cas.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.easou.common.util.CookieUtil;

/**
 * 票据对象
 * 
 * @author damon
 * @since 2012.07.03
 * @version 1.0
 *
 */
public class EucUCookie implements java.io.Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -3460097573745876956L;
	
	/**值*/
	private String u;
	/**COOKIE域*/
	private String domain;
	/**COOKIE路径*/
	private String path;
	/**失效时间*/
	private int age;
	
	/**
	 * 从COOKIE中获取U信息
	 * 对于不支持COOKIE的用户，调用此方法会取不到U
	 * 用户在登录的时候是会把U信息直接返回给应用的
	 * 应用可以在那个时候自行保存
	 * 
	 * @param request
	 * @return
	 */
	public static EucUCookie getUFromCookie(HttpServletRequest request){
		Cookie cookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_U);
		if(cookie!=null&&null!=cookie.getValue()
				&&!"".equals(cookie.getValue().trim())){
			String uValue = cookie.getValue();
			EucUCookie u = new EucUCookie();
			u.setU(uValue);
			u.setDomain(cookie.getDomain());
			u.setPath(cookie.getPath());
			u.setAge(cookie.getMaxAge());
			return u;
		}
		return null;
	}
	
	
	
	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
