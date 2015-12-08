package com.easou.usercenter.web.usermodule;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.json.JsonUtil;
import com.easou.usercenter.ErrorDesc;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormUser;
import com.easou.usercenter.web.form.FormVerify;
import com.easou.usercenter.web.user.ArrestValidation;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
public class MBindMobileController extends MUserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	@RequestMapping(value = "/user/mbindMobile", method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		model.addAttribute("id", av.getUserId());
		model.addAttribute("formUser", new FormUser());
		return "usermodule/phone-binding";
	}

	@RequestMapping(value = "/user/mbindMobileVerify", method = RequestMethod.POST)
	public String verify(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		ErrorDesc errorDesc = new ErrorDesc(0, "");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			errorDesc = new ErrorDesc(1001, "用户未登陆");
			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
			return renderJSONSuccess(response, jsonResult);
		}
		// 渠道
		String channel = ConditionUtil.getChannel(request);
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String mobile = formUser.getMobile();
		model.addAttribute("id", id);
		model.addAttribute("mobile", mobile);
		// 当前用户信息
		String curMobile = eucUserService.queryUserInfoById(id).getMobile();
		if (null == curMobile) {
			curMobile = "";
		}
		if (!FormUserValidator.checkMobile(mobile)) {
			log.debug("手机号不合法");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_ILLEGAL);
			result.reject("notvaildMobile", "您输入的不是一个正确的手机号");
//			return "usermodule/phone-binding";
			errorDesc = new ErrorDesc(1002, "请输入合法手机号");
			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
			return renderJSONSuccess(response, jsonResult);
		}
		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
		if (null != user) {
			log.debug("该手机号已被使用");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_EXIST);
			result.reject("repeatBind", "该手机号已被使用");
//			return "usermodule/phone-binding";
			errorDesc = new ErrorDesc(1003, "该手机号已被使用");
			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
			return renderJSONSuccess(response, jsonResult);
		} else {
			String veriCode = mobileService.getVeriCodeByMobile(mobile);
			if (null == veriCode) {
				errorDesc = new ErrorDesc(1000, "该手机号已发送了验证码");
			}
			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
			return renderJSONSuccess(response, jsonResult);
		}
	}
	
	@RequestMapping(value = "/user/mbindMobile", method = RequestMethod.POST)
	public String bind(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
//		ErrorDesc errorDesc = new ErrorDesc(0, "");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			return "usermodule/phone-binding";
//			errorDesc = new ErrorDesc(1001, "用户未登陆");
//			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
//			return renderJSONSuccess(response, jsonResult);
		}
		// 渠道
		String channel = ConditionUtil.getChannel(request);
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String mobile = formUser.getMobile();
		model.addAttribute("id", id);
		model.addAttribute("mobile", mobile);
		// 当前用户信息
		String curMobile = eucUserService.queryUserInfoById(id).getMobile();
		if (null == curMobile) {
			curMobile = "";
		}
		if (!FormUserValidator.checkMobile(mobile)) {
			log.debug("手机号不合法");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_ILLEGAL);
			result.reject("notvaildMobile", "您输入的不是一个正确的手机号");
			return "usermodule/phone-binding";
//			errorDesc = new ErrorDesc(1002, "请输入合法手机号");
//			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
//			return renderJSONSuccess(response, jsonResult);
		}
		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
		if (null != user) {
			log.debug("该手机号已被使用");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_EXIST);
			result.reject("repeatBind", "该手机号已被使用");
			return "usermodule/phone-binding";
//			errorDesc = new ErrorDesc(1003, "该手机号已被使用");
//			String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
//			return renderJSONSuccess(response, jsonResult);
		} else {
			String veriCode = mobileService.getVeriCodeByMobile(mobile);
			if (null == veriCode) {
				veriCode = mobileService.setVeriCodeByMobile(mobile);
				// 需要发短信
				String message = "验证码: "+veriCode+", 感谢您对梵町的支持!";
				String messageLog = "验证码: xxxx, 感谢您对梵町的支持!";

				SendSmsHelper.sendSms(uid, mobile, message,messageLog, channel, esid,
						SMSType.BIND);
				log.debug("验证码发送成功");
				BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
						Way.PAGE, BindActivity.REQUEST, veriCode,
						LogConstant.RESULT_SUCCESS, av.getBackUrl(), channel,
						esid, LogConstant.BING_MSISDN_PUSH_SMS_SUCCESS);
				return "forward:/user/mbindcfm";
//				String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
//				return renderJSONSuccess(response, jsonResult);
			} else {
				log.debug("该手机号已发送了验证码");
				BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
						Way.PAGE, BindActivity.REQUEST, veriCode,
						LogConstant.RESULT_FAILURE, av.getBackUrl(), channel,
						esid, LogConstant.BING_MSISDN_PUSH_SMS_EXIST);
//				errorDesc = new ErrorDesc(1000, "该手机号已发送了验证码");
//				String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
//				return renderJSONSuccess(response, jsonResult);
				model.addAttribute("formVerify", new FormVerify());
				model.addAttribute("msg", "已向该手机号发送验证码");
				return "usermodule/phone-binding-cfm";
			}
		}
	}
	
	@RequestMapping(value="/user/msendSms",method = RequestMethod.POST)
	public String sendSms(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ErrorDesc errorDesc = null;
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			errorDesc = new ErrorDesc(1001, "你无权操作");
		}
		// 渠道
		String channel = ConditionUtil.getChannel(request);
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String mobile = formUser.getMobile();
		model.addAttribute("id", id);
		model.addAttribute("mobile", mobile);
		// 当前用户信息
		String curMobile = eucUserService.queryUserInfoById(id).getMobile();
		if (null == curMobile) {
			curMobile = "";
		}
		if (!FormUserValidator.checkMobile(mobile)) {
			log.debug("手机号不合法");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_ILLEGAL);
			result.reject("notvaildMobile", "您输入的不是一个正确的手机号");
			errorDesc = new ErrorDesc(1002, "您输入的不是一个正确的手机号");
		}
		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
		if (null != user) {
			log.debug("该手机号已被使用");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_EXIST);
			result.reject("repeatBind", "该手机号已被使用");
			errorDesc = new ErrorDesc(1003, "该手机号已被使用");
		} else {
			String veriCode = mobileService.setVeriCodeByMobile(mobile);
			// 需要发短信
			String message = "验证码: "+veriCode+", 感谢您对梵町的支持!";
			String messageLog = "验证码: xxxx, 感谢您对梵町的支持!";

			SendSmsHelper.sendSms(uid, mobile, message,messageLog, channel, esid,
					SMSType.BIND);
			log.debug("验证码发送成功");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, veriCode,
					LogConstant.RESULT_SUCCESS, av.getBackUrl(), channel,
					esid, LogConstant.BING_MSISDN_PUSH_SMS_SUCCESS);
			errorDesc = new ErrorDesc(0, "");
		}
		String json = JsonUtil.parserObjToJsonStr(errorDesc);
		return renderJSONSuccess(response, json);
	}
}
