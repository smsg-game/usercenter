package com.easou.usercenter.web.api;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.easou.common.api.ClientConfig;
import com.easou.common.api.JBean;
import com.easou.common.api.JDesc;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.Way;
import com.easou.common.util.MD5Util;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucPrivilegeService;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api/")
public class ApiUpMessageController extends AbstractAPIController {
	
	private static RequestInfo info = new RequestInfo();
	
	static {
		info.setSource("30");
	}

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private EucPrivilegeService eucPrivilegeService;
	
	@RequestMapping("upRegist")
	@ResponseBody
	public String upMsgRegist(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		String mobile = request.getParameter("mobile");
		String passwd = request.getParameter("content");
		String appId = ClientConfig.getProperty("appId");
		String ipAddress = request.getRemoteHost();
		if(log.isDebugEnabled()) {
			log.debug("mobile=" + mobile + " content=" + passwd);
		}
		try {
			if(!eucPrivilegeService.isPrivilege(ipAddress)) {
				log.warn("[上行注册]非法请求ip:" + ipAddress);
				return JSON.toJSONString(JSonBeanFactory.getDefaultNotAuthBean());
			}
			JBean jbean = JSonBeanFactory.getDefaultBean();
			JDesc jdesc = new JDesc(); // 错误描述
			if (!FormUserValidator.checkMobile(mobile)) {
				BizLogUtil.registLog(info, "", null, Way.SMS, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_MSISDN_ILLEGAL, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, "", appId);
				return JSON.toJSONString(busiErrorBean(jbean, jdesc, "1",
						"手机号不合法"));
			}
			EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
			if (null != eucUser) {
				SendSmsHelper.sendSms(mobile, "梵町用户注册失败,失败原因:该手机号已注册,请用该手机号直接登录");
				BizLogUtil.registLog(info, "", null, Way.SMS, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_MSISDN_EXIST, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, "", appId);
				return JSON.toJSONString(busiErrorBean(jbean, jdesc, "2",
						"手机号已注册"));
			}
			String s = FormUserValidator.checkPassword(passwd);
			if (!"".equals(s)) {
				jbean = busiErrorBean(jbean, jdesc, "3", s);
				SendSmsHelper.sendSms(mobile, "梵町用户注册失败,失败原因:" + s);
				BizLogUtil.registLog(info, "", null, Way.SMS, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_MSISDN_PASS_ERR, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, "", appId);
				return JSON.toJSONString(jbean);
			}
			EucUser user = eucUserService.insertUserForMobileRegist(mobile, MD5Util.md5(passwd), null);
			if(null!=user) {
				SendSmsHelper.sendSms(mobile, "恭喜成功注册梵町用户, 登录 http://sso.fantingame.com 完善个人信息");
				BizLogUtil.registLog(info, user.getName(), user.getId(), Way.SMS, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS, "", LogConstant.REGIS_RESULT_SUCCESS, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, "", appId);
				return JSON.toJSONString(jbean);
			} else {
				SendSmsHelper.sendSms(mobile, "梵町用户注册失败,失败原因:系统繁忙,请稍后重试");
				BizLogUtil.registLog(info, "", null, Way.SMS, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "", LogConstant.REGIS_RESULT_EXCEPTION, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, "", appId);
				return JSON.toJSONString(JSonBeanFactory.getDefaultErrorBean());
			}
		} catch (Exception e) {
			log.error(e,e);
			SendSmsHelper.sendSms(mobile, "梵町用户注册失败,失败原因:系统繁忙,请稍后重试");
			BizLogUtil.registLog(info, "", null, Way.SMS, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",  LogConstant.REGIS_RESULT_EXCEPTION, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, "", appId);
			return JSON.toJSONString(JSonBeanFactory.getDefaultErrorBean());
		}
	}
}