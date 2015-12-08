package com.easou.cas.authenticateion;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.OUserConstant;
import com.easou.common.constant.Way;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.Constant;
import com.easou.usercenter.util.LogConstant;

/**
 * 新浪用户登陆tgt创建
 * 
 * @author river
 * 
 */
public class ExtCreateTgtAction {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private CentralAuthenticationService centralAuthenticationService;	
	private EucUserService eucUserService;

	public String createTgt(RequestContext context,AccessTokenCredentials credentials,
			MessageContext messageContext) throws Exception {
		ThirdPartUserInfo tinfo = (ThirdPartUserInfo) context.getFlowScope()
				.get(OUserConstant.THIRDPART_INFO_SESSION);

		try {
			EucUser eucUser = null;
			if(null!=tinfo.getEasouId() && !"".equals(tinfo.getEasouId().trim())) {
				eucUser = eucUserService.queryUserInfoById(tinfo.getEasouId());
			}
			
			credentials.setAccToken(tinfo.getAccToken());
			credentials.setThirdId(tinfo.getThirdId());
			credentials.setEasouId(tinfo.getEasouId());
			credentials.setType(tinfo.getType());
			if(null!=eucUser && null!=eucUser.getPasswd()) {
				// 能查到用户密码
				credentials.setPasswd(eucUser.getPasswd());
			}

			// 创建tgt信息
			WebUtils.putTicketGrantingTicketInRequestScope(context,
					this.centralAuthenticationService
							.createTicketGrantingTicket(credentials));
			return "success";
		} catch (Exception ex) {
			RequestInfo info = new RequestInfo();
			info.setSource(context.getFlowScope().getString(Constant.CHANNEL_TAG));
			info.setEsid(context.getFlowScope().getString(Constant.ESID_TAG));
			info.setUid(context.getFlowScope().getString(Constant.UID_TAG));
			info.setAgent( context.getFlowScope().getString(Constant.APP_AGENT));
			LoginType loginType = (LoginType)context.getFlowScope().get(Constant.LOGIN_TYPE);
			String st = WebUtils.getServiceTicketFromRequestScope(context);
			String extType = BizLogUtil.getExtLogLoginType(tinfo.getType());
			// 记录登录异常日志
			BizLogUtil.loginLog(info, tinfo != null ? tinfo.getEasouId() : "",
					"", Way.PAGE, LogConstant.RESULT_FAILURE,
					loginType, st!=null?st:"","",LogConstant.REGIS_RESULT_EXCEPTION, extType, 
					context.getFlowScope().getString(Constant.QN_TAG), context.getFlowScope().getString(Constant.APPID_TAG), null);
			logger.error("创建TGT失败", ex);
			return "error";
		}
	}

	public void setCentralAuthenticationService(
			CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public void setEucUserService(EucUserService eucUserService) {
		this.eucUserService = eucUserService;
	}
}
