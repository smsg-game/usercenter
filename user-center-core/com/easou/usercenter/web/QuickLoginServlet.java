package com.easou.usercenter.web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.util.StringUtil;
import com.easou.game.GameConfig;
import com.easou.usercenter.asyn.AsynGameManager;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.context.SpringContext;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.impl.EucUserServiceImpl;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;

public class QuickLoginServlet extends HttpServlet {

	private static final long serialVersionUID = -2384673208230242168L;
	private static Log log = LogFactory.getLog(QuickLoginServlet.class);
	private static final String DEFAULT_VERSION = "2.1";		//默认2.1大厅版本
	private static final String DEFAULT_PAGE = "/view/jsp/touch/ui/quickLogin.jsp";	//默认jsp
	private static final String gameVersion = GameConfig.getProperty("game.version");	// 默认游戏大厅版本
	private static final String gameUrl =  GameConfig.getProperty("game.url");	// 默认游戏大厅路径
	private static final String defaultLoginPage = GameConfig.getProperty("quick.login.default.page");	// 默认游戏登录注册页面
	private static final String DEFAULT_QN = "33";

	private static final String LOGIN_TYPE = "qlogin";
	
	private static final String REGISTER_TYPE = "qregister";
	
	private AsynGameManager asynGameManager=(AsynGameManager)SpringContext.getContext().getBean("asynGameManager");
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String uri = request.getRequestURI();
			uri = uri.substring(1);
			
