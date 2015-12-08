/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import com.easou.common.constant.CasConstant;
import com.easou.common.util.StringUtil;

/**
 * Controller to delete ticket granting ticket cookie in order to log out of
 * single sign on. This controller implements the idea of the ESUP Portail's
 * Logout patch to allow for redirecting to a url on logout. It also exposes a
 * log out link to the view via the WebConstants.LOGOUT constant.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class LogoutController extends AbstractController {

	/** The CORE to which we delegate for all CAS functionality. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	/** CookieGenerator for TGT Cookie */
	@NotNull
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	private CookieRetrievingCookieGenerator uinfoCookieGenerator;
	/** 返回结果标识 */
	private final String LOGOUT_RESULT_TAG = "logoutResult";
	/** 返回结果，退出成功 */
	private final int LOGOUT_RESULT_SUCCESS = 100;
	/** 返回结果，退出失败 */
	private final int LOGOUT_RESULT_FAILURE = 200;
	/** 返回结果，退出失败,用户未登录 */
	private final int LOGOUT_RESULT_FAILURE_NOT_LOGIN = 201;

	public CookieRetrievingCookieGenerator getUinfoCookieGenerator() {
		return uinfoCookieGenerator;
	}

	public void setUinfoCookieGenerator(
			CookieRetrievingCookieGenerator uinfoCookieGenerator) {
		this.uinfoCookieGenerator = uinfoCookieGenerator;
	}

	/** CookieGenerator for Warn Cookie */
	@NotNull
	private CookieRetrievingCookieGenerator warnCookieGenerator;

	/** Logout view name. */
	@NotNull
	private String logoutView;

	/**
	 * Boolean to determine if we will redirect to any url provided in the
	 * service request parameter.
	 */
	private boolean followServiceRedirects;

	public LogoutController() {
		setCacheSeconds(0);
	}

	protected ModelAndView handleRequestInternal(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator
				.retrieveCookieValue(request);
		String service = null;

		// 登出成功标识
		int logoutResult = LOGOUT_RESULT_FAILURE;
		if (ticketGrantingTicketId != null) {// 移除登录信息
			//TODO 不能移除ticket，否则所有该用户客户端均会被T下线
//			this.centralAuthenticationService
//					.destroyTicketGrantingTicket(ticketGrantingTicketId);

			this.ticketGrantingTicketCookieGenerator.removeCookie(response);
			this.uinfoCookieGenerator.removeCookie(response);
			this.warnCookieGenerator.removeCookie(response);
			logoutResult = LOGOUT_RESULT_SUCCESS;
		} else {// 用户未登录
			logoutResult = LOGOUT_RESULT_FAILURE_NOT_LOGIN;
		}
		request.getSession().removeAttribute(CasConstant.CONST_CAS_ASSERTION);

		if (request.getParameter("service") != null) {
			service = request.getParameter("service");
		} else if (request.getParameter("back") != null) {
			service = request.getParameter("back");
		} 
		if (this.followServiceRedirects) {
			if (service != null) {
				StringBuffer buffer = new StringBuffer();
				if (service.indexOf("?") != -1) {
					buffer.append(service).append("&")
							.append(LOGOUT_RESULT_TAG).append("=")
							.append(logoutResult);
				} else {
					buffer.append(service).append("?")
							.append(LOGOUT_RESULT_TAG).append("=")
							.append(logoutResult);
				}
				service = buffer.toString();
			} else {
				service = request.getContextPath() + "/";
			}
			// 模板类型
			String wver = request.getParameter("wver");
			if (!StringUtil.isEmpty(wver)) {
				if (service.indexOf("?") != -1) {
					service=service+"&wver="+wver;
				} else {
					service=service+"?wver="+wver;
				}
			}
			return new ModelAndView(new RedirectView(service));
		}
		return new ModelAndView(this.logoutView);
	}

	public void setTicketGrantingTicketCookieGenerator(
			final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public void setWarnCookieGenerator(
			final CookieRetrievingCookieGenerator warnCookieGenerator) {
		this.warnCookieGenerator = warnCookieGenerator;
	}

	/**
	 * @param centralAuthenticationService
	 *            The centralAuthenticationService to set.
	 */
	public void setCentralAuthenticationService(
			final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public void setFollowServiceRedirects(final boolean followServiceRedirects) {
		this.followServiceRedirects = followServiceRedirects;
	}

	public void setLogoutView(final String logoutView) {
		this.logoutView = logoutView;
	}
}
