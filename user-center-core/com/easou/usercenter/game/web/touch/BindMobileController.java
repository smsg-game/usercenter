package com.easou.usercenter.game.web.touch;

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
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormUser;
import com.easou.usercenter.web.user.ArrestValidation;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/game/touch/")
public class BindMobileController extends GameBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	@RequestMapping("bindMobile")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		model.addAttribute("id", av.getUserId());
		model.addAttribute("formUser", new FormUser());
		return "game/touch/ui/bindMobile";
	}

	@RequestMapping("bindMobileNext")
	public String bind(ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response){
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			//model.addAttribute("redirUrl", av.getBackUrl());
			//model.addAttribute("redirUrl",getDefaultBackUrl(request));
			//model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			//return "result";
			return renderJSONFailure(response, "你无权操作");
		}
		// 渠道
		String channel = ConditionUtil.getChannel(request);
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String mobile = request.getParameter("mobile");
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
			return renderJSONFailure(response, "您输入的不是一个正确的手机号");
			//result.reject("notvaildMobile", "您输入的不是一个正确的手机号");
			//return "game/touch/ui/bindMobile";
		}
		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
		if (null != user) {
			log.debug("该手机号已被使用");
			BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
					Way.PAGE, BindActivity.REQUEST, "",
					LogConstant.RESULT_FAILURE, av.getBackUrl(), channel, esid,
					LogConstant.BING_MSISDN_EXIST);
			//result.reject("repeatBind", "该手机号已被使用");
			return renderJSONFailure(response, "该手机号已被使用");
			//return "game/touch/ui/bindMobile";
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
				return renderJSONSuccess(response, "验证码已发送，请在手机上查看");
				//return "forward:/game/touch/bindcfm";
			} else {
				log.debug("该手机号已发送了验证码:"+veriCode);
				BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
						Way.PAGE, BindActivity.REQUEST, veriCode,
						LogConstant.RESULT_FAILURE, av.getBackUrl(), channel,
						esid, LogConstant.BING_MSISDN_PUSH_SMS_EXIST);
				return renderJSONFailure(response, "验证码已发送，请在手机上查看");
				//return "forward:/game/touch/bindcfm";
			}
		}
	}
}
