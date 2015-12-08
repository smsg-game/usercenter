package com.easou.cas.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;

/**
 * 鉴权提供者
 * 
 * @author damon
 * @since 2012.06.26
 * @version 1.0
 *
 */
public interface EucAuthHelper {
	
	/**
	 * 登录鉴权
	 * 
	 * @param request
	 *     请求对象
	 * @param response
	 *     应答对象
	 * @param auth
	 *     鉴权信息 
	 * @param authUrl
	 *     鉴权地址
	 */
	public void auth(HttpServletRequest request,
			HttpServletResponse response,EucAuth auth,String authUrl,RequestInfo info)throws EucParserException;
	
	/**
	 * 登录鉴权
	 * 
	 * @param request
	 *     请求对象
	 * @param response
	 *     应答对象
	 * @param auth
	 *     鉴权信息 
	 * @param authUrl
	 *     鉴权地址
	 */
	public void auth(HttpServletRequest request,
			HttpServletResponse response,EucAuth auth,RequestInfo info)throws EucParserException;
	
    /**
     * 验证Service票据
     * 
     * @param service
     *     回调地址
     * @param serviceTicket
     *     票据
     * @param info
     *     请求参数
     * @return
     * @throws EucParserException
     */
	public EucApiResult<EucAuthResult> validateServiceTicket(String service,String serviceTicket,RequestInfo info)throws EucParserException;
			
	/**
	 * 退出
	 * 
	 * @param response
	 *     应答对象
	 * @param service
	 *     回调地址
	 * @param info
	 *     请求参数
	 * @throws EucParserException
	 */
	public void logout(HttpServletResponse response,String service,RequestInfo info)throws EucParserException;

}
