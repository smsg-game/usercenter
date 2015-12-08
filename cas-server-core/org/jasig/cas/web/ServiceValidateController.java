/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web;

import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.HttpBasedServiceCredentials;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketValidationException;
import org.jasig.cas.ticket.proxy.ProxyHandler;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.Cas20ProtocolValidationSpecification;
import org.jasig.cas.validation.ValidationSpecification;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easou.cas.auth.EucToken;
import com.easou.common.api.JReason;
import com.easou.common.api.JUser;
import com.easou.session.SessionId;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;

/**
 * Process the /validate and /serviceValidate URL requests.
 * <p>
 * Obtain the Service Ticket and Service information and present them to the CAS
 * validation services. Receive back an Assertion containing the user Principal
 * and (possibly) a chain of Proxy Principals. Store the Assertion in the Model
 * and chain to a View to generate the appropriate response (CAS 1, CAS 2 XML,
 * SAML, ...).
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class ServiceValidateController extends AbstractController {

	// Log log = LogFactory.getLog(ServiceValidateController.class);
	protected final Log log = LogFactory
			.getLog(ServiceValidateController.class);

	/** View if Service Ticket Validation Fails. */
	private static final String DEFAULT_SERVICE_FAILURE_VIEW_NAME = "casServiceFailureView";

	/** View if Service Ticket Validation Succeeds. */
	private static final String DEFAULT_SERVICE_SUCCESS_VIEW_NAME = "casServiceSuccessView";

	/** Constant representing the PGTIOU in the model. */
	private static final String MODEL_PROXY_GRANTING_TICKET_IOU = "pgtIou";

	/** Constant representing the Assertion in the model. */
	private static final String MODEL_ASSERTION = "assertion";

	/** 返回结果类型 */
	private static final String RESULT_TYPE = "resultType";
	/** 返回结果为json */
	private static final String RESULT_JSON = "json";

	/** The CORE which we will delegate all requests to. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	/** The validation protocol we want to use. */
	@NotNull
	private Class<?> validationSpecificationClass = Cas20ProtocolValidationSpecification.class;

	/** The proxy handler we want to use with the controller. */
	@NotNull
	private ProxyHandler proxyHandler;

	/** The view to redirect to on a successful validation. */
	@NotNull
	private String successView = DEFAULT_SERVICE_SUCCESS_VIEW_NAME;

	/** The view to redirect to on a validation failure. */
	@NotNull
	private String failureView = DEFAULT_SERVICE_FAILURE_VIEW_NAME;

	/** Extracts parameters from Request object. */
	@NotNull
	private ArgumentExtractor argumentExtractor;
	
	@NotNull
	private EucUserService eucUserService;
	
	@NotNull
	private AsynManager<EucUser> asynUserManager;

	public void setAsynUserManager(AsynManager<EucUser> asynUserManager) {
		this.asynUserManager = asynUserManager;
	}

	public void setEucUserService(EucUserService eucUserService) {
		this.eucUserService = eucUserService;
	}

	/**
	 * Overrideable method to determine which credentials to use to grant a
	 * proxy granting ticket. Default is to use the pgtUrl.
	 * 
	 * @param request
	 *            the HttpServletRequest object.
	 * @return the credentials or null if there was an error or no credentials
	 *         provided.
	 */
	protected Credentials getServiceCredentialsFromRequest(
			final HttpServletRequest request) {
		final String pgtUrl = request.getParameter("pgtUrl");
		if (StringUtils.hasText(pgtUrl)) {
			try {
				return new HttpBasedServiceCredentials(new URL(pgtUrl));
			} catch (final Exception e) {
				log.error("Error constructing pgtUrl", e);
			}
		}

		return null;
	}

	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) {
		binder.setRequiredFields("renew");
	}

	protected final ModelAndView handleRequestInternal(
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始验证ST信息.....");
		}
		final WebApplicationService service = this.argumentExtractor
				.extractService(request);
		final String serviceTicketId = service != null ? service
				.getArtifactId() : null;
				
	    String uid = ConditionUtil.getUid(request);
	    String esid = ConditionUtil.getEsid(request);
	    String channel = ConditionUtil.getChannel(request);

		if (service == null || serviceTicketId == null) {
			if (log.isDebugEnabled()) {
				log.debug(String
						.format("Could not process request; Service: %s, Service Ticket Id: %s",
								service, serviceTicketId));
			}
			return generateErrorView("INVALID_REQUEST", "INVALID_REQUEST", null);
		}
        //校验是否成功
		boolean vSuccess = false;
		try {
			final Credentials serviceCredentials = getServiceCredentialsFromRequest(request);
			String proxyGrantingTicketId = null;

			// XXX should be able to validate AND THEN use
			if (serviceCredentials != null) {
				try {
					proxyGrantingTicketId = this.centralAuthenticationService
							.delegateTicketGrantingTicket(serviceTicketId,
									serviceCredentials);
				} catch (final TicketException e) {
					log.error("TicketException generating ticket for: "
							+ serviceCredentials, e);
				}
			}
			final Assertion assertion = this.centralAuthenticationService
					.validateServiceTicket(serviceTicketId, service);

			final ValidationSpecification validationSpecification = this
					.getCommandClass();
			final ServletRequestDataBinder binder = new ServletRequestDataBinder(
					validationSpecification, "validationSpecification");
			initBinder(request, binder);
			binder.bind(request);

			if (!validationSpecification.isSatisfiedBy(assertion)) {
				if (log.isDebugEnabled()) {
					log.debug("ServiceTicket [" + serviceTicketId
							+ "] does not satisfy validation specification.");
				}
				return generateErrorView("INVALID_TICKET",
						"INVALID_TICKET_SPEC", null);
			}

			onSuccessfulValidation(uid,channel,esid,serviceTicketId, assertion);
			if (isResultJson(request)) {//请求要求返回json,目前此返回结果只支持SDK返回
				//TODO查找用户数据
				String eaId = assertion.getChainedAuthentications().get(0)
						.getPrincipal().getId();
				EucUser eucUser = eucUserService.queryUserInfoById(eaId);
				JUser jUser = new JUser();
				if (null != eucUser) {
					vSuccess = true;
					BeanUtils.copyProperties(eucUser, jUser);
					String ticketGrantingTicket = assertion.getTicketGrantingTicket();	
					JSONObject jsonObject = new JSONObject();
					//成功编码
					jsonObject.put("code", 0);
					EucToken token = new EucToken();
					token.setToken(ticketGrantingTicket);
					jsonObject.put("token", token);
					jsonObject.put("user", jUser);
					//生成登录后的esid
			        String loginEsid = SessionId.createLogonId(jUser.getId(), esid, jUser.getRandomPasswd());
			        jsonObject.put("esid", loginEsid);
					String result = JSON.toJSONString(jsonObject);
				    response.getWriter().print(result);
				    return null;
				}

			} else {//请求要求返回XML，目前此返回结果只支持过滤器模式
				// 返回页面
				final ModelAndView success = new ModelAndView(this.successView);
				success.addObject(MODEL_ASSERTION, assertion);

				if (serviceCredentials != null && proxyGrantingTicketId != null) {
					final String proxyIou = this.proxyHandler.handle(
							serviceCredentials, proxyGrantingTicketId);
					success.addObject(MODEL_PROXY_GRANTING_TICKET_IOU, proxyIou);
				}

				if (log.isDebugEnabled()) {
					log.debug(String.format(
							"Successfully validated service ticket: %s",
							serviceTicketId));
				}

				return success;
			}
		} catch (final TicketValidationException e) {
			BizLogUtil.stLog(uid,channel,LogConstant.RESULT_FAILURE,esid,serviceTicketId, LogConstant.ST_VALIDATE_ERROR);
			log.error(e, e);
			return generateErrorView(e.getCode(), e.getCode(),
					new Object[] { serviceTicketId,
							e.getOriginalService().getId(), service.getId() });
		} catch (final TicketException te) {
			BizLogUtil.stLog(uid,channel,LogConstant.RESULT_FAILURE,esid,serviceTicketId, LogConstant.ST_VALIDATE_ERROR);
			log.error(te, te);
			return generateErrorView(te.getCode(), te.getCode(),
					new Object[] { serviceTicketId });
		} catch (final UnauthorizedServiceException e) {
			BizLogUtil.stLog(uid,channel,LogConstant.RESULT_FAILURE,esid,serviceTicketId != null ? serviceTicketId : "",
					LogConstant.ST_VALIDATE_UNAUTHORIZED_ERROR);
			log.error(e, e);
			return generateErrorView(e.getMessage(), e.getMessage(), null);
		}finally{
			if (isResultJson(request)&&!vSuccess) {//请求要求返回json,目前此返回结果只支持SDK返回
				JSONObject jsonObject = new JSONObject();
				//成功编码
				jsonObject.put("code", 2001);
				jsonObject.put("desc",new JReason("11","ST验证失败")); 
				String result = JSON.toJSONString(jsonObject);
			    response.getWriter().print(result);
			    return null;
			}
		}
	    return null;
	}

	protected void onSuccessfulValidation(final String uid,final String channel,String esid,
			final String serviceTicketId,
			final Assertion assertion) {
		String eaId = assertion.getChainedAuthentications().get(0)
				.getPrincipal().getId();
		EucUser eucUser= new EucUser();
		eucUser.setId(Long.valueOf(eaId));
		eucUser.setLastLoginTime(new Date(System.currentTimeMillis()));
		asynUserManager.asynUpdate(eucUser);
		// template method with nothing to do.
		// 验证成功写日志
		BizLogUtil.stLog(uid,channel,LogConstant.RESULT_SUCCESS,esid,serviceTicketId, LogConstant.ST_VALIDATE_SUCCESS);
	}

	private ModelAndView generateErrorView(final String code,
			final String description, final Object[] args) {
		final ModelAndView modelAndView = new ModelAndView(this.failureView);
		final String convertedDescription = getMessageSourceAccessor()
				.getMessage(description, args, description);
		modelAndView.addObject("code", code);
		modelAndView.addObject("description", convertedDescription);

		return modelAndView;
	}

	private ValidationSpecification getCommandClass() {
		try {
			return (ValidationSpecification) this.validationSpecificationClass
					.newInstance();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param centralAuthenticationService
	 *            The centralAuthenticationService to set.
	 */
	public final void setCentralAuthenticationService(
			final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public final void setArgumentExtractor(
			final ArgumentExtractor argumentExtractor) {
		this.argumentExtractor = argumentExtractor;
	}

	/**
	 * @param validationSpecificationClass
	 *            The authenticationSpecificationClass to set.
	 */
	public final void setValidationSpecificationClass(
			final Class<?> validationSpecificationClass) {
		this.validationSpecificationClass = validationSpecificationClass;
	}

	/**
	 * @param failureView
	 *            The failureView to set.
	 */
	public final void setFailureView(final String failureView) {
		this.failureView = failureView;
	}

	/**
	 * @param successView
	 *            The successView to set.
	 */
	public final void setSuccessView(final String successView) {
		this.successView = successView;
	}

	/**
	 * @param proxyHandler
	 *            The proxyHandler to set.
	 */
	public final void setProxyHandler(final ProxyHandler proxyHandler) {
		this.proxyHandler = proxyHandler;
	}

	/**
	 * 判断返回结果是否是JSON
	 * 
	 * @param request
	 * @return
	 */
	private boolean isResultJson(HttpServletRequest request) {
		String resultType = request.getParameter(RESULT_TYPE);
		if (resultType != null && RESULT_JSON.equalsIgnoreCase(resultType)) {
			return true;
		}
		return false;
	}
}
