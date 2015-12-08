/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web.flow;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.easou.cas.authenticateion.ThirdPartUserInfo;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.OUserConstant;
import com.easou.common.constant.Way;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.Constant;
import com.easou.usercenter.util.LogConstant;

/**
 * Action to generate a service ticket for a given Ticket Granting Ticket and
 * Service.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public final class GenerateServiceTicketAction extends AbstractAction {

	private static Logger LOG = Logger
			.getLogger(GenerateServiceTicketAction.class);
	/** Instance of CentralAuthenticationService. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}

	TicketRegistry ticketRegistry;

	protected Event doExecute(final RequestContext context) {
		final Service service = WebUtils.getService(context);
		final String ticketGrantingTicket = WebUtils
				.getTicketGrantingTicketId(context);
		RequestInfo info = new RequestInfo();
		info.setSource(context.getFlowScope().getString(Constant.CHANNEL_TAG));
		info.setEsid(context.getFlowScope().getString(Constant.ESID_TAG));
		info.setUid(context.getFlowScope().getString(Constant.UID_TAG));
		info.setAgent( context.getFlowScope().getString(Constant.APP_AGENT));

		try {
			final String serviceTicketId = this.centralAuthenticationService
					.grantServiceTicket(ticketGrantingTicket, service);
			if (logger.isDebugEnabled()) {
				this.logger.debug("Generated app service ticket(ST)["
						+ serviceTicketId + "] success by TGT["
						+ ticketGrantingTicket + "] ");
			}
			WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
           
			// TODO 登录成功,记录日志
            logLoginSuc(info, context, service, ticketGrantingTicket, serviceTicketId);
			return success();
		} catch (final TicketException e) {
			// 获取登录类型
			LoginType loginType = (LoginType) context.getFlowScope().get(
					Constant.LOGIN_TYPE);
			// 记录登录异常日志
			BizLogUtil.loginLog(info, "", "", Way.PAGE,
					LogConstant.RESULT_FAILURE, loginType, "",
					ticketGrantingTicket,LogConstant.LOGIN_RESULT_TGC_ERROR, "", 
					context.getFlowScope().getString(Constant.QN_TAG), context.getFlowScope().getString(Constant.APPID_TAG), null);
			if (logger.isDebugEnabled()) {
				this.logger
						.debug("Generated app service ticket(ST) fail by TGT["
								+ ticketGrantingTicket + "] ,and gateway["
								+ isGatewayPresent(context) + "]");
			}
			if (isGatewayPresent(context)) {
				return result("gateway");
			}
		}
		return error();
	}

	public void setCentralAuthenticationService(
			final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	protected boolean isGatewayPresent(final RequestContext context) {
		return StringUtils.hasText(context.getExternalContext()
				.getRequestParameterMap().get("gateway"));
	}

	/**
	 * 记录用户登录成功日志
	 * 
	 * @param context
	 */
	public void logLoginSuc(RequestInfo info, RequestContext context, Service service,
			String ticketGrantingTicket, String serviceTicketId) {
		Credentials cre = (Credentials) context.getFlowScope().get(
				"credentials");
		String userName = "";
		// 获取登录类型
		LoginType loginType = (LoginType) context.getFlowScope().get(
				Constant.LOGIN_TYPE);
		Ticket ticket = ticketRegistry.getTicket(ticketGrantingTicket);
		if (ticket != null && ticket instanceof TicketGrantingTicket) {
			Authentication authentication = ((TicketGrantingTicket) ticket)
					.getAuthentication();
			Principal principal = authentication.getPrincipal();
			if (LOG.isDebugEnabled()) {
				LOG.debug("authentication is success by TGT["
						+ ticketGrantingTicket + "],user'id["
						+ principal.getId() + "]");
			}
			if (LoginType.DEFAULT == loginType) {// 默认用户名密码登录
				if (cre instanceof UsernamePasswordCredentials) {
					UsernamePasswordCredentials cr = (UsernamePasswordCredentials) cre;
					userName = cr.getUsername();
				}
			} else if (LoginType.AUTO == loginType) {// cookie登录
				if (cre instanceof UsernamePasswordCredentials) {
					UsernamePasswordCredentials cr = (UsernamePasswordCredentials) cre;
					userName = cr.getUsername();
				}
			}
			// 获得用户注册时间
			Date regTime = (Date)principal.getAttributes().get("regTime");
			String extType = "";
			if(LoginType.EXT == loginType) {
				// 如果是外部登录，检查外部登录类型
				ThirdPartUserInfo tinfo = (ThirdPartUserInfo)context.getFlowScope().get(OUserConstant.THIRDPART_INFO_SESSION);
				if(tinfo!=null) {
					extType = BizLogUtil.getExtLogLoginType(tinfo.getType());
					regTime = (Date)tinfo.getRegistTime();
				}
			}
			// 记录登录成功日志
			BizLogUtil.loginLog(info, principal.getId(), userName, Way.PAGE,
					LogConstant.RESULT_SUCCESS, loginType, serviceTicketId,
					ticketGrantingTicket,LogConstant.LOGIN_RESULT_SUCCESS, extType,
					context.getFlowScope().getString(Constant.QN_TAG), context.getFlowScope().getString(Constant.APPID_TAG), regTime);
			Map<String,Object> map = authentication.getPrincipal().getAttributes();
		    if(map!=null){
		    	Object isRegist = map.get("isRegist");
		    	if(isRegist!=null&&Boolean.valueOf(isRegist.toString())){//判断是否是通过手机下发短信的形式首次登录的用户
		    		Long eaId = null;
		    		try {
		    			eaId = new Long(principal.getId());
		    		} catch(Exception e) {
		    			// TODO principal中的id可能不是long型
		    			//	暂不记录日志
		    		}
		    		//写注册成功日志
		    		BizLogUtil.registLog(info,userName, eaId, Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS, "",
							LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, context.getFlowScope().getString(Constant.QN_TAG), context.getFlowScope().getString(Constant.APPID_TAG));
		    	}
		    }
		}
	}
}
