package com.easou.usercenter.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.easou.common.constant.SMSType;
import com.easou.common.util.CookieUtil;

/**
 * 条件公共类
 * 
 * @author damon
 * @since 2012.07.31
 * @version 1.0
 *
 */
public class ConditionUtil {
	
	/**
	 * 获取渠道
	 * 
	 * @param jBean
	 * @return
	 */
	/*public static String getChannel(JBean jBean){
		String from = jBean.getHead().getFrom();
		return from!=null?from:"";
	}
	*/
	/**
	 * 
	 * @param jBean
	 * @return
	 */
/*	public static String getEsId(JBean jBean){
		String esid = jBean.getBody().getString("esid");
		return esid!=null?esid:"";
	}
	*/
	/**
	 * 从URL中截取ESID,有多个时截取第一个
	 * 
	 * @param url
	 * @return
	 */
	public static String getEsIdFromUrl(String url){
		if(url==null||"".equals(url)){
			return "";
		}
		String deUrl = url;
		try {
			if(!URLDecoder.decode(url,"utf-8").equals(url)){//decode一次，如果不一致表示有转码过
				deUrl = URLDecoder.decode(url, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int qMark = deUrl.indexOf("?");
		if(qMark<0){//不带任何参数
			return "";
		}
		int eMark = deUrl.indexOf("esid=");
		if(eMark<0){//无esid参数
			return "";
		}
		String paraStr = deUrl.substring(deUrl.indexOf("esid="));
		String[] paras = paraStr.split("&");
		String esid = paras[0].substring(paras[0].indexOf("esid=")+5);		
		//判断是否带参数
		return esid;
	}
	
	/**
	 * 获取esid
	 * 
	 * @param request
	 * @return
	 */
	public static String getEsid(final HttpServletRequest request){
		return request.getParameter(Constant.ESID_TAG)!=null?
				request.getParameter(Constant.ESID_TAG):"";
	}
	
	/**
	 * 获取渠道号
	 * 
	 * @param request
	 * @return
	 */
	public static String getChannel(final HttpServletRequest request){
		return request.getParameter(Constant.CHANNEL_TAG)!=null?
				request.getParameter(Constant.CHANNEL_TAG):"";
	}
	
	/**
	 * 获得uid(cookie中)
	 * @param request
	 * @return
	 */
	public static String getUid(final HttpServletRequest request){
		String uid=request.getParameter(Constant.UID_TAG);
		if(null==uid) {
				Cookie uidCookie = CookieUtil.getCookie(request, Constant.UID_TAG);
				if (uidCookie != null) {
					uid = uidCookie.getValue();
				}
		}
		return uid!=null?uid:"";
	}
	
	/**
	 * 获得App-Agent(从header中获取)
	 * @param request
	 * @return
	 */
	public static String getAppAgent(final HttpServletRequest request) {
		return request.getHeader(Constant.APP_AGENT)!=null?request.getHeader(Constant.APP_AGENT):"";
	}
	
	/**
	 * 获取渠道标识
	 * @param request
	 * @return
	 */
	public static String getQn(final HttpServletRequest request) {
		return request.getParameter(Constant.QN_TAG)!=null?request.getParameter(Constant.QN_TAG):"";
	}
	
	/**
	 * 获得appId参数值
	 * @param request
	 * @return
	 */
	public static String getAppId(final HttpServletRequest request) {
		return request.getParameter(Constant.APPID_TAG)!=null?request.getParameter(Constant.APPID_TAG):"";
	}
	
	/**
	 * 获得gameId参数值
	 * @param request
	 * @return
	 */
	public static String getGameId(final HttpServletRequest request) {
		return request.getParameter(Constant.GAMEID_TAG)!=null?request.getParameter(Constant.GAMEID_TAG):"";
	}
	
	public  static  void main(String[] mrgs){
		String url = "www.baidu.com";
		/*System.out.println(ConditionUtil.getEsIdFromUrl(url));
		url = "www.baidu.com?";
		System.out.println(ConditionUtil.getEsIdFromUrl(url));
		url = "www.baidu.com?a=1";
		System.out.println(ConditionUtil.getEsIdFromUrl(url));

		url = "www.baidu.com?esid=123456";
		System.out.println(ConditionUtil.getEsIdFromUrl(url));
		url = "www.baidu.com?esid=123456&a=2";
		System.out.println(ConditionUtil.getEsIdFromUrl(url));
		url = "www.baidu.com?a=3&esid=123456";
		System.out.println(ConditionUtil.getEsIdFromUrl(url));*/
		url = "www.baidu.com?a=3&esid=123456&esid=883456";
		System.out.println(ConditionUtil.getEsIdFromUrl(url));
		try {
			url = URLEncoder.encode("www.baidu.com?a=3&esid=123456&esid=883456","utf-8");
			System.out.println(ConditionUtil.getEsIdFromUrl(url));
			System.out.println(url);
			System.out.println(URLDecoder.decode("www.baidu.com?a=3&esid=123456&esid=883456","utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(SMSType.BIND);
	}
}
