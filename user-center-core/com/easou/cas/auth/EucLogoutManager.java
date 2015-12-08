package com.easou.cas.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;

/**
 * 登出处理类
 * 
 * @author damon
 * @since 2012.06.28
 * @version 1.0
 * 
 */
public class EucLogoutManager extends EucAbstractManager{
	
	protected static final Log LOG = LogFactory.getLog(EucLogoutManager.class);
	/**
	 * 生成鉴权地址
	 * 
	 * @param auth
	 * @return
	 */
	protected String buildRedirect(String logoutUrl,String service,RequestInfo info){
		StringBuffer redirectBuffer = new StringBuffer();
		redirectBuffer.append(logoutUrl).append("?");
		if(info!=null){
			redirectBuffer.append(info.paraToString()).append("&");
		}
		try {
			//URL转码
			String enService = URLEncoder.encode(service, "UTF-8");
			redirectBuffer.append("service=").append(enService);
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
	protected boolean verify(String logoutUrl,String service)throws EucParserException{

	    if(!verify(service)){//URL非法
			throw new EucParserException("service ["+service+"] parameter of auth object is Illegal");
		}
		//鉴权地址
		if(!verify(logoutUrl)){//URL非法
			throw new EucParserException("authUrl ["+logoutUrl+"] parameter of auth object is Illegal");
		}
		return true;
	}
	

	/**
	 * 登出
	 * 
	 * @param logoutUrl
	 * @param service
	 */
	public void logout(HttpServletResponse response,String logoutUrl,String service,RequestInfo info)throws EucParserException{
		verify(logoutUrl, service);
	    try {
			response.sendRedirect(buildRedirect(logoutUrl,service,info));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
