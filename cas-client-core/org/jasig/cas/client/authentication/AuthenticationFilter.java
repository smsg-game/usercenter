package org.jasig.cas.client.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.CookieUtil;
import org.jasig.cas.client.util.ThirldAppHandler;
import org.jasig.cas.client.validation.Assertion;

public class AuthenticationFilter extends EAuthenticationFilter {
	protected final Log log = LogFactory.getLog(getClass());

	public final void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession();
		if (log.isDebugEnabled()) {
			log.debug("sessionL:......." + session);
		}
		final Assertion assertion = session != null ? (Assertion) session
				.getAttribute(EAuthenticationFilter.CONST_CAS_ASSERTION) : null;
		// request.setAttribute("m",new Boolean(true));
		Boolean m = (Boolean) request.getAttribute("m");
		if (m == null)
			m = false;
		if (assertion!=null||m) {
			if (log.isDebugEnabled()) {
				if(assertion!=null){
					log.debug("already logined user["+assertion.getPrincipal().getName()+"]");
				}else{
				    log.debug("this page is must logining,and jump next filter");
				}
			}
//			if(assertion!=null&&!checkLoginStat(request)){//如果为第三方APP接入，且为未登录状态，标明已登录
//				ThirldAppHandler.addThirldSuccLoginTag(request, response, assertion);
//			}
			filterChain.doFilter(request, response);
			return;
		}

		final String serviceUrl = constructServiceUrl(request, response);
		final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);
		final String ticket = CommonUtils.safeGetParameter(request,
				getArtifactParameterName());
		if (CommonUtils.isNotBlank(ticket)||wasGatewayed) {
			if (log.isDebugEnabled()) {
				log.debug("this page is not must logining,and return the ticket["+ticket+"]," +
						"wasGatewayed["+wasGatewayed+"] from sso center");
			}
			//TGC返回为空，说明验证失败，且为第三方应用
			if(!CommonUtils.isNotBlank(ticket)&&wasGatewayed){
				//修改第三方应用登录标识为未登录
                ThirldAppHandler.addThirldUnLoginTag(request, response, assertion);
			}
			filterChain.doFilter(request, response);
			return;
		}

        log.debug("no ticket(ST) and no assertion found");	
		if (isRedirectToCasServer(request)) {// 登录标识和自动登录标识存在，或者是第三方应用且还未登录;进入登录流程;
			final String modifiedServiceUrl;	
	        if (this.gateway) {
	            log.debug("setting gateway attribute in session");
	            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
	        } else {
	            modifiedServiceUrl = serviceUrl;
	        }
			final String urlToRedirectTo = CommonUtils.constructRedirectUrl(
					this.casServerLoginUrl, getServiceParameterName(),
					modifiedServiceUrl, false, this.gateway);

			if (log.isDebugEnabled()) {
				log.debug("this page is not must logining,but login info not in session");
				//log.debug("the must login target(A) is exist,or the already logined target(S) is exist");
		        log.debug("this page is not must logining,but you are first to visit it in this session");
				log.debug(" so redirecting [" + urlToRedirectTo + "] to check certification login");
			}
			response.sendRedirect(urlToRedirectTo);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	protected void initInternal(final FilterConfig filterConfig)
			throws ServletException {
		super.initInternal(filterConfig);
		// String cookieDomain = filterConfig.getInitParameter("cookieDomain");
		// if (cookieDomain == null)
		// cookieDomain = SSOCookie.CookieUtil;
		// cookie = new CookieUtil(cookieDomain);
	}
	
	/**
	 * 判断是否跳转到SSO去验证
	 * 
	 * @param request
	 * @return
	 */
	public boolean isRedirectToCasServer(HttpServletRequest request){
		if(checkLoginStat(request)){//用户状态为已登录
			return true;
		}else if(ThirldAppHandler.isRedirectToCasServer(request)){//登录标识为空(表明为第一次请求服务），且为第三方APP
			return true;
		}
		return false;
	}
	
	/**
	 * 检查用户的登录状态
	 * 
	 * @param request
	 * @return
	 */
	public boolean checkLoginStat(HttpServletRequest request){
		//自动登录标识
		Cookie autoLogin = CookieUtil.getCookie(request, CookieUtil.AUTO_LOGIN_TAG);
		//已登录标识
		Cookie alreadyLogin = CookieUtil.getCookie(request, CookieUtil.ALREADY_LOGIN_TAG);
		if((autoLogin!=null&&CookieUtil.LOGIN_STAT_VALUE.equals(autoLogin.getValue()))
				||(alreadyLogin!=null&&CookieUtil.LOGIN_STAT_VALUE.equals(alreadyLogin.getValue()))){//用户状态为已登录
			return true;
		}
		return false;
		
	}

}
