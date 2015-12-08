package com.easou.usercenter.web.user;

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
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormUser;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/bindMobile")
public class BindMobileController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		
		//防止恶意访问
		if(user.getMobile() != null && user.getMobile().length() > 0){
			if(!Boolean.TRUE.toString().equals(
					request.getSession().getAttribute("ID_IDENTIFIED"))){
				return "redirect:/user/check/show";
			}
		}
		request.getSession().removeAttribute("ID_IDENTIFIED");
		
		model.addAttribute("id", av.getUserId());
		model.addAttribute("formUser", new FormUser());
		return "default/ui/bindMobile";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bind(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			return "result";
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
			return "default/ui/bindMobile";
		}
		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
		if (null != user) {
			log.debug("该手机号已被使用");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_EXIST);
			result.reject("repeatBind", "该手机号已被使用");
			return "default/ui/bindMobile";
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
				return "forward:/user/bindcfm";
			} else {
				log.debug("该手机号已发送了验证码");
				BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
						Way.PAGE, BindActivity.REQUEST, veriCode,
						LogConstant.RESULT_FAILURE, av.getBackUrl(), channel,
						esid, LogConstant.BING_MSISDN_PUSH_SMS_EXIST);
				return "forward:/user/bindcfm";
			}
		}
	}
}
