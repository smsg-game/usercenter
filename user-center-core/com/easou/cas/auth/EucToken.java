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
public class EucToken implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8328276849391350854L;
	
	public String token;	
	/**COOKIE域*/
	private String domain;
	/**COOKIE路径*/
	private String path;
	/**失效时间*/
	private int age;
	
	public EucToken(String token){
		this.token = token;
	}
	
	public EucToken(){
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	/**
	 * 从COOKIE中获取TOKEN信息
	 * 对于不支持COOKIE的用户，调用此方法会取不到TOKEN
	 * 用户在登录的时候是会把TOKEN信息直接返回给应用的
	 * 应用可以在那个时候自行保存
	 * 
	 * @param request
	 * @return
	 */
	public static EucToken getTokenFromCookie(HttpServletRequest request){
		Cookie cookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
		if(cookie!=null&&null!=cookie.getValue()
				&&!"".equals(cookie.getValue().trim())){
			String tgc = cookie.getValue();
			EucToken token = new EucToken();
			token.setToken(tgc);
			token.setDomain(cookie.getDomain());
			token.setPath(cookie.getPath());
			token.setAge(cookie.getMaxAge());
			return token;
		}
		return null;
	}
}
