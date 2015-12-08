package com.easou.cas.authenticateion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucToken;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JReason;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.OAuthParametric;
import com.easou.common.util.CookieUtil;
import com.easou.usercenter.util.ConditionUtil;

public class EAuthenticationWrapper {

	private static AuthParametric<RequestInfo> oAuthPara = new OAuthParametric();
	
	private static final String VALIDATE_API = "/api2/validatenew.json";
	
	private static final String QN_TAG = "qn";
	
	private static final String GAMEID_TAG = "gameId";
	
	private static final String APPID_TAG = "appId";

	/**
	 * 验证登录 只提供给WEB模式使用
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return
	 */
	public static EucApiResult<EucAuthResult> validatenew(
			final HttpServletRequest request, final HttpServletResponse response)
			throws EucParserException {
		Cookie tgcCookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
//		Cookie uCookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_U);
		String tgc = null;
		String u = null;
		JBody jb = new JBody();
		if (tgcCookie != null && tgcCookie.getValue() != null
				&& !"".equals(tgcCookie.getValue().trim())) {
			tgc = tgcCookie.getValue();
			jb.put(CookieUtil.COOKIE_TGC, tgc);
		}
//		if (uCookie != null && uCookie.getValue() != null
//				&& !"".equals(uCookie.getValue().trim())) {
//			u = uCookie.getValue();
//			jb.put(CookieUtil.COOKIE_U, u);
//		}
		if (tgc == null && u == null) {
			return buildNullResult("5", "无登录信息", null);
		}
		EucService eucService = EucService.getInstance();
		RequestInfo info = new RequestInfo();
		if(request.getParameter(QN_TAG)!=null){
		    info.setQn(request.getParameter(QN_TAG));
		}
		if(request.getParameter(GAMEID_TAG)!=null) {
			info.setAppId(request.getParameter(GAMEID_TAG));
		}else if(request.getParameter(APPID_TAG)!=null) {
			info.setAppId(request.getParameter(APPID_TAG));
		}
		info.setAgent(ConditionUtil.getAppAgent(request));
		info.setSource(ConditionUtil.getChannel(request));
		// 解析返回結果
		JBean jbean = eucService.getResult(VALIDATE_API, jb, oAuthPara, info);
		EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
				jbean);
		if (CodeConstant.OK.equals(result.getResultCode())) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			EucToken token = jbean.getBody().getObject("token", EucToken.class);
			EucAuthResult eucAuthResult = new EucAuthResult(token, jUser, null,
					String.valueOf(jUser.getId()), false);
			result.setResult(eucAuthResult);
			return result;
		} else if (result.getResultCode() != null) {
			// 验证失败，删除相关cookie，防止循环重定向
			CookieUtil.removeCookie(request, response, CookieUtil.COOKIE_TGC);
			CookieUtil.removeCookie(request, response, CookieUtil.COOKIE_U);
			return result;
		}
		CookieUtil.removeCookie(request, response, CookieUtil.COOKIE_TGC);
		CookieUtil.removeCookie(request, response, CookieUtil.COOKIE_U);
		return buildNullResult("9", "用户未登录", null);
	}
	
	/**
	 * 返回一个未登录信息
	 * 
	 * @return
	 */
	private static <T> EucApiResult<T> buildNullResult(String errorCode,
			String content, T t) {
		EucApiResult<T> result = new EucApiResult<T>();
		result.setResultCode(CodeConstant.BUSI_ERROR);
		List<JReason> desc = new ArrayList<JReason>();
		desc.add(new JReason(errorCode, content));
		result.setDescList(desc);
		result.setResult(t);
		return result;
	}
}
