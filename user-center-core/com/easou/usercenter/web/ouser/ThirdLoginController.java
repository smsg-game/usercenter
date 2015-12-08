package com.easou.usercenter.web.ouser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weibo4j.util.WeiboConfig;

import com.easou.common.constant.OUserConstant;
import com.easou.usercenter.service.impl.UrlTransService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.BaseController;

/**
 * 第三方登录控制器
 * 
 * @author damon
 * @since 2012.06.07
 * @version 1.0
 * 
 */
@Controller
@RequestMapping("/")
public class ThirdLoginController extends BaseController {

	private static Logger log = Logger.getLogger(ThirdLoginController.class);

	@Autowired
	private UrlTransService urlTransService;

	/**
	 * 重定向到新浪登录页面 重定向前对应用地址service进行加密,加密后的参数名为enService
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("oRedirect")
	@ResponseBody
	public String execute(final HttpServletRequest request,
			final HttpServletResponse response) {
		
		try {
			String type = request.getParameter("t");
			String qryStr = removeTparam(request.getQueryString());
			String redirectUri = "";
			if (null != qryStr) {
				redirectUri = urlTransService.saveUrl(qryStr);
			}
			// 根据sessionId随机生成stat
			String stat = randStat(request, response);
			if (OUserConstant.SINA_TYPE.equals(type) || OUserConstant.SINA_TYPE_WAP1.equals(type)) {
				// 新浪微博地址
				response.sendRedirect(getSinaRedirect(redirectUri, stat, type));
			} else if (OUserConstant.TQQ_TYPE.equals(type) || OUserConstant.TQQ_TYPE_WAP1.equals(type)) {
				// 腾讯微博地址
				response.sendRedirect(getTqqRedirect(redirectUri, stat, type));
			} else if (OUserConstant.QQ_TYPE.equals(type) || OUserConstant.QQ_TYPE_WAP1.equals(type)) {
				// QQ社区地址
				response.sendRedirect(getQQRedirect(redirectUri, stat, type));
			} else if (OUserConstant.RENREN_TYPE.equals(type) || OUserConstant.RENREN_TYPE_WAP1.equals(type)) {
				// 人人网地址
				response.sendRedirect(getRenrenRedirect(redirectUri, stat, type));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String removeTparam(String qryString) {
		if (null == qryString)
			return "";
		String[] params = qryString.split("&");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			// 参数中包含t和renew去掉
			if (!params[i].startsWith("t=")) {
				if (i > 0) {
					sb.append("&").append(params[i]);
				} else {
					sb.append(params[i]);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 新浪
	 * 
	 * @param redirectUri
	 * @return
	 */
	private String getSinaRedirect(final String redirectUri, final String state, final String type) {
		try {
			String reUrl = URLEncoder.encode(
					WeiboConfig.getValue("redirect_URI")		
					+ "?"+ OUserConstant.ENCRYPT_SERVICE_TAG + "="
							+ redirectUri, "utf-8");
			String display = "mobile";
			if(OUserConstant.SINA_TYPE_WAP1.equals(type)) {
				display = "wap";
			}
		String url = "https://api.weibo.com/oauth2/authorize?response_type=code&client_id="
				+ WeiboConfig.getValue("client_ID")
				+ "&display=" + display + "&redirect_uri="
				+ reUrl+ "&state=" + state;
		log.debug(url);
		return url;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 腾讯
	 * 
	 * @param redirectUri
	 * @return
	 */
	private String getTqqRedirect(final String redirectUri,String state, final String type) {
		String url = "https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id="
				+ WeiboConfig.getValue(OUserConstant.TQQ_CLIENT_ID)
				+ "&response_type=code&wap=2&redirect_uri="
				+ WeiboConfig.getValue(OUserConstant.TQQ_REDIRECT_URI)
				+ "?"
				+ OUserConstant.ENCRYPT_SERVICE_TAG + "=" + redirectUri+"&state="+state;
		log.debug(url);
		return url;
	}

	/**
	 * 生成人人网重定向地址
	 * 
	 * @param redirectUri
	 * @return
	 */
	private String getRenrenRedirect(final String redirectUri, final String state, final String type) {
		try {
			String reUrl = URLEncoder.encode(
					"http://sso.fantingame.com/loginRenren?"
							+ OUserConstant.ENCRYPT_SERVICE_TAG + "="
							+ redirectUri , "utf-8");
			String url = WeiboConfig
					.getValue(OUserConstant.RENREN_OAUTH_AUTHORIZE_URL)
					+ "?client_id="
					+ WeiboConfig.getValue(OUserConstant.RENREN_CLIENT_ID)
					+ "&response_type=code&redirect_uri=" + reUrl+ "&state=" + state;
			log.debug(url);
			return url;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * QQ社区
	 * 
	 * @param redirectUri
	 * @return
	 */
	private String getQQRedirect(final String redirectUri, final String state, final String type) {
		try {
			String reUrl = URLEncoder.encode(
					WeiboConfig.getValue(OUserConstant.QQ_REDIRECT_URI) + "?"
							+ OUserConstant.ENCRYPT_SERVICE_TAG + "="
							+ redirectUri + "&state=" + state, "utf-8");
			String url = WeiboConfig
					.getValue(OUserConstant.QQ_OAUTH_AUTHORIZE_URL)
					+ "?client_id="
					+ WeiboConfig.getValue(OUserConstant.QQ_CLIENT_ID)
					+ "&response_type=code&display=mobile&redirect_uri=" + reUrl;
			log.debug(url);
			return url;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 随机生成验证参数
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected String randStat(final HttpServletRequest request,
			final HttpServletResponse response) {
		String state = request.getSession().getId()
				+ (int) (Math.random() * 10000);
		// 保存到session中
		request.getSession().setAttribute("state", state);
		return state;
	}
}
