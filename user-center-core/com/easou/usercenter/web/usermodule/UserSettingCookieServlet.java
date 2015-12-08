package com.easou.usercenter.web.usermodule;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easou.common.util.CookieUtil;
import com.easou.usercenter.util.ConditionUtil;

public class UserSettingCookieServlet extends HttpServlet {

	private static final long serialVersionUID = 5083823947310312667L;

	private static final String QN_TAG = "qn";
	
	private static final String GAMEID_TAG = "gameId";
	
	private static final String APPID_TAG = "appId";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String tgc = req.getParameter(CookieUtil.COOKIE_TGC);
		String u = req.getParameter(CookieUtil.COOKIE_U);
		String username = req.getParameter("USERNAME");
		String eua = req.getParameter("eua");
		String uid = req.getParameter("uid");
		
		if(tgc != null && !"".equals(tgc.trim())) {
			Cookie tgcCookie = new Cookie("EASOUTGC", tgc);
			tgcCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			tgcCookie.setMaxAge(60*60*24*182);
			tgcCookie.setPath("/");
			resp.addCookie(tgcCookie);
		}
		
		if(u != null && !"".equals(u.trim())) {
			Cookie uCookie = new Cookie("U", u);
			uCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			uCookie.setMaxAge(60*60*24*183);
			uCookie.setPath("/");
			resp.addCookie(uCookie);
		}
		
		if(username != null && !"".equals(username.trim())) {
			Cookie usernameCookie = new Cookie("USERNAME", username);
			usernameCookie.setMaxAge(60*60*24*183);
			usernameCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			usernameCookie.setPath("/");
			resp.addCookie(usernameCookie);
		}
		
		if(eua != null && !"".equals(eua.trim())) {
			Cookie euaCookie = new Cookie("eua", eua);
			euaCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			euaCookie.setMaxAge(60*60*24*183);
			euaCookie.setPath("/");
			resp.addCookie(euaCookie);
		}
		
		if(uid != null && !"".equals(uid.trim())) {
			Cookie uidCookie = new Cookie("uid", uid);
			uidCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			uidCookie.setPath("/");
			resp.addCookie(uidCookie);
		}
		StringBuilder paramUrl = new StringBuilder();
		String qn = null;
		if(req.getParameter(QN_TAG)!=null){
			qn = req.getParameter(QN_TAG);
			paramUrl.append("&qn=").append(qn);
		}
		String appId = null;
		if(req.getParameter(GAMEID_TAG)!=null && !"".equals(req.getParameter(GAMEID_TAG))) {
			appId = req.getParameter(GAMEID_TAG);
		}else if(req.getParameter(APPID_TAG)!=null) {
			appId = req.getParameter(APPID_TAG);
		}
		if(appId != null) {
			paramUrl.append("&appId=").append(appId);
		}
		String source = ConditionUtil.getChannel(req);
		if(source != null) {
			paramUrl.append("&source=").append(source);
		}
		if(paramUrl.length() > 0) {
			paramUrl.deleteCharAt(0);
		}
		if(paramUrl.length() > 0) {
			resp.sendRedirect("/user/usersetting.html?"+resp.encodeURL(paramUrl.toString()));
		} else {
			resp.sendRedirect("/user/usersetting.html");
		}
	}
	
}
