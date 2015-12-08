package com.easou.usercenter.web.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.util.MD5Util;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api/")
public class AppRegistController extends AbstractAPIController {
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestVericode")
	@ResponseBody
	public String requestVericode(final HttpServletRequest request, final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				RequestInfo info = fetchRequestInfo(request);
				
				JDesc jdesc = new JDesc(); // 错误描述
				String mobile = getRequestBean().getBody().getString("mobile");
				String passwd = getRequestBean().getBody().getString("password");
				boolean isSendMsg = isSendMsg(getRequestBean().getBody());
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, jdesc, "1", "手机号不合法");
				}
				if (null != passwd) {
					String s = FormUserValidator.checkPassword(passwd);
					if (!"".equals(s)) {
						BizLogUtil.registLog(info, "", null, Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_VERIFICATION_FAILURE, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
						return busiErrorBean(jbean, jdesc, "2", s);
					}
				}
				
				EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
				if (null != eucUser) {
					return busiErrorBean(jbean, jdesc, "3", "该手机号已注册，可直接登录");
				}
				String veriCode = mobileService.getVeriCodeByMobile(mobile);
				if (null == veriCode) {
					// 未有验证码或验证码已超时
					veriCode = mobileService.setVeriCodeByMobile(mobile);
					if (isSendMsg) {
						// 需要发短信
						String message = "欢迎注册梵町用户,您的验证码是" + veriCode;
						String messageLog = "欢迎注册梵町用户,您的验证码是******";
						SendSmsHelper.sendSms(info.getUid(), mobile, message, messageLog, info.getSource(), info.getEsid(), SMSType.REGISTER);
					}
				} else {
					return busiErrorBean(jbean, jdesc, "4", "验证码已发送,请稍后再试");
				}
				JBody jbody = new JBody();
				jbody.putContent("veriCode", veriCode);
				jbean.setBody(jbody);

				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	@RequestMapping("regByVericode")
	@ResponseBody
	public String regByVericode(final HttpServletRequest request, final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				RequestInfo info = fetchRequestInfo(request);
				String mobile = getRequestBean().getBody().getString("mobile");
				String passwd = getRequestBean().getBody().getString("password");
				String veriCode = getRequestBean().getBody().getString("veriCode");
				String logMobile = mobile != null ? new String(mobile) : "";

				if (!FormUserValidator.checkMobile(mobile)) {
					BizLogUtil.registLog(info, logMobile, null, Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_MSISDN_ILLEGAL, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					log.debug("手机号不合法");
					return busiErrorBean(jbean, jdesc, "1", "手机号不合法");
				}
				String s = FormUserValidator.checkPassword(passwd);
				if (!"".equals(s)) {
					BizLogUtil.registLog(info, "", null, Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_VERIFICATION_FAILURE, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					return busiErrorBean(jbean, jdesc, "2", s);
				}
				String eucVeriCode = mobileService.getVeriCodeByMobile(mobile);
				if (null != eucVeriCode) {
					if (!eucVeriCode.equalsIgnoreCase(veriCode))
						return busiErrorBean(jbean, jdesc, "4", "验证码不正确");
				} else {
					return busiErrorBean(jbean, jdesc, "3", "验证码已失效");
				}
				EucUser user = eucUserService.queryUserInfoByMobile(mobile);
				if (null != user) {
					BizLogUtil.registLog(info, logMobile, null, Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_MSISDN_EXIST, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					log.debug("该手机号已被注册");
					return busiErrorBean(jbean, jdesc, "5", "该手机号已注册,可直接登录");
				} else {
					EucUser eucUser = eucUserService.insertUserForMobileRegist(mobile, MD5Util.md5(passwd), qn);
					if (null != eucUser) {
						if (log.isDebugEnabled()) {
							log.debug(mobile + "注册成功, pass:" + passwd);
						}
						JBody jbody = new JBody();
						jbody.putContent("user", transJUser(eucUser, new ExpJUser()));
						jbean.setBody(jbody); // 用户注册成功，返回用户实体
						BizLogUtil.registLog(info, eucUser.getName(), eucUser.getId(), Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS, "", LogConstant.REGIS_RESULT_SUCCESS, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					} else {
						log.warn("注册失败: " + mobile);
						BizLogUtil.registLog(info, logMobile, null, Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_EXCEPTION, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
						return busiErrorBean(jbean, jdesc, "6", "注册失败");
					}
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}
}
