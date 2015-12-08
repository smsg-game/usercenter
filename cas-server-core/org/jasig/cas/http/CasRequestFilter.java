package org.jasig.cas.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.easou.common.util.StringUtil;
import com.easou.usercenter.util.ConditionUtil;

/**
 * cas http请求对像封装类
 * 
 * @author river
 * 
 */
public class CasRequestFilter implements Filter {

	protected Logger log = Logger.getLogger(getClass());

	private static String excludeUri = null;

	public static final String SESSION_ID_NAME = "esid";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest ser = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String esid = null;

		if (null != excludeUri && ser.getServletPath().matches(excludeUri)) {
			// 接口或者图片等进行过滤
			String queryString = ser.getQueryString();
			if (queryString != null && !"".equals(queryString.trim()) && !queryString.contains("&#")) {
				String[] queryArr = queryString.split("&amp;|&");
				for (int i = 0; i < queryArr.length; i++) {
					String[] valueArr = queryArr[i].split("=");
					if (valueArr.length == 2) {
						if ("esid".equals(valueArr[0])) {
							esid = valueArr[1];
							break;
						}
					}
				}
			}
			
			if (null != esid) {
				res.setHeader(SESSION_ID_NAME, esid);
			}
			
			chain.doFilter(ser, res);
		} else {
			esid = (String) request.getParameter(SESSION_ID_NAME);
			if (null == esid) {
				esid = (String) request.getAttribute(SESSION_ID_NAME);
			}
			if (null != esid) {
				res.setHeader(SESSION_ID_NAME, esid);
			}
			
			CasHttpRequest casRequest = new CasHttpRequest(ser);
			CasHttpSession session = (CasHttpSession) casRequest.getSession();
			String appAgent = ConditionUtil.getAppAgent(casRequest);
			if (null == appAgent || "".equals(appAgent)) {
				appAgent = (String) session.getAttribute("appAgent");
			}
			if (log.isDebugEnabled()) {
				log.debug(ser.getRequestURI() + " ++++++++appAgent+++++++ " + appAgent);
			}
			if (null != appAgent) {
				ser.setAttribute("appAgent", appAgent);
				session.setAttribute("appAgent", appAgent);
			}
			chain.doFilter(casRequest, res);
			
			// 将数据添加到缓存
			session.saveToCache();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String temp = filterConfig.getInitParameter("excludeUri");
		if (null != temp && !"".equals(temp)) {
			excludeUri = temp;
		}
	}
}