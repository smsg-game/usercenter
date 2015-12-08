package com.easou.usercenter.interceptor;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.easou.common.constant.Server;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.interceptor.constant.PermissionsConstant;

/**
 * 控制访问权限
 * 
 * @author damon
 * @since 2012.05.07
 * 
 */
public class PermissionsInterceptorAdapter extends HandlerInterceptorAdapter {

	private String SERVER_TYPE = "server.type";
	private String REDIRECT_DOMAIN = "domain.write";
	private String REMOVED_PARAMETERS = "permissions.removed.parameters";

	protected final Log LOG = LogFactory
			.getLog(PermissionsInterceptorAdapter.class);

	// 重定向域名
	private String redirectDomain;
	// 服务器类型
	private String serverType;
	// 剔除参数
	private String removedParameters;

	public String getRemovedParameters() {
		return removedParameters;
	}

	public void setRemovedParameters(String removedParameters) {
		this.removedParameters = removedParameters;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 初始化
		this.init();
		String uri = request.getRequestURI();
		if (LOG.isDebugEnabled()) {
			LOG.debug("the request uri[" + uri + "],server type[" + serverType
					+ "]");
		}
		if (isDxServer()) {// 电信服务器，判断请求是否需要写的权限
			if (uri != null && !"".equals(uri)) {
				for (String writeUri : PermissionsConstant.WRITE_PERMISSIONS_REQUEST) {
					if (writeUri.equals(uri)) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("the request uri[" + uri
									+ "] is must write permission");
						}
						if (!StringUtil.isEmpty(redirectDomain)) {// 重定向到可写地址
							if (redirectDomain.indexOf(request.getServerName()) == -1)
								response.sendRedirect(buildOriginalURL(request));
						} else {// 跳到错误提示页
							    request.getRequestDispatcher("404").forward(request, response);
						}

					}
				}
			}
		}
		return true;

	}

	/**
	 * 判断是否是电信服务器
	 * 
	 * @return
	 */
	public boolean isDxServer() {
		if (serverType != null
				&& Server.DIANXIN.type.equalsIgnoreCase(serverType)) {
			return true;
		}
		return false;
	}

	/**
	 * 初始化数据
	 */
	public void init() {
		if (StringUtil.isEmpty(redirectDomain)) {
			// 读配置
			redirectDomain = SSOConfig.getProperty(REDIRECT_DOMAIN);
		}
		if (StringUtil.isEmpty(serverType)) {
			// 读配置
			serverType = SSOConfig.getProperty(SERVER_TYPE);
		}
		if (StringUtil.isEmpty(removedParameters)) {
			removedParameters = SSOConfig.getProperty(REMOVED_PARAMETERS);
		}
	}

	/**
	 * 生成重定向URL地址
	 * 
	 * @param request
	 * @return
	 */
	private String buildOriginalURL(HttpServletRequest request) {
		StringBuffer originalURL = new StringBuffer(redirectDomain).append(request.getRequestURI());
		Map parameters = request.getParameterMap();
		if (parameters != null && parameters.size() > 0) {
			originalURL.append("?");
			for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				// 是否剔除
				boolean isNotRemoved = true;
				if (!StringUtil.isEmpty(removedParameters)) {
					String[] reParameters = removedParameters.split(",");
					if (reParameters != null) {
						for (String para : reParameters) {
							if (key.equals(para)) {// 找到剔除参数
								isNotRemoved = false;
								break;
							}
						}
					}
				}
				if (isNotRemoved) {
					String[] values = (String[]) parameters.get(key);
					for (int i = 0; i < values.length; i++) {
						if(i!=values.length-1){
						originalURL.append(key).append("=").append(values[i])
								.append("&");
						}else{
							originalURL.append(key).append("=").append(values[i]);
						}
					}
				}
			}
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("redirect URL["+originalURL.toString()+"]");
		}
		return originalURL.toString();
	}

	public String getRedirectDomain() {
		return redirectDomain;
	}

	public void setRedirectDomain(String redirectDomain) {
		this.redirectDomain = redirectDomain;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
}
