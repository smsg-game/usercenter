/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;

import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.Way;
import com.easou.common.util.MD5Util;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;
import com.easou.usercenter.util.LogConstant;

/**
 * Action to authenticate credentials and retrieve a TicketGrantingTicket for
 * those credentials. If there is a request for renew, then it also generates
 * the Service Ticket required.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public class AuthenticationViaFormAction {

	/**
	 * Binder that allows additional binding of form object beyond Spring
	 * defaults.
	 */
	private CredentialsBinder credentialsBinder;

	/** Core we delegate to for handling all ticket related tasks. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	@NotNull
	private CookieGenerator warnCookieGenerator;
	
	@NotNull
	private EucUserService eucUserService;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public final void doBind(final RequestContext context,
			final Credentials credentials) throws Exception {
		final HttpServletRequest request = WebUtils
				.getHttpServletRequest(context);

		if (this.credentialsBinder != null
				&& this.credentialsBinder.supports(credentials.getClass())) {
			this.credentialsBinder.bind(request, credentials);
		}
	}

	public final String submit(final RequestContext context,
			Credentials credentials, final MessageContext messageContext)
			throws Exception {

		Object uInfo = context.getFlowScope().get("uinfo");
		//获取LT
		final String authoritativeLoginTicket = WebUtils
				.getLoginTicketFromFlowScope(context);
		if (uInfo == null) {
			// Validate login ticket
            //从页面请求的LT
			final String providedLoginTicket = WebUtils
					.getLoginTicketFromRequest(context);
			//判断前后LT是否一致
			if (!authoritativeLoginTicket.equals(providedLoginTicket)) {
				this.logger.warn("Invalid login ticket " + providedLoginTicket);
				final String code = "INVALID_TICKET";

				messageContext.addMessage(new MessageBuilder().error().code(
						code).arg(providedLoginTicket).defaultText(code)
						.build());
				return "error";
			}
		} else {
			if (authoritativeLoginTicket != null
					&& !"".equals(authoritativeLoginTicket))
				if (credentials instanceof UsernamePasswordCredentials) {
					((UsernamePasswordCredentials) credentials)
							.setCookieLogin(false);

				}

		}
		final String ticketGrantingTicketId = WebUtils
				.getTicketGrantingTicketId(context);
		final Service service = WebUtils.getService(context);
		//需要强制重新登录
		if (StringUtils.hasText(context.getRequestParameters().get("renew"))
				&& ticketGrantingTicketId != null && service != null) {
			try {
				final String serviceTicketId = this.centralAuthenticationService
						.grantServiceTicket(ticketGrantingTicketId, service,
								credentials);
				WebUtils.putServiceTicketInRequestScope(context,
						serviceTicketId);
				putWarnCookieIfRequestParameterPresent(context);
				return "warn";
			} catch (final TicketException e) {
				if (e.getCause() != null
						&& AuthenticationException.class.isAssignableFrom(e
								.getCause().getClass())) {
					populateErrorsInstance(e, messageContext);
					return "error";
				}
				this.centralAuthenticationService
						.destroyTicketGrantingTicket(ticketGrantingTicketId);
				if (logger.isDebugEnabled()) {
					logger
							.debug(
									"Attempted to generate a ServiceTicket using renew=true with different credentials",
									e);
				}
			}
		}
		try {
			if (credentials instanceof UsernamePasswordCredentials) {
				UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials) credentials;
				try {
					EucUser user = eucUserService.queryUserByIds(userCredentials.getUsername(),
							MD5Util.md5(userCredentials.getPassword()));
					// 如果用户不存在
					if (user != null) {
						userCredentials.setUsername(user.getName());
					}
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}
			WebUtils.putTicketGrantingTicketInRequestScope(context,
					this.centralAuthenticationService
							.createTicketGrantingTicket(credentials));
			putWarnCookieIfRequestParameterPresent(context);

			return "success";
		} catch (final TicketException e) {
			populateErrorsInstance(e, messageContext);
			if (credentials instanceof UsernamePasswordCredentials) {
				UsernamePasswordCredentials cr = (UsernamePasswordCredentials) credentials;
				// 获取登录类型
				LoginType loginType = (LoginType) context.getFlowScope().get(
						Constant.LOGIN_TYPE);
				RequestInfo info = new RequestInfo();
				info.setSource(context.getFlowScope().getString(Constant.CHANNEL_TAG));
				info.setEsid(context.getFlowScope().getString(Constant.ESID_TAG));
				info.setUid(context.getFlowScope().getString(Constant.UID_TAG));
				info.setAgent(context.getFlowScope().getString(Constant.APP_AGENT));
				String st = WebUtils.getServiceTicketFromRequestScope(context);
				// 记录登录失败日志
				BizLogUtil.loginLog(info, "", cr.getUsername(), Way.PAGE,
						LogConstant.RESULT_FAILURE, loginType,st!=null?st:"","",LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", 
						context.getFlowScope().getString(Constant.QN_TAG), context.getFlowScope().getString(Constant.APPID_TAG), null);
			}
			return "error";
		}
	}

	private void populateErrorsInstance(final TicketException e,
			final MessageContext messageContext) {

		try {
			messageContext.addMessage(new MessageBuilder().error().code(
					e.getCode()).defaultText(e.getCode()).build());
		} catch (final Exception fe) {
			logger.error(fe.getMessage(), fe);
		}
	}

	private void putWarnCookieIfRequestParameterPresent(
			final RequestContext context) {
		final HttpServletResponse response = WebUtils
				.getHttpServletResponse(context);

		if (StringUtils.hasText(context.getExternalContext()
				.getRequestParameterMap().get("warn"))) {
			this.warnCookieGenerator.addCookie(response, "true");
		} else {
			this.warnCookieGenerator.removeCookie(response);
		}
	}

	public final void setCentralAuthenticationService(
			final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	/**
	 * Set a CredentialsBinder for additional binding of the HttpServletRequest
	 * to the Credentials instance, beyond our default binding of the
	 * Credentials as a Form Object in Spring WebMVC parlance. By the time we
	 * invoke this CredentialsBinder, we have already engaged in default binding
	 * such that for each HttpServletRequest parameter, if there was a JavaBean
	 * property of the Credentials implementation of the same name, we have set
	 * that property to be the value of the corresponding request parameter.
	 * This CredentialsBinder plugin point exists to allow consideration of
	 * things other than HttpServletRequest parameters in populating the
	 * Credentials (or more sophisticated consideration of the
	 * HttpServletRequest parameters).
	 * 
	 * @param credentialsBinder
	 *            the credentials binder to set.
	 */
	public final void setCredentialsBinder(
			final CredentialsBinder credentialsBinder) {
		this.credentialsBinder = credentialsBinder;
	}

	public final void setWarnCookieGenerator(
			final CookieGenerator warnCookieGenerator) {
		this.warnCookieGenerator = warnCookieGenerator;
	}
	
	public void setEucUserService(EucUserService eucUserService) {
		this.eucUserService = eucUserService;
	}
	
}
