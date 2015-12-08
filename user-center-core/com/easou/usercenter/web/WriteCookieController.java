package com.easou.usercenter.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;
import com.easou.common.util.CookieUtil;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;

/**
 * 提供给大厅客户端写入cookie的中转页面
 * 
 * @author jay
 * 
 */
@Controller
public class WriteCookieController extends BaseController {

	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;

	@RequestMapping("/writeCookie")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response)
			throws UnsupportedEncodingException {
		String token = request.getParameter("token");
		String u = request.getParameter("u");
		String src = request.getParameter("src");
		if (null != token) {
			CookieUtil.addCookie(
					ticketGrantingTicketCookieGenerator.getCookieName(), token,
					ticketGrantingTicketCookieGenerator.getCookieDomain(),
					ticketGrantingTicketCookieGenerator.getCookiePath(),
					ticketGrantingTicketCookieGenerator.getCookieMaxAge(),
					response);
		}
		if (null != u) {
			CookieUtil.addCookie(uinfoCookieGenerator.getCookieName(), u,
					uinfoCookieGenerator.getCookieDomain(),
					uinfoCookieGenerator.getCookiePath(),
					uinfoCookieGenerator.getCookieMaxAge(), response);
		}
		if(src!=null) {
			return "redirect:" + src;
		} else {
			return null;
		}
	}
}