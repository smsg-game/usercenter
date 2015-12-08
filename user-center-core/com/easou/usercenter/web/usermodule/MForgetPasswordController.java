package com.easou.usercenter.web.usermodule;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.constant.SMSType;
import com.easou.usercenter.ErrorDesc;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.JsonUtil;
import com.easou.usercenter.web.EucMessage;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/")
public class MForgetPasswordController extends MUserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	@RequestMapping("mforget")
	public String show() {
		return "usermodule/forgetPassword";
	}

	/**
	 * 手机号找回密码
	 * 
	 * @return
	 */
	@RequestMapping("mfindbym")
	public String mobileForget(ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String channel = ConditionUtil.getChannel(request);
        String esid = ConditionUtil.getEsid(request);
        String uid = ConditionUtil.getUid(request);
		model.addAttribute("mobile", mobile); // 将手机号传递到验证码页面，用户此时手机如果修改了绑定，那他会修改他新绑定手机的账号的密码
		EucMessage msg = new EucMessage();
//		ArrestValidation av = checkLogin(request, model);
//		EucUser tuser = eucUserService.queryUserInfoById(av.getUserId());
		ErrorDesc error = new ErrorDesc(0, "");
//		if(tuser == null) {
//			log.debug("用户不存在");
//			msg.addMsg("用户不存在");
//			msg.bindMsg(model);
//			error.build(1001, "用户不存在");
//			String jsonResult = JsonUtil.parserObjToJsonStr(error);
//			return renderJSONSuccess(response, jsonResult);
//		}
		if (!FormUserValidator.checkMobile(mobile)) {
			log.info("手机号不合法:" + mobile);
			msg.addMsg("手机号不合法");
			msg.bindMsg(model);
			error.build(1002, "手机号不合法");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}
		EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
		
		if (eucUser == null) {
			log.info("此手机号未绑定用户");
			error.build(1002, "此手机号未绑定用户");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}
		// 从缓存中获取手机号对应的验证码
		String veriCode = mobileService.getVeriCodeByMobile(mobile);
		if (null == veriCode) {
			// 缓存中没有验证码，生成新的验证码
			veriCode = mobileService.setVeriCodeByMobile(mobile);
			log.info("新的验证码是: " + veriCode);
			
			StringBuffer message = new StringBuffer("欢迎使用梵町忘记密码功能,您的验证码是").append(veriCode);
			StringBuffer messageLog = new StringBuffer("欢迎使用梵町忘记密码功能,您的验证码是******");
			SendSmsHelper.sendSms(uid,mobile,message.toString(),messageLog.toString(),channel,esid,SMSType.FORGET_PASS);
			
		} else { 
			// 缓存中已有验证码，说明用户刚生成了验证码
			log.info("已有验证码: " + veriCode);
			msg.addMsg("验证码已发送，请在手机上查看");
			msg.bindMsg(model);
			error.build(1004, "验证码已发送，请在手机上查看");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
//			return "usermodule/forgetPassword-cfm";
		}
		String jsonResult = JsonUtil.parserObjToJsonStr(error);
		return renderJSONSuccess(response, jsonResult);
	}

	/**
	 * 输入新密码
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "mresetpass", method = RequestMethod.POST)
	public String resetPass(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String newPwd = request.getParameter("newPwd");
		String veriCode = request.getParameter("veriCode");
		String mobile = request.getParameter("mobile");
		model.addAttribute("mobile", mobile); // 将手机号放入model，作为要验证的手机号
		model.addAttribute("veriCode", veriCode);
		EucMessage msg = new EucMessage();
//		ArrestValidation av = checkLogin(request, model);
//		EucUser tuser = eucUserService.queryUserInfoById(av.getUserId());
		ErrorDesc error = new ErrorDesc(0, "");
		if (null == mobile || "".equals(mobile)) {
			log.warn("手机号非法: " + mobile);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			error.build(1001, "手机号非法: " + mobile);
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}
		msg.addMsg(FormUserValidator.checkPassword(newPwd));
		if (msg.hasMsg()) {
			msg.bindMsg(model);
			error.build(1002, "密码验证错误");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}
		
		// 缓存中的验证码
		String vcodeInCache = mobileService.getVeriCodeByMobile(mobile);
		if (null == vcodeInCache) {
			log.debug(mobile + " 手机的验证码在缓存中不存在");
			msg.addMsg("验证码已过期，请重新获取");
			msg.bindMsg(model);
			error.build(1005, "验证码已过期，请重新获取");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		} else {
			if (vcodeInCache.equalsIgnoreCase(veriCode)) {
				// 验证码相符
				EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
				if (null != eucUser) {
					eucUserService.updatePasswd(eucUser.getId() + "", newPwd);
					mobileService.delVeriCodeByMobile(mobile);
				} else {
					log.debug("手机号对应用户不存在" + mobile);
					msg.addMsg("该手机号不存在任何账号");
					msg.bindMsg(model);
					error.build(1006, "手机号对应用户不存在" + mobile);
					String jsonResult = JsonUtil.parserObjToJsonStr(error);
					return renderJSONSuccess(response, jsonResult);
				}
			} else {
				// 验证码不符
				log.debug("验证码不符");
				msg.addMsg("验证码错误");
				msg.bindMsg(model);
				error.build(1007, "验证码不符");
				String jsonResult = JsonUtil.parserObjToJsonStr(error);
				return renderJSONSuccess(response, jsonResult);
			}
		}
		log.debug("密码重设成功");
		request.setAttribute("user", mobile);
		request.setAttribute("pass", newPwd);
//		String domain = SSOConfig.getProperty("domain.readonly");
//		String url = "redirect:" + domain + "/user/usersetting.html";
		
		String jsonResult = JsonUtil.parserObjToJsonStr(error);
		return renderJSONSuccess(response, jsonResult);
	}
	
	@RequestMapping(value = "mresetSucc", method = RequestMethod.POST)
	public String resetSucc(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String newPwd = request.getParameter("newPwd");
		String mobile = request.getParameter("mobile");
		request.setAttribute("user", mobile);
		request.setAttribute("pass", newPwd);
		request.setAttribute("service", "/user/usersetting.html");
		return "forward:/inlogin";
	}
	
	
	
	@RequestMapping(value = "mresetpass", method = RequestMethod.GET)
	public String resetPassBySms(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
//		String domain = SSOConfig.getProperty("domain.readonly");
//		String url = "redirect:" + domain + "/user/usersetting.html";
		String mobile = request.getParameter("mobile");
		model.addAttribute("mobile", mobile);
		return "usermodule/forgetPassword-cfm";
	}
	
}