			if ("quickLogin".equals(uri)) {
				// 自动登录，成功直接跳转到service，否则跳转至登录注册页
				quickLogin(request, response);
			} else if ("autoReg".equals(uri)) {
				// ajax自动注册
				autoReg(request, response);
				// SpringContext.getContext().getBean("eucUserServiceImpl", EucUserServiceImpl.class).queryUserInfoByName("jay_liang");
			}
			
		} catch (Exception e) {
			log.error(e,e);
		}

	}

	/**
	 * 自动注册
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws EucParserException
	 */
	protected void autoReg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String source = ConditionUtil.getChannel(request);
		String qn= ConditionUtil.getQn(request);
		String appAgent = ConditionUtil.getAppAgent(request);
		String appId = ConditionUtil.getGameId(request);	// 游戏id作为appId
		
		if(appId==null) {
			appId=ConditionUtil.getAppId(request);
		}
		RequestInfo info = new RequestInfo();
		info.setEsid(esid);
		info.setUid(uid);
		info.setSource(source);
		info.setQn(qn);
		info.setAgent(appAgent);
		info.setAppId(appId);

		JSONObject json = new JSONObject();
		try {
			// 自动注册接口
			EucApiResult<EucAuthResult> result = EucApiAuthCall.autoAKeyRegist(request, response, true, info);
			
			if (CodeConstant.OK.equals(result.getResultCode())) {
				// 注册成功
				ExpJUser user = (ExpJUser) result.getResult().getUser();
				json.put("name", user.getName());
				json.put("passwd", user.getPasswd());
				json.put("success", "true");
//				BizLogUtil.visitLog(request.getRequestURI(), uid, esid, REGISTER_TYPE, "100", appAgent);
			} else {
				// 注册失败
				json.put("success", "false");
//				BizLogUtil.visitLog(request.getRequestURI(), uid, esid, REGISTER_TYPE, "200", appAgent);
			}
		} catch (Exception e) {
			log.error(e,e);
			json.put("success", "false");
//			BizLogUtil.visitLog(request.getRequestURI(), uid, esid, REGISTER_TYPE, "200", appAgent);
		}
		response.getOutputStream().write(json.toString().getBytes());
		response.flushBuffer();
	}

	/**
	 * 自动登录
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void quickLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			EucParserException {
		// 请求版本
		String requestVersion = request.getParameter("v");
		// 会话参数
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String source = "qlog"+(requestVersion==null?"":requestVersion);
		String qn = ConditionUtil.getQn(request);
		String appAgent = ConditionUtil.getAppAgent(request);
		if (StringUtil.isEmpty(qn)) {
			qn = DEFAULT_QN;
		}

		// 游戏相关参数
		String service = request.getParameter("service");
		String appId = request.getParameter("gameId");
		if(appId==null) {
			appId = request.getParameter(Constant.APPID_TAG);
		}
		String autoReg = request.getParameter("auto");
		if (null == service || "".equals(service.trim())) {
			// 没有提供service，回到大厅首页
			String esidStr = "";
			if (!(null == esid || "".equals(esid.trim()))) {
				esidStr = "?esid=" + esid;
			}
			service = gameUrl + esidStr;
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
		request.setAttribute("autoReg", "1".equals(autoReg) ? true: false);
		String page = getDisplayPage(requestVersion);
		try {
			// 获取cookie自动登录，如登录不成功则显示登录注册页
			EucApiResult<EucAuthResult> result = EucApiAuthCall.autoLogin(
					request, response, info);
			if (result == null	|| !CodeConstant.OK.equals(result.getResultCode())) {
//				BizLogUtil.visitLog(request.getRequestURI(), uid, esid,
//						LOGIN_TYPE, "200", appAgent);
				// 登录未成功
				request.getRequestDispatcher(page).forward(request, response);
			} else {
				// 登录成功
				if (log.isDebugEnabled()) {
					log.debug("登录成功, service=" + service + " appId=" + appId
							+ " auto=" + autoReg);
				}
				if (null != service) {
					String serviceTicket = ServiceFactory
							.getInstance()
							.getCentralAuthenticationService()
							.grantServiceTicket(
									result.getResult().getToken().getToken(),
									SimpleWebApplicationServiceImpl
											.createServiceFrom(request));
					service = addTicket(service, serviceTicket, qn);
				}
//				BizLogUtil.visitLog(request.getRequestURI(), uid, esid,LOGIN_TYPE, "100", appAgent);
				response.sendRedirect(service);
				/*记录游戏历史记录*/
                if(!StringUtil.isEmpty(appId)){
                    asynGameManager.savePlayHistory(appId,result.getResult().getUser().getId().longValue());
                }
			}
		} catch (Exception e) {
			log.error(e, e);
//			BizLogUtil.visitLog(request.getRequestURI(), uid, esid, LOGIN_TYPE,	"200", appAgent);
			request.getRequestDispatcher(page).forward(request, response);
		}
		return;
	}
	
	// 添加ticket参数,如果ticket已存在,用新生成的替换
	private static String addTicket(String service, String ticket, String qn) {
		if (service == null || !service.startsWith("http")) {
			return null;
		}
		if (StringUtil.isEmpty(ticket)) {
			return service;
		}
		String ticketParam = "ticket="+ticket;
		int t = service.indexOf("?");
		if (t == -1) {
			service = service + "?" + ticketParam;
		} else {
			if (service.length() - 1 == t) {
				service = service + ticketParam;
			} else {
				boolean ticketAdded = false;
				boolean hasParam = false;
				StringBuffer sb = new StringBuffer();
				String[] param = service.substring(t+1).split("&");
				sb.append(service.substring(0, t)).append("?");
				for (int i = 0; i < param.length; i++) {
					String temp[] = param[i].split("=");
					if("ticket".equals(temp[0])) {
						ticketAdded = true;
						sb.append(ticketParam);
					} else {
						sb.append(param[i]);
					}
					hasParam = true;
					if(i<param.length-1) {
						sb.append("&");
					}
				}
				if(!ticketAdded) {
					if(hasParam) 
						sb.append("&");
					sb.append(ticketParam);
				}
				service = sb.toString();
			}
		}
		if(service.indexOf("qn=")==-1) {
			service =  service + "&qn=" + qn;
		}
		return service;
	}
	
	/**
	 * 根据参数展现不同的页面
	 * @param requestVersion
	 * @return
	 */
	private String getDisplayPage(String requestVersion) {
		if (StringUtil.isEmpty(requestVersion)) {// 请求版本为空
			if (!StringUtil.isEmpty(gameVersion)) {//配置文件的版本不为空
				requestVersion = gameVersion;
			} else {//默认版本
				requestVersion = DEFAULT_VERSION;
			}
		}
		String page = defaultLoginPage;
		if (StringUtil.isEmpty(page)) {
			page = DEFAULT_PAGE;
		}
		if (requestVersion.compareTo("2.1") >= 0) {// 2.1版本以上的页面跳转
			page = "/view/jsp/game/touch/ui/quickLogin.jsp";
		}
		return page;
	}

	public static void main(String[] args) {
		String test1 = "http://sso.fantingame.com";
		System.out.println(addTicket(test1, "2", null));
		String test2 = "http://sso.fantingame.com?";
		System.out.println(addTicket(test2, "2", "33"));
		String test3 = "http://sso.fantingame.com?t=2";
		System.out.println(addTicket(test3, "2", "33_1"));
		String test4 = "http://sso.fantingame.com?ticket=1&b=2";
		System.out.println(addTicket(test4, "2", "33_1"));
		String test5 = "http://sso.fantingame.com?&";
		System.out.println(addTicket(test5, "2", "33_1"));
	}
}
