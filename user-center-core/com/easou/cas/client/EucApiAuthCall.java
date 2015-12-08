package com.easou.cas.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucToken;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;
import com.easou.common.api.JReason;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.CasConstant;
import com.easou.common.constant.LoginType;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.InAuthParametric;
import com.easou.common.util.CookieUtil;

/**
 * 登录鉴权接口调用
 * 
 * @author damon
 * @since 2012.06.20
 * @version 2.0
 * 
 */
public class EucApiAuthCall {

	protected static final Log LOG = LogFactory.getLog(EucApiAuthCall.class);

	protected static EucService eucService = EucService.getInstance();

	// 用户登录类型
	public static final String LOGIN_TYPE = "loginType";
	// 记住密码标识
	public static final String REMEMBER_PASSWORD = "remember";

	private final static String TGC_VERIFICATION_FAIL = "7";

	public static String loginApiUri = "/api/login";
	/** 登出 */
	public static String logoutApiUri = "/api/logout";
	
	private static AuthParametric<RequestInfo> authPara = new InAuthParametric();

	/**
	 * 检测用户的登录状态 用户在进行用户名密码登录前，可以调用此方法检测用户的登录状态
	 * 
	 * 检测的内容包括： 1、session中是否存在用户信息Assertion对象 2、tgc是否存在；存在则调用接口验证
	 * 3、cookie中是否存在自动登录标识和用户信息，如果存在进行登录验证
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            应答对象
	 * @param info
	 *            请求参数
	 * @return
	 */
	public static EucApiResult<EucAuthResult> checkLogin(
			HttpServletRequest request, HttpServletResponse response,
			RequestInfo info) throws EucParserException {

		Cookie tgcCookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
		if (tgcCookie != null) {
			return autoLogin(request, response, info);
		}
		return buildNullResult("5", "无登录信息", null);
	}

	/**
	 * 用户名密码登录 此方法在用户登录成功后，会将用户信息存储到session和cookie中 用户登录成功后，会将用户信息标识（U）写入cookie
	 * 
	 * 此方法专门提供给WEB\WAP应用调用
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param rememberPass
	 *            记住密码
	 * @return 梵町ID
	 */
	public static EucApiResult<EucAuthResult> login(HttpServletRequest request,
			HttpServletResponse response, String userName, String password,
			boolean rememberPass, RequestInfo info) throws EucParserException {
		JBean jBean = login(userName, password, rememberPass, "web", info);
		return loginResult(request, response, jBean);
	}

