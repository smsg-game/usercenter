package com.easou.cas.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.auth.EucAuthHelper;
import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucSimpleAuthHelper;
import com.easou.cas.auth.EucToken;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;



/**
 * 第三方登录接口调用
 * 
 * @author Terry
 * @since 2012.12.20
 * @version 2.0
 * 客户端接口
 */
public class ThirdLoginApiCall{
	
	protected static final Log LOG = LogFactory.getLog(ThirdLoginApiCall.class);
	
	/**
	 * 检测用户的登录状态 用户在进行用户名密码登录前，可以调用此方法检测用户的登录状态
	 * 
	 * 检测的内容包括： 1、tgc是否存在；存在则调用接口验证
	 * 2、cookie中是否存在自动登录标识和用户信息，如果存在进行登录验证
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            应答对象
	 * @param info
	 *            请求参数
	 * @return
	 */
	public static EucApiResult<EucAuthResult> checkThirdLogin(
			HttpServletRequest request, HttpServletResponse response,
			RequestInfo info) throws EucParserException {
		return EucApiAuthCall.checkLogin(request, response, info);
	}
	
	/**
	 * 查询第三方绑定用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static EucApiResult<EucAuthResult> queryThirdBind(String service,
			String serviceTicket) throws EucParserException  {
		EucAuthHelper helper = EucSimpleAuthHelper.getInstance();
		return helper.validateServiceTicket(service, serviceTicket,null);

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
		return EucApiAuthCall.logout(request, response, info);
	}
	
	/**
	 * 带鉴权的更新密码接口
	 * 
	 * @param token
	 * @param oldPass
	 * @param newPass
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<Boolean> updatePass(EucToken token,
			String oldPass, String newPass, RequestInfo info)
			throws EucParserException {
		return EucApiCall.updatePass(token, oldPass, newPass, info);
	}
	

}
