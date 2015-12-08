/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web.flow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.easou.cas.authenticateion.ThirdPartUserInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.OUserConstant;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;

/**
 * Class to automatically set the paths for the CookieGenerators.
 * <p>
 * Note: This is technically not threadsafe, but because its overriding with a
 * constant value it doesn't matter.
 * <p>
 * Note: As of CAS 3.1, this is a required class that retrieves and exposes the
 * values in the two cookies for subclasses to use.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class InitialFlowSetupAction extends AbstractAction {

	/** CookieGenerator for the Warnings. */
	@NotNull
	private CookieRetrievingCookieGenerator warnCookieGenerator;

	/** CookieGenerator for the TicketGrantingTickets. */
	@NotNull
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	private CookieRetrievingCookieGenerator uinfoCookieGenerator;

	public CookieRetrievingCookieGenerator getUinfoCookieGenerator() {
		return uinfoCookieGenerator;
	}

	public void setUinfoCookieGenerator(
			CookieRetrievingCookieGenerator uinfoCookieGenerator) {
		this.uinfoCookieGenerator = uinfoCookieGenerator;
	}

	/** Extractors for finding the service. */
	@NotNull
	@Size(min = 1)
	private List<ArgumentExtractor> argumentExtractors;

	/** Boolean to note whether we've set the values on the generators or not. */
	private boolean pathPopulated = false;
	private static final Log logger = LogFactory
			.getLog(InitialFlowSetupAction.class);

	protected Event doExecute(final RequestContext context) throws Exception {
		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);

		if (!this.pathPopulated) {
			final String contextPath = context.getExternalContext().getContextPath();
			final String cookiePath = StringUtils.hasText(contextPath) ? contextPath+ "/": "/";
			logger.info("Setting path for cookies to: " + cookiePath);
			this.warnCookieGenerator.setCookiePath(cookiePath);
			this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
			this.pathPopulated = true;
		}

		// 设置登录类型
		putLoginInfoIntoFlow(context, request);
		// 设置日志内容
		putLogInfoIntoFlow(context, request);

		final Service service = WebUtils.getService(this.argumentExtractors,
				context);
		context.getFlowScope().put("requestURI", request.getRequestURI());
		context.getFlowScope().put("service", service);

		//日志输出
		if(logger.isDebugEnabled()){
			//应用服务器信息
			if(service==null){
			    logger.debug("the app service is not exists");
			}else{
				logger.debug("the app service["+service.getId()+"] is exist");
			}
			//APP传递上来的参数
			logger.debug("app service transfer parameters to sso service by URL,ticketGrantingTicketId["
			    		+context.getFlowScope().get("ticketGrantingTicketId")+"],warnCookieValue["
			    		+context.getFlowScope().get("warnCookieValue")+
						"],gateway["+request.getParameter("gateway")+"],renew["+request.getParameter("renew")
					+"],code["+request.getParameter("code")+"]");
		}
		return result("success");
	}
	
	/**
	 * 将登录信息放入flowScope
	 * @param context
	 * @param request
	 * @return
	 */
	private LoginType putLoginInfoIntoFlow(RequestContext context, HttpServletRequest request) {
		// 设置用户cookie中保存的登陆信息
		String uInfo = uinfoCookieGenerator.retrieveCookieValue(request);
		if (uInfo != null) {
			context.getFlowScope().put("uinfo", uInfo);
			if (logger.isDebugEnabled()) {
				logger.debug("发现Cookie登录信息["+uInfo+"]");
			}
		}

		// 设置TGC信息
		String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator
				.retrieveCookieValue(request);
		context.getFlowScope().put("ticketGrantingTicketId",
				ticketGrantingTicketId);
		context.getFlowScope().put("warnCookieValue",
				Boolean.valueOf(this.warnCookieGenerator
						.retrieveCookieValue(request)));
		
		
		LoginType loginType = LoginType.DEFAULT;
		// 根据第三方用户信息判断是否是外部用户登录
		ThirdPartUserInfo tInfo = (ThirdPartUserInfo)request.getAttribute(OUserConstant.THIRDPART_INFO_SESSION);
		if (tInfo != null) {
			context.getFlowScope().put(OUserConstant.THIRDPART_INFO_SESSION, tInfo);
			loginType = LoginType.EXT;
			if(logger.isDebugEnabled()) {
				logger.debug("发现第三方用户信息:" + tInfo.getThirdId() + " accToken:" + tInfo.getAccToken());
			}
		} else if (!StringUtil.isEmpty(ticketGrantingTicketId)) {
			// TGT登录类型，在非第三方登录情况下，优先级要高于COOKIE
			loginType = LoginType.TGC;
		} else if (!StringUtil.isEmpty(uInfo)) {
			loginType = LoginType.AUTO;
		}
		context.getFlowScope().put(Constant.LOGIN_TYPE, loginType);
		return loginType;
	}
	
	private void putLogInfoIntoFlow(RequestContext context, HttpServletRequest request) {
		//调用应用
		String channel = ConditionUtil.getChannel(request);
		context.getFlowScope().put(Constant.CHANNEL_TAG, channel);
		//esid
		String esid = ConditionUtil.getEsid(request);
		context.getFlowScope().put(Constant.ESID_TAG,  esid);
		//UID
		String uid = ConditionUtil.getUid(request);
		context.getFlowScope().put(Constant.UID_TAG, uid);
		//qn
		String qn = ConditionUtil.getQn(request);
		context.getFlowScope().put(Constant.QN_TAG, qn);
		//APP-AGENT
		String appAgent = ConditionUtil.getAppAgent(request);
		context.getFlowScope().put(Constant.APP_AGENT, appAgent);
		// appId
		String appId = ConditionUtil.getGameId(request);
		if(appId==null) {
			// 没有gameId的情况下
			appId = ConditionUtil.getAppId(request);
		}
		context.getFlowScope().put(Constant.APPID_TAG, appId);
	}

	public void setTicketGrantingTicketCookieGenerator(
			final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public void setWarnCookieGenerator(
			final CookieRetrievingCookieGenerator warnCookieGenerator) {
		this.warnCookieGenerator = warnCookieGenerator;
	}

	public void setArgumentExtractors(
			final List<ArgumentExtractor> argumentExtractors) {
		this.argumentExtractors = argumentExtractors;
	}
}
