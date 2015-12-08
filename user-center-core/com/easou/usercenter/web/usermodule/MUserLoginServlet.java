package com.easou.usercenter.web.usermodule;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.JReason;
import com.easou.common.api.RequestInfo;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;

public class MUserLoginServlet extends HttpServlet {

	private static final long serialVersionUID = -5895354406922686955L;

	private static final String DEFAULT_QN = "33";
	private static final String URL_USERMODULE_INDEX = "/user/usersetting.html";
	public static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
	private static Log log = LogFactory.getLog(MUserLoginServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			quickLogin(req,resp);
		} catch (EucParserException e) {
			log.error(e, e);
		}
	}
	
	protected void quickLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			EucParserException {
		final HttpSession session = request.getSession();
		if (log.isDebugEnabled()) {
			log.debug("this page is must logining,session[" + session + "]");
		}
		String domain = SSOConfig.getProperty("domain.readonly") + URL_USERMODULE_INDEX;
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(username == null || password == null || "".equals(username) || "".equals(password)) {
			String str = "{\"c:\":\"2001\",\"m\":\"用户名或密码不能为空\"}";
			response.getWriter().write(str);
			return;
		}
		// 会话参数
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String source = ConditionUtil.getChannel(request);
		String qn = ConditionUtil.getQn(request);
		String appAgent = ConditionUtil.getAppAgent(request);
		String appType = request.getParameter("appType");
		if(appType == null || "".equals(appType.trim())) {
			appType = "APP";
		}
		if (StringUtil.isEmpty(qn)) {
			qn = DEFAULT_QN;
		}

		// 游戏相关参数
		String service = request.getParameter("service");
		String appId = request.getParameter("gameId");
		if(appId==null) {
			appId = request.getParameter(Constant.APPID_TAG);
		}
		if (null == service || "".equals(service.trim())) {
			service = domain;
		}
		request.setAttribute("esid", esid);
		request.setAttribute("source", source);
		request.setAttribute("uid", uid);
		request.setAttribute("qn", qn);
		RequestInfo info = new RequestInfo();
		info.setEsid(esid);
		info.setUid(uid);
		info.setSource(source);
		info.setQn(qn);
		info.setAgent(appAgent);
		info.setAppId(appId);
		try {
			// 获取cookie自动登录，如登录不成功则显示登录注册页
			EucApiResult<EucAuthResult> result = EucApiAuthCall.login(username, password, appType, info);
			if (result == null	|| !CodeConstant.OK.equals(result.getResultCode())) {
				// 登录未成功
				if(result != null) {
					List<JReason> msg = result.getDescList();
					String str = "{\"c:\":\"2001\",\"m\":\"登录失败，请查证后再登录\"}";
					if(msg != null && !msg.isEmpty()) {
						str = msg.get(0).getD();
						str = "{\"c:\":\"2001\",\"m\":\""+str+"\"}";
					}
					response.getWriter().write(str);
				}
//				response.sendRedirect(service);
				return;
			} else {
				EucAuthResult eucAuth = result.getResult();
				CookieUtil.addCookie(CookieUtil.COOKIE_TGC, 
						eucAuth.getToken().getToken(), CookieUtil.DEFAULT_DOMAIN, 
						"/", 60*60*24*182, response);
//				response.sendRedirect(service);
				response.getWriter().write("0");
			}
		} catch (Exception e) {
			log.error(e, e);
			response.sendRedirect(service);
		}finally {
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

}
