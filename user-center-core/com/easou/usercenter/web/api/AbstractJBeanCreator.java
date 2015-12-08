package com.easou.usercenter.web.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.easou.cas.auth.EucToken;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.service.AuthVerifyService;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;

abstract class AbstractJBeanCreator {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private JBean requestBean;
	/** 验证信息 */
	private AuthToken authToken;
	/** 校验方式 */
	private int checkMode;
	/** TOKEN校验 */
	protected static int CHECK_BY_TOKEN = 1;
	/** 加密鉴权验证 */
	protected static int CHECK_BY_CODE = 2;

	protected String qn = "";
	protected String appId = "";

	protected Logger log = Logger.getLogger(getClass());
	// private static EucParser eup = new EucParser(SSOConfig.getSecertKey(),
	// "1.0", "euc");

	private static final long NO_ID_DEFAULT = -1;

	@Autowired
	private AuthVerifyService authVerifyService;

	public AuthVerifyService getAuthVerifyService() {
		return authVerifyService;
	}

	public void setAuthVerifyService(AuthVerifyService authVerifyService) {
		this.authVerifyService = authVerifyService;
	}

	public AbstractJBeanCreator(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public int getCheckMode() {
		return checkMode;
	}

	private void setCheckMode(int checkMode) {
		this.checkMode = checkMode;
	}

	public AuthToken getAuthToken() {
		return authToken;
	}

	private void setAuthToken(AuthToken authToken) {
		this.authToken = authToken;
	}

	/**
	 * 通过request获取请求json串
	 * 
	 * @return
	 * @throws IOException
	 * @throws IOException
	 */
	private String getRequestString() throws IOException {

		// 为兼容以前客户端包
		request.setCharacterEncoding("UTF8");
		InputStream is = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			is = request.getInputStream();
			if (null == is) {
				return "";
			}
			reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
				if (null != line) {
					sb.append("\n");
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (null != is)
				is.close();
			if (null != reader)
				reader.close();
		}
		// 通过post传递上来的JSON参数
		if (sb.indexOf("json=") == 0) {
			String str = URLDecoder.decode(sb.toString(), "UTF-8");
			log.info(str);
			return str.substring(5);
		}
		if (log.isDebugEnabled()) {
			log.info(sb.toString());
		}
		return sb.toString();
	}

	/**
	 * 转换json串为JBean
	 * 
	 * @throws IOException
	 */
	private void transRequestBean() throws IOException {
		requestBean = EucService.parserJsonResult(getRequestString());
	}

	/**
	 * 生成最终返回的json串，给控制器加入http body
	 * 
	 * @return
	 */
	public String genReturnJson() {
		String json = "{}";
		try {
			transRequestBean();
			if (null == requestBean) {
				json = JSON.toJSONString(JSonBeanFactory.getDefaultErrorBean());
				response.setContentLength(json.length());
				return json;
			}
			JHead head = getRequestBean().getHead();
			if (null != head) {
				if (head.getQn() != null)
					qn = head.getQn();
				if (head.getAppId() != null)
					appId = head.getAppId();
			}
			// 提供了两种不同的校验方式，一种是token校验，另一种是加密校验
			if (isAuth(getRequestBean().getBody())) {// TOKEN鉴权校验
				EucToken token = getRequestBean().getBody().getObject("token", EucToken.class);
				// TOKEN校验方式
				this.setCheckMode(CHECK_BY_TOKEN);
				long eaId = ServiceFactory.getInstance().getAuthVerifyService().verify(token);
				if (eaId == NO_ID_DEFAULT) {// 无效验证信息
					json = JSON.toJSONString(JSonBeanFactory.getDefaultNotAuthBean());
					response.setContentLength(json.length());
				} else {// TOKEN验证
					AuthToken authToken = new AuthToken(eaId, token);
					this.setAuthToken(authToken);
					JBean jbean = getJBean();
					fixJBean(jbean);
					json = JSON.toJSONString(jbean);
					response.setContentLength(json.length());
				}

			} else {// 加密鉴权验证
				// 加密鉴权验证
				this.setCheckMode(CHECK_BY_CODE);
				// String veriCode = eup.getVeriCode(requestBean.getBody()
				// .toJSONString(), requestBean.getHead().getFlowCode());
				// if (!veriCode.equals(requestBean.getHead().getVeriCode())) {
				// log.info("计算验证码：" + veriCode + " 请求Bean验证码："
				// + requestBean.getHead().getVeriCode());
				// json = JSON.toJSONString(JSonBeanFactory
				// .getDefaultNotAuthBean());
				// response.setContentLength(json.length());
				// } else {
				JBean jbean = getJBean();
				fixJBean(jbean);
				json = JSON.toJSONString(jbean);
				response.setContentLength(json.length());
				// }
			}
		} catch (Exception e) {
			log.error(e, e);
			json = JSON.toJSONString(JSonBeanFactory.getDefaultErrorBean());
			response.setContentLength(json.length());
			response.setContentType("text/plain;charset=UTF-8");
		}
		if (log.isDebugEnabled()) {
			log.debug(json);
		}
		return json;
	}

	/**
	 * 实现方法，通过实现该方法生成要返回的业务bean
	 * 
	 * @return
	 */
	public abstract JBean getJBean() throws Exception;

	/**
	 * 获取请求bean
	 * 
	 * @return
	 */
	protected JBean getRequestBean() {
		return requestBean;
	}

	/**
	 * 判断是否需要鉴权
	 * 
	 * @param token
	 *          鉴权信息
	 * @param isAuth
	 *          是否需要鉴权
	 * @return
	 */
	public boolean isAuth(JBody jBody) {
		String token = jBody.getString("token");
		String isAuth = jBody.getString("isAuth");
		if (token != null && isAuth != null && Boolean.valueOf(isAuth)) {// 需要鉴权
			return true;
		}
		return false;
	}

	private void fixJBean(JBean jbean) {
		try {
			JHead head = jbean.getHead();
			if (!"0".equals(head.getRet()) && null == jbean.getDesc()) {
				jbean.setDesc(new JDesc());
			}
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	protected boolean isSendMsg(JBody jBody) {
		String isSend = jBody.getString("sendMsg");
		if (isSend != null && Boolean.valueOf(isSend)) {
			return true;
		}
		return false;
	}
}
