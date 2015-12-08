package com.easou.cas.sdk;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucToken;
import com.easou.cas.auth.EucUCookie;
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

/**
 * 
 * 
 * @author damon
 * @since 2013.01.15
 * @version 1.0
 */
public class EucOApiAuthCall {

	//protected static EucService eucService = EucService.getInstance();

	protected static final Log LOG = LogFactory.getLog(EucOApiAuthCall.class);

	public static String loginApiUri = "/api2/login.json";

	public static String validateApiUri = "/api2/validate.json";
	
	public static String validateServiceTicketUri = "/api2/validateServiceTicket.json";
	
	public static String QN = "qn";

	private static AuthParametric oAuthPara = new OAuthParametric();

	/**
	 * 用户名密码登录
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param remember
	 *            记住密码
	 * @return
	 */
	public static EucApiResult<EucAuthResult> login(String userName,
			String password, boolean remember) {
		JBody jbody = new JBody();
		jbody.putContent("userName", userName);
		jbody.putContent("password", password);
		jbody.putContent("remember", remember);
		try {
			EucService eucService = EucService.getInstance();
			JBean jbean = eucService.getResult(loginApiUri, jbody, oAuthPara,
					null);
			EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
					jbean);
			if (result.getResultCode().equals(CodeConstant.OK)) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token",
						EucToken.class);
				EucUCookie u = jbean.getBody().getObject("U", EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token, jUser,
						u, String.valueOf(jUser.getId()), false);
				result.setResult(eucAuthResult);
			}
			return result;
		} catch (EucParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buildNullResult("9", "用户未登录", null);
	}

	/**
	 * 验证登录 只提供给WEB模式使用
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return
	 */
	public static EucApiResult<EucAuthResult> validate(
			final HttpServletRequest request, final HttpServletResponse response)
			throws EucParserException {
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
			// 用户信息
			jb.put(CookieUtil.COOKIE_U, u);
		}
		if (tgc != null) {// 提交TGC信息
			jb.put(CookieUtil.COOKIE_TGC, tgc);
		}
		EucService eucService = EucService.getInstance();
		if(request.getParameter(QN)!=null){
			RequestInfo info=new RequestInfo();
			info.setQn(request.getParameter(QN));
		}
		// 解析返回結果
		JBean jbean = eucService.getResult(validateApiUri, jb, oAuthPara, null);
		EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
				jbean);
		if (result.getResultCode().equals(CodeConstant.OK)) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			EucToken token = jbean.getBody().getObject("token", EucToken.class);
			// EucUCookie u = jbean.getBody().getObject("U", EucUCookie.class);
			EucAuthResult eucAuthResult = new EucAuthResult(token, jUser, null,
					String.valueOf(jUser.getId()), false);
			result.setResult(eucAuthResult);
			return result;
		} else if (result.getResultCode() != null) {
			return result;
		}
		return buildNullResult("9", "用户未登录", null);
	}

	/**
	 * 验证SERVICE TICKET
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<EucAuthResult> validateServiceTicket(final HttpServletRequest request, final HttpServletResponse response) throws EucParserException {
		String ticket = request.getParameter("ticket");
		if (ticket == null) {
			return buildNullResult("5", "无登录信息", null);
		}
		JBody jb = new JBody();
		jb.put("ticket", ticket);
		EucService eucService = EucService.getInstance();
		if(request.getParameter(QN)!=null){
			RequestInfo info=new RequestInfo();
			info.setQn(request.getParameter(QN));
		}
		// 解析返回結果
		JBean jbean = eucService.getResult(validateServiceTicketUri, jb, oAuthPara, null);
		EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(
				jbean);
		if (result.getResultCode().equals(CodeConstant.OK)) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			EucToken token = jbean.getBody().getObject("token", EucToken.class);
			// EucUCookie u = jbean.getBody().getObject("U", EucUCookie.class);
			EucAuthResult eucAuthResult = new EucAuthResult(token, jUser, null,
					String.valueOf(jUser.getId()), false);
			result.setResult(eucAuthResult);
			return result;
		} else if (result.getResultCode() != null) {
			return result;
		}
		return null;
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
