package com.easou.usercenter.web.api2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JHead;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.OAuthParametric;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.entity.EucPartner;
import com.easou.usercenter.util.HttpUtil;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;

abstract class AbstractOpenBeanCreator {
	
	protected Logger log = Logger.getLogger(getClass());

	private HttpServletRequest request;
	private HttpServletResponse response;
	private JBean requestBean;
	private static AuthParametric<RequestInfo> authPara = new OAuthParametric();
	private final static String CONTENT_TYPE = "text/plain;charset=utf-8";
	protected String qn;			// 推广渠道, 提供给日志输出使用
	protected String appId;		// 应用ID, 提供给日志输出使用
	protected String source;	// 接口访问来源;
	protected String agent;		// 客户端agent
	
	public AbstractOpenBeanCreator(final HttpServletRequest request,
			final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * 从request中获得请求json串, 再转换json串为JBean
	 * 
	 * @throws IOException
	 */
	private void transRequestBean() throws IOException {
		requestBean = EucService.parserJsonResult(HttpUtil.requestToJson(this.request));
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
				response.setContentType(CONTENT_TYPE);
				log.debug("request body is null, return:\n" + json);
				return json;
			}
			JHead head = getRequestBean().getHead();
			if(null!=head) {
				if(head.getQn()!=null)
					qn=head.getQn();
				if(head.getAppId()!=null)
					appId=head.getAppId();
				if(head.getSource()!=null)
					source=head.getSource();
				if(head.getAgent()!=null)
					agent=head.getAgent();
			}
			
			// 根据appId获得密钥
			log.info("Check sign appID=" + requestBean.getHead().getAppId());
			EucPartner partner = null;
			if(requestBean.getHead().getAppId() != null && requestBean.getHead().getAppId().length() > 0) {
				partner = ServiceFactory.getInstance().getEucAppService().queryByAppId(requestBean.getHead().getAppId());
			}
			
			// TODO 因为有一些垃圾数据的存在， 所以这么写
			if(partner == null){
				log.warn("The appID="+ requestBean.getHead().getAppId() +" is invalid!");
				log.warn("Now, use partnerId=" + requestBean.getHead().getPartnerId());
				partner = ServiceFactory.getInstance().getEucAppService().queryByPartnerId(requestBean.getHead().getPartnerId());
			}
			
			if(null==partner) {
				JBean retBean = JSonBeanFactory.getDefaultNotAuthBean();
				retBean.getHead().setFlowCode(requestBean.getHead().getFlowCode());
				json = JSON.toJSONString(retBean);
				response.setContentLength(json.length());
				response.setContentType(CONTENT_TYPE);
				log.debug("partner not exist, partnerId=" + requestBean.getHead().getPartnerId() +" return:\n" +  json);
				return json;
			}
			String veriCode = authPara.getSign(requestBean.getBody(),partner.getSecert());
			if (!veriCode.equals(requestBean.getHead().getSign())) {
				log.warn("计算验证码：" + veriCode + " 请求Bean验证码："
						+ requestBean.getHead().getSign() + " 请求者："
						+ requestBean.getHead().getAppId());
				JBean retBean = JSonBeanFactory.getDefaultNotAuthBean();
				retBean.getHead().setFlowCode(requestBean.getHead().getFlowCode());
				json = JSON.toJSONString(retBean);
				response.setContentLength(json.length());
				response.setContentType(CONTENT_TYPE);
			} else {
				// partnerId验签成功，进入业务处理
				JBean jbean = buildJBean(JSonBeanFactory.getDefaultBean());
				jbean.getHead().setFlowCode(requestBean.getHead().getFlowCode());
				json = JSON.toJSONString(jbean);
				response.setContentLength(json.length());
				response.setContentType(CONTENT_TYPE);
			}
		} catch (Exception e) {
			log.error(e, e);
			json = JSON.toJSONString(JSonBeanFactory.getDefaultErrorBean());
			response.setContentLength(json.length());
			response.setContentType(CONTENT_TYPE);
		}
		log.debug(json);
		return json;
	}

	/**
	 * 获取请求bean
	 * 
	 * @return
	 */
	protected JBean getRequestBean() {
		return requestBean;
	}
	
	/**
	 * 实现方法，通过实现该方法生成要返回的业务bean
	 * 
	 * @return
	 */
	protected abstract JBean buildJBean(JBean jbean) throws Exception;
}
