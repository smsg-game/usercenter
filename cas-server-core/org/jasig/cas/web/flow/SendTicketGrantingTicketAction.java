/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web.flow;

import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.easou.cas.authenticateion.AccessTokenCredentials;
import com.easou.cas.authenticateion.Md5PwdEncoder;
import com.easou.common.util.DESPlus;

/**
 * Action that handles the TicketGrantingTicket creation and destruction. If the
 * action is given a TicketGrantingTicket and one also already exists, the old
 * one is destroyed and replaced with the new one. This action always returns
 * "success".
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public final class SendTicketGrantingTicketAction extends AbstractAction {

	@NotNull
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	private CookieRetrievingCookieGenerator uinfoCookieGenerator;

	// 密码加密工具
	private Md5PwdEncoder pwdEncode;

	private DESPlus des = new DESPlus();

	public void setDes(DESPlus des) {
		this.des = des;
	}

	public Md5PwdEncoder getPwdEncode() {
		return pwdEncode;
	}

	public void setPwdEncode(Md5PwdEncoder pwdEncode) {
		this.pwdEncode = pwdEncode;
	}

	public CookieRetrievingCookieGenerator getUinfoCookieGenerator() {
		return uinfoCookieGenerator;
	}

	public void setUinfoCookieGenerator(
			CookieRetrievingCookieGenerator uinfoCookieGenerator) {
		this.uinfoCookieGenerator = uinfoCookieGenerator;
	}

	/** Instance of CentralAuthenticationService. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	protected Event doExecute(final RequestContext context) {
		final String ticketGrantingTicketId = WebUtils
				.getTicketGrantingTicketId(context);
		final String ticketGrantingTicketValueFromCookie = (String) context
				.getFlowScope().get("ticketGrantingTicketId");

		if (ticketGrantingTicketId == null) {
			return success();
		}

		// 将tgt添加到cookie
		this.ticketGrantingTicketCookieGenerator.addCookie(WebUtils
				.getHttpServletRequest(context), WebUtils
				.getHttpServletResponse(context), ticketGrantingTicketId);

		if (ticketGrantingTicketValueFromCookie != null
				&& !ticketGrantingTicketId
						.equals(ticketGrantingTicketValueFromCookie)) {
			this.centralAuthenticationService
					.destroyTicketGrantingTicket(ticketGrantingTicketValueFromCookie);
		}

		// 添加用户登陆信息
		Credentials cre = (Credentials) context.getFlowScope().get(
				"credentials");
		logger.info("credentials type : " + cre.getClass().getName());
		if (cre != null) {
			// 保存用户信息
			if (cre instanceof UsernamePasswordCredentials) {
				UsernamePasswordCredentials cr = (UsernamePasswordCredentials) cre;
				if ("true".equals(cr.getIsCookie())) {
					uinfoCookieGenerator.addCookie(WebUtils
							.getHttpServletRequest(context), WebUtils
							.getHttpServletResponse(context), des.encrypt(cr
							.getUsername()
							+ "$" + pwdEncode.encode(cr.getPassword())));
				}
			}
			if (cre instanceof AccessTokenCredentials) {
				AccessTokenCredentials cr = (AccessTokenCredentials) cre;
				if (null != cr.getPasswd()) {
					uinfoCookieGenerator.addCookie(WebUtils
							.getHttpServletRequest(context), WebUtils
							.getHttpServletResponse(context), des.encrypt(cr
							.getEasouId()
							+ "$" + cr.getPasswd()));
				}
			}
		}
		//记录登录日志
		//logLoginSuc(context);
		return success();
	}

	public void setTicketGrantingTicketCookieGenerator(
			final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public void setCentralAuthenticationService(
			final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}
	
	
}
