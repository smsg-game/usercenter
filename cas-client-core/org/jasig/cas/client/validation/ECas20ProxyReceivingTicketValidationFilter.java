package org.jasig.cas.client.validation;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ECas20ProxyReceivingTicketValidationFilter extends
		Cas20ProxyReceivingTicketValidationFilter {
	private static final Log log = LogFactory
			.getLog(ECas20ProxyReceivingTicketValidationFilter.class);

	//CookieUtil cookie = null;

	protected void initInternal(final FilterConfig filterConfig)
			throws ServletException {
		super.initInternal(filterConfig);
		String cookieDomain = filterConfig.getInitParameter("cookieDomain");
		//if (cookieDomain == null)
		//	cookieDomain = SSOCookie.CookieUtil;
		//cookie = new CookieUtil(cookieDomain);
	}

	protected void onSuccessfulValidation(final HttpServletRequest request,
			final HttpServletResponse response, final Assertion assertion) {
//		try {
//			// 获取esid
//			String esid = EsidUtil.getEsidFromRequest(request);
//
//			Assertion ass = (Assertion) request.getSession().getAttribute(
//					CONST_CAS_ASSERTION);
//			if (log.isDebugEnabled()) {
//				log.debug("assattri: " + ass.getPrincipal().getAttributes());
//			}
//			// 创建登陆esid
//			String longinEsid = SessionId.createLogonId(Long.parseLong(ass
//					.getPrincipal().getName()), esid, "123456789");
//			if (log.isDebugEnabled()) {
//				log.debug("登陆session为: " + longinEsid);
//			}
//			request.setAttribute(MemSessionFilter.SESSION_ID_NAME, longinEsid);
//
//			HttpSession session = request.getSession();
//
//			// 将现有esid中的属性保存到登录后的esid
//			sessionChange(session, longinEsid);
//
//			// 添加已登陆标识
//			//cookie.addStatCookie(response);
//			//String a = (String) ass.getPrincipal().getAttributes().get(
//			//		"acookieAge");
//			// 添加自动登陆标识
//			//if (a != null) {
//			//	cookie.addACookie(Integer.valueOf(a), response);
//			//}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}

//	private void sessionChange(HttpSession session, String loginEsid)
//			throws Exception {
//		String[] keys = session.getValueNames();
//		WapSession newSession = new WapSession(loginEsid);
//		for (int i = 0; i < keys.length; i++) {
//			newSession.setAttribute(keys[i], session.getAttribute(keys[i]));
//		}
//		newSession.saveSession();
//	}

}
