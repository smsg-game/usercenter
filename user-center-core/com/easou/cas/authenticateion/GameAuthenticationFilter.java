/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.easou.cas.authenticateion;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.util.CommonUtils;

import com.easou.cas.auth.EucAuth;
import com.easou.cas.auth.EucAuthHelper;
import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucSimpleAuthHelper;
import com.easou.cas.auth.EucToken;
import com.easou.cas.validation.EAssertion;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;
import com.easou.common.util.CookieUtil;
import com.easou.game.GameConfig;

/**
 * Filter implementation to intercept all requests and attempt to authenticate
 * the user by redirecting them to CAS (unless the user has a ticket).
 * <p>
 * This filter allows you to specify the following parameters (at either the
 * context-level or the filter-level):
 * <ul>
 * <li><code>casServerLoginUrl</code> - the url to log into CAS, i.e.
 * https://cas.rutgers.edu/login</li>
 * <li><code>renew</code> - true/false on whether to use renew or not.</li>
 * <li><code>gateway</code> - true/false on whether to use gateway or not.</li>
 * </ul>
 * 
 * <p>
 * Please see AbstractCasFilter for additional properties.
 * </p>
 * 
 * @author Scott Battaglia
 * @version $Revision: 11768 $ $Date: 2007-02-07 15:44:16 -0500 (Wed, 07 Feb
 *          2007) $
 * @since 3.0
 */
public class GameAuthenticationFilter extends EAuthenticationFilter {

	/**
	 * The URL to the CAS Server login.
	 */
	protected String casServerLoginUrl;

	/**
	 * Whether to send the renew request or not.
	 */
//	private boolean renew = false;

	/**
	 * Whether to send the gateway request or not.
	 */
	protected boolean gateway = false;

	protected GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

	public void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession();
		if (log.isDebugEnabled()) {
			log.debug("this page is must logining,session[" + session + "]");
		}
		final EAssertion assertion = session != null ? (EAssertion) session
				.getAttribute(CONST_CAS_ASSERTION) : null;
		if (assertion != null) {
			EucToken token = (EucToken) assertion.getAttributes().get("token");
			Cookie tgc = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
			// 判定cookie中的tgc是否与会话中的相同
			if (tgc != null && tgc.getValue().equals(token.getToken())) {
				if (log.isDebugEnabled()) {
					log.debug("already logined user[" + assertion.getUserId()	+ "]");
				}
				filterChain.doFilter(request, response);
				return;
			}
		}
		final String serviceUrl = constructServiceUrl(request, response);
		final String ticket = CommonUtils.safeGetParameter(request,
				getArtifactParameterName());
		// final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(
		// request, serviceUrl);
		final String wver = CommonUtils.safeGetParameter(request, WAP_VERSION);
		if (CommonUtils.isNotBlank(ticket)) {
			log.debug("带有ticket参数");
			response.sendRedirect(serviceUrl);
			return;
		}
		try {
			if (request.getRequestURI().indexOf("game/touch/gtLogin") != -1
					|| request.getRequestURI().indexOf("game/touch/sforget") != -1
					|| request.getRequestURI().indexOf("game/touch/resetpass") != -1
					|| request.getRequestURI().indexOf("game/touch/resetSucc") != -1
					|| request.getRequestURI().indexOf("game/touch/findbym") != -1) {
				// 不需要登录的页面
				filterChain.doFilter(request, response);
			} else {
				// 需要登录
				EucApiResult<EucAuthResult> result = EAuthenticationWrapper
						.validatenew(request, response);
				if (CodeConstant.OK.equals(result.getResultCode())) {
					// 验证通过
					EAssertion newAssertion = new EAssertion();
					newAssertion
							.setUserId(result.getResult().getUser().getId());
					newAssertion.setAttribute("token", result.getResult()
							.getToken());
					request.getSession().setAttribute(CONST_CAS_ASSERTION,
							newAssertion);
					filterChain.doFilter(request, response);
				} else {
					// 验证不通过，重定向至登录授权地址
					EucAuthHelper helper = EucSimpleAuthHelper.getInstance();
					EucAuth auth = new EucAuth();
					// 鉴权后重定向地址
					auth.setService(serviceUrl);
					auth.setWver(wver);
					// auth.setGateWay(false); //如果为true时，不跳转到登陆页
					RequestInfo info = new RequestInfo();
					info.setEsid(request.getParameter("esid"));
					info.setUid(request.getParameter("uid"));
					info.setSource(request.getParameter("source"));
					// 判断是否是鉴权请求，如果不是，跳转到鉴权页面
					helper.auth(request, response, auth, getAuthURL(request),info);
					return;
				}
			}
		} catch (EucParserException e) {
			log.error(e, e);
		}
	}

	private String getAuthURL(HttpServletRequest request){
		return GameConfig.getProperty("auth.url");
	}
}