	/**
	 * 用户名密码登录 此方法专门提供给APP客户端应用调用
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param appType
	 *            应用类型 （类型[值]）APP类型[app]、WEB类型[web]、WAP类型[wap]
	 * 
	 * @return EucApiResult<EucAuthResult>
	 */
	public static EucApiResult<EucAuthResult> login(String userName,
			String password, String appType, RequestInfo info)
			throws EucParserException {
		JBean jBean = login(userName, password, false, appType, info);
		EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
				jBean);
		if (jBean.getBody() != null) {
			EucToken token = jBean.getBody().getObject("token", EucToken.class);
			JUser user = jBean.getBody().getObject("user", JUser.class);
			boolean isRegist = jBean.getBody().getBooleanValue("isRegist");
			String esid = jBean.getBody().getString("esid");
			EucAuthResult authResult = new EucAuthResult(token, user, esid,
					isRegist);
			result.setResult(authResult);
		}
		return result;
	}

	/**
	 * 用户名密码登录 此方法不提供给外部调用
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param rememberPass
	 *            记住密码
	 * @param appType
	 *            应用类型 （类型[值]）APP类型[app]、WEB类型[web]、WAP类型[wap]
	 * @return
	 */
	protected static JBean login(String userName, String password,
			boolean rememberPass, String appType, RequestInfo info)
			throws EucParserException {
		JBody jb = new JBody();
		// 登录类型，默认用户名密码登录
		jb.put(LOGIN_TYPE, LoginType.DEFAULT);
		jb.putContent("userName", userName);
		jb.putContent("password", password);
		if (appType != null) {
			jb.putContent("appType", appType);
		}
		if (rememberPass) {
			jb.putContent(REMEMBER_PASSWORD, rememberPass);
		}
		JBean jBean = eucService.getResult(loginApiUri, jb,authPara, info);
		return jBean;
	}

	/**
	 * 自动登录
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            应答对象
	 * @param info
	 *            请求参数
	 * @return
	 */
	public static EucApiResult<EucAuthResult> autoLogin(
			HttpServletRequest request, HttpServletResponse response,
			RequestInfo info) throws EucParserException {
		Cookie tgcCookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
		Cookie uCookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_U);
		String tgc = null;
		String u = null;
		if (tgcCookie != null && tgcCookie.getValue() != null
				&& !"".equals(tgcCookie.getValue().trim())) {
			tgc = tgcCookie.getValue();
		}
		if (uCookie != null && uCookie.getValue() != null
				&& !"".equals(uCookie.getValue().trim())) {
			u = uCookie.getValue();
		}
		if (tgc == null && u == null) {
			return buildNullResult("5", "无登录信息", null);
		}
		JBody jb = new JBody();
		if (u != null) {// 提交COOKIE登录信息
			jb.put(LOGIN_TYPE, LoginType.AUTO);
			// 用户信息
			jb.put(CookieUtil.COOKIE_U, u);
		}
		if (tgc != null) {// 提交TGC信息
			jb.put(LOGIN_TYPE, LoginType.TGC);
			jb.put(CookieUtil.COOKIE_TGC, tgc);
		}

		// 解析返回結果
		JBean jBean = eucService.getResult(loginApiUri, jb,authPara,info);
		return loginResult(request, response, jBean);
	}

	/**
	 * 登录结果处理
	 * 
	 * @param jBean
	 *            被处理的结果
	 * @return
	 */
	private static EucApiResult<EucAuthResult> loginResult(
			HttpServletRequest request, HttpServletResponse response,
			JBean jBean) throws EucParserException {
		EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
				jBean);
		JHead jHead = jBean.getHead();
		if (CodeConstant.OK.equals(jHead.getRet())) {// 登录成功
			JBody jBody = jBean.getBody();
			// 获取TGC作为token
			// String token = jBody.getString(CookieUtil.COOKIE_TGC);
			// EucAuthResult eucAuthResult =
			// jBean.getBody().getObject("authResult", EucAuthResult.class);
			EucToken token = jBean.getBody().getObject("token", EucToken.class);
			JUser user = jBean.getBody().getObject("user", JUser.class);
			boolean isRegist = jBean.getBody().getBooleanValue("isRegist");
			String esid = jBean.getBody().getString("esid");
			EucAuthResult eucAuthResult = new EucAuthResult(token, user, esid,
					isRegist);
			// 将用户对象保存到结果集中
			result.setResult(eucAuthResult);
			LOG.info("user[" + eucAuthResult.getUser().getId()
					+ "] login in success");
			if (jBody.getString(CookieUtil.COOKIE_TGC) != null) {// 保存TGC
				CookieUtil.addCookie(
						CookieUtil.COOKIE_TGC,
						jBody.getString(CookieUtil.COOKIE_TGC),
						jBody.getString("tgcDomain"),
						jBody.getString("tgcPath"),
						jBody.getString("tgcAge") != null ? Integer
								.valueOf(jBody.getString("tgcAge")) : 0,
						response);
			}
			if (jBody.getString(CookieUtil.COOKIE_U) != null) {// 已登录
				CookieUtil.addCookie(
						CookieUtil.COOKIE_U,
						jBody.getString(CookieUtil.COOKIE_U),
						jBody.getString("uDomain"),
						jBody.getString("uPath"),
						jBody.getString("uAge") != null ? Integer.valueOf(jBody
								.getString("uAge")) : 0, response);
			}
		} else if (CodeConstant.BUSI_ERROR.equals(jHead.getRet())) {
			JDesc desc = jBean.getDesc();
			for (int i = 0; i < desc.size(); i++) {
				if (TGC_VERIFICATION_FAIL.equals(desc.getReason(i).getC())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("TGC is failure");
					}
					CookieUtil.removeCookie(request, response,
							CookieUtil.COOKIE_TGC);
				}
			}
		}
		return result;
	}

	/**
	 * 登出
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            应答对象
	 * @param info
	 *            请求参数
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<Boolean> logout(HttpServletRequest request,
			HttpServletResponse response, RequestInfo info)
			throws EucParserException {
		JBody jb = new JBody();
		final HttpSession session = request.getSession();
		Cookie tgcCookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
		if (tgcCookie != null) {// TGC登录或cookie登录验证
			if (session != null) {// 删除session中的用户信息
				session.removeAttribute(CasConstant.CONST_CAS_ASSERTION);
			}
			// 传递参数
			jb.put(CookieUtil.COOKIE_TGC, tgcCookie.getValue());
			// 解析返回結果
			JBean jBean = eucService.getResult(logoutApiUri, jb,authPara,info);
			// 解析返回结果
			JBody jBody = jBean.getBody();
			// 生成结果对象
			EucApiResult<Boolean> result = new EucApiResult<Boolean>(jBean);
			if (CodeConstant.OK.equals(jBean.getHead().getRet())) {// 登出成功
				if (jBody != null) {
					result.setResult(jBody.getBooleanValue("success"));
				} else {
					result.setResult(true);
				}
				// TODO 删除COOKIE信息
				CookieUtil.removeCookie(request, response,
						CookieUtil.COOKIE_TGC);
				CookieUtil.removeCookie(request, response, CookieUtil.COOKIE_U);
			} else {// 登出失败
				result.setResult(false);
			}
			return result;
		} else {
			return buildNullResult("9", "用户未登录", true);
		}
	}

	/**
	 * 返回一个未登录信息
	 * 
	 * @return
	 */
	private static <T> EucApiResult<T> buildNullResult(String code,
			String content, T t) {
		EucApiResult<T> result = new EucApiResult<T>();
		result.setResultCode(CodeConstant.BUSI_ERROR);
		List<JReason> desc = new ArrayList<JReason>();
		desc.add(new JReason(code, content));
		result.setDescList(desc);
		result.setResult(t);
		return result;
	}

	/**
	 * 登录名密码注册后直接登录(得到token和JUser对象)
	 * 
	 * @param username
	 * @param password
	 * @param appType
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<EucAuthResult> registerByName(String username,
			String password, String appType, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("username", username);
		jbody.putContent("password", password);
		JBean jrBean = eucService.getResult("/api/registByName", jbody,authPara,info);
		EucApiResult<EucAuthResult> result1 = new EucApiResult<EucAuthResult>(
				jrBean); // 注册结果
		if(result1.getResultCode().equals(CodeConstant.OK)) {
			return result1;
		}
		return login(username, password, appType, info);
	}

	/**
	 * 登录名密码注册接口,注册成功后直接登录(得到token和JUser对象),并提供记住密码功能(写入cookie)
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @param password
	 * @param rememberPass
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<EucAuthResult> registerByName(
			HttpServletRequest request, HttpServletResponse response,
			String username, String password, boolean rememberPass,
			RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("username", username);
		jbody.putContent("password", password);
		JBean jrBean = eucService.getResult("/api/registByName", jbody,authPara,info);
		EucApiResult<EucAuthResult> result1 = new EucApiResult<EucAuthResult>(
				jrBean); // 注册结果
		if(!result1.getResultCode().equals(CodeConstant.OK)) {
			return result1;
		}
		return login(request, response, username, password, rememberPass, info);
	}

	/**
	 * 直接通过手机号和密码注册(内部接口),同时完成登录(得到token和JUser对象)
	 * 
	 * @param mobile
	 * @param password
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<EucAuthResult> regByRealMobile(String mobile,
			String password, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		jbody.putContent("password", password);
		JBean jrBean = eucService
				.getResult("/api/regByRealMobile", jbody,authPara,info);
		EucApiResult<EucAuthResult> result1 = new EucApiResult<EucAuthResult>(
				jrBean); // 注册结果
		if(result1.getResultCode().equals(CodeConstant.OK)) {
			return result1;
		}
		return login(mobile, password, "app", info);
	}

	/**
	 * 客户端通过验证码验证注册
	 * 
	 * @param mobile
	 * @param password
	 * @param veriCode
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<EucAuthResult> regByVericode(String mobile,
			String password, String veriCode, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		jbody.putContent("password", password);
		jbody.putContent("veriCode", veriCode);
		JBean jrBean = eucService.getResult("/api/regByVericode", jbody,authPara,info);
		EucApiResult<EucAuthResult> result1 = new EucApiResult<EucAuthResult>(
				jrBean); // 注册结果
		if(result1.getResultCode().equals(CodeConstant.OK)) {
			return result1;
		}
		return login(mobile, password, "app", info);
	}

	/**
	 * 带鉴权的一键注册
	 * 
	 * @param info
	 *            请求对象
	 * @return
	 */
	public static EucApiResult<EucAuthResult> autoAKeyRegist(RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		// 是否需要登录标识
		jbody.putContent("isNeedLogin", true);
		JBean jBean = eucService.getResult("/api/autoAKeyRegist", jbody,authPara,info);
		// 返回结果
		EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
				jBean);
		// 注册结果
		if(result.getResultCode().equals(CodeConstant.OK)) {
			ExpJUser expJUser = jBean.getBody().getObject("user",
					ExpJUser.class);
			EucToken token = jBean.getBody().getObject("token", EucToken.class);
			boolean isRegist = jBean.getBody().getBooleanValue("isRegist");
			String esid = jBean.getBody().getString("esid");
			EucAuthResult authResult = new EucAuthResult(token, expJUser, esid,
					isRegist);
			result.setResult(authResult);
			return result;
		} else {
			return result;
		}
	}

	/**
	 * 带鉴权的一键注册
	 * 此接口会在客户端的Cookie中写入单点登录的必要标识以及记住密码登录信息
	 * 
	 * @param info
	 *            请求对象
	 * @return
	 */
	public static EucApiResult<EucAuthResult> autoAKeyRegist(HttpServletRequest request, HttpServletResponse response, boolean rememberPass, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		// 是否需要登录标识
		jbody.putContent("isNeedLogin", true);
		// 记住密码
		if (rememberPass) {
			jbody.putContent(REMEMBER_PASSWORD, rememberPass);
		}
		
		jbody.putContent("appType", "web");
		JBean jBean = eucService.getResult("/api/autoAKeyRegist", jbody,authPara,info);
		EucApiResult<EucAuthResult> result = loginResult(request, response, jBean);
		if(result.getResultCode().equals(CodeConstant.OK)) {
			ExpJUser user = jBean.getBody().getObject("user", ExpJUser.class);
			result.getResult().setUser(user);
		}
		return result;
	}
}
