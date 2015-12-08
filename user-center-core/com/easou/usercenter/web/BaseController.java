package com.easou.usercenter.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.FastHashMap;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.util.MD5Util;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.AjaxBaseController;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;
import com.easou.usercenter.util.HttpClientUtil;

public class BaseController extends AjaxBaseController {

	protected Logger log = Logger.getLogger(getClass());

	protected final static String ERROR = "errors";

	protected JUser transJUser(EucUser eucUser, JUser jUser) {
		if (jUser == null) {
			jUser = new JUser();
		}
		// JUser user = new JUser();
		if (null != eucUser) {
			BeanUtils.copyProperties(eucUser, jUser);
		}
		return jUser;
	}

	protected String getQueryString(final HttpServletRequest request) {
		if (null != request.getQueryString()) {
			return "?" + request.getQueryString();
		} else {
			return "";
		}
	}

	/**
	 * 计算短链接密文，使用短链接服务必须提供密文
	 * 
	 * @param content
	 * @return
	 */
	private static String calShortUrlSecert(String content) {
		return MD5Util.md5(content + SSOConfig.getSecertKey());
	}

	protected String getShortUrl(final HttpServletRequest request, String longUrl) {
		log.debug("长链接为 " + longUrl);
		try {
			String encodeUrl = URLEncoder.encode(longUrl, "UTF-8");
			String content = "lu=" + encodeUrl + "&srt=" + calShortUrlSecert(longUrl);
			String shortUrl = HttpClientUtil.executeUrl(SSOConfig.getSulServer(), content);
			if (null == shortUrl) {
				return "";
			} else {
				return shortUrl;
			}
		} catch (Exception e) {
			log.error(e, e);
			return "";
		}
	}

	/**
	 * 获取服务路径
	 * 
	 * @param request
	 * @param action
	 *          要跳转url中的action
	 * @param parameters
	 *          要跳转url的参数列表
	 * @return
	 */
	protected String getPath(final HttpServletRequest request, String action, Map paramMap) {
		StringBuffer sb = new StringBuffer("http://");
		sb.append(request.getServerName());
		int port = request.getServerPort();
		if (80 != port) {
			sb.append(":").append(port);
		}
		if (!"/".equals(request.getContextPath())) {
			sb.append(request.getContextPath());
		}
		sb.append(action);
		if (null == paramMap)
			return sb.toString();
		if (paramMap.size() > 0) {
			sb.append("?");
		}
		for (Iterator iterator = paramMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sb.append(key).append("=");
			sb.append((String) paramMap.get(key));
			if (iterator.hasNext()) {
				sb.append("&");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取服务路径(只获取queryString)
	 * 
	 * @param request
	 * @param action
	 * @return
	 */
	protected String getQueryPath(final HttpServletRequest request, String action) {
		StringBuffer sb = new StringBuffer("http://");
		sb.append(request.getServerName());
		int port = request.getServerPort();
		if (80 != port) {
			sb.append(":").append(port);
		}
		if (!"/".equals(request.getContextPath())) {
			sb.append(request.getContextPath());
		}
		sb.append(action);
		sb.append(getQueryString(request));
		log.debug(sb.toString());
		return sb.toString();
	}

	/**
	 * 获取服务路径(只获取queryString)
	 * 
	 * @param request
	 * @param action
	 * @return
	 */
	public String getQueryParam(final Map paramMap) {
		if (null == paramMap) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		if (paramMap.size() > 0) {
			sb.append("?");
		} else {
			return "";
		}
		for (Iterator iterator = paramMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sb.append(key).append("=").append(paramMap.get(key));
			if (iterator.hasNext()) {
				sb.append("&");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(sb.toString());
		}
		return sb.toString();
	}

	protected Map getRequestMap(final HttpServletRequest request) {
		Map map = new FastHashMap();
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			map.put(key, request.getParameter(key));
		}
		return map;
	}

	/**
	 * 生成URL
	 * 
	 * @param url
	 * @param paraMap
	 * @return
	 */
	protected String buildURL(String url, Map paramMap) {
		if (paramMap == null || paramMap.isEmpty()) {
			return url;
		}
		if (StringUtil.isEmpty(url)) {
			return url;
		}
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = paramMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sb.append(key).append("=").append(paramMap.get(key));
			if (iterator.hasNext()) {
				sb.append("&");
			}
		}
		String paraStr = sb.toString();
		StringBuffer buffer = new StringBuffer();

		buffer.append(url);
		if (url.indexOf("?") != -1) {
			if (url.indexOf("?") == url.length() - 1) {
				buffer.append(paraStr);
			} else {
				buffer.append("&").append(paraStr);
			}
		} else {
			buffer.append("?").append(paraStr);
		}
		return buffer.toString();
	}

	protected RequestInfo fetchRequestInfo(final HttpServletRequest request) {
		RequestInfo info = new RequestInfo();
		info.setUid(ConditionUtil.getUid(request));
		info.setEsid(ConditionUtil.getEsid(request));
		info.setSource(ConditionUtil.getChannel(request));
		info.setAppId(ConditionUtil.getAppId(request));
		info.setQn(ConditionUtil.getQn(request));
		info.setAgent(request.getParameter(Constant.AGENT_TAG) != null ? request.getParameter(Constant.AGENT_TAG) : "");

		return info;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		BaseController base = new BaseController();
		System.out.println(base.buildURL("http://sso.fantingame.com", null));

		Map map = new HashMap();
		System.out.println(base.buildURL("http://sso.fantingame.com", map));
		map.put("a", 1);
		map.put("b", "s");
		System.out.println(base.buildURL("http://sso.fantingame.com", map));
		System.out.println(base.buildURL("http://sso.fantingame.com?", map));
		System.out.println(base.buildURL("http://sso.fantingame.com?t=4", map));
		System.out.println(base.buildURL("http://sso.fantingame.com?t=4&", map));
	}
}