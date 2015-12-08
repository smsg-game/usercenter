package com.easou.cas.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;

/**
 * 鉴权管理类
 * 
 * @author damon
 * 
 */
public class EucAuthManager extends EucAbstractManager{
	
	protected static final Log LOG = LogFactory.getLog(EucAuthManager.class);
	
	/**
	 * 生成鉴权地址
	 * 
	 * @param auth
	 * @return
	 */
	protected String buildRedirect(EucAuth auth,String authUrl,RequestInfo info){
		StringBuffer redirectBuffer = new StringBuffer();
		redirectBuffer.append(authUrl);
		if(redirectBuffer.indexOf("?")==-1) {
			redirectBuffer.append("?");
		} else {
			redirectBuffer.append("&");
		}
		if(auth.isGateWay()){
			redirectBuffer.append("gateway=").append("true").append("&");
		}
		if(auth.isRenew()){
			redirectBuffer.append("renew=").append("true").append("&");
		}
		String wver = auth.getWver();
		if(wver!=null&&!"".equals(wver)){
			redirectBuffer.append("wver=").append(wver).append("&");
		}
		if(info!=null){
			String infoPara = info.paraToString();
			if(!"".equals(infoPara)) {
				redirectBuffer.append(infoPara).append("&");
			}
		}
		String service;
		try {
			//URL转码
			service = URLEncoder.encode(auth.getService(), "UTF-8");
			redirectBuffer.append("service=").append(service);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("build redirect url["+redirectBuffer.toString()+"]");
		}
		return redirectBuffer.toString();
	}
	
	/**
	 * 验证鉴权对象的合法性
	 * 
	 * @param auth
	 * @return
	 * @throws EucParserException
	 */
	protected boolean verify(EucAuth auth,String authUrl,RequestInfo info)throws EucParserException{

		if(auth==null){
			throw new EucParserException("auth Parameter is null");
		}
		String service = auth.getService();
	    if(!verify(service)){//URL非法
			throw new EucParserException("service ["+service+"] parameter of auth object is Illegal");
		}
		//鉴权地址
		if(!verify(authUrl)){//URL非法
			throw new EucParserException("authUrl ["+authUrl+"] parameter of auth object is Illegal");
		}
		if(auth.isGateWay()&&auth.isRenew()){
			throw new EucParserException("geteWay parameter and renew parameter of auth object can not be true at the same time ");
		}
		String wver = auth.getWver();
		if(wver==null||"".equals(wver)){
			if(LOG.isInfoEnabled()) {
				LOG.info("wver parameter of auth object is ["+wver+"],so user center will return default template");
			}
		}
		return true;
	}
	
	/**
	 * 登录鉴权
	 * 
	 * @param request
	 * @param response
	 * @param auth
	 *     鉴权信息 
	 * @param authUrl
	 *     鉴权地址
	 */
	public void auth(HttpServletRequest request,
			HttpServletResponse response,EucAuth auth,String authUrl,RequestInfo info)throws EucParserException{
		//TODO 校验鉴权对象
		verify(auth,authUrl,info);
		//TODO 生成重定向地址
		try {
			response.sendRedirect(buildRedirect(auth,authUrl,info));
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
