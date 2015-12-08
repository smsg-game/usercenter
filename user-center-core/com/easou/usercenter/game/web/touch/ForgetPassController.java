package com.easou.usercenter.game.web.touch;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.easou.common.constant.SMSType;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.EucMessage;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/game/touch/")
public class ForgetPassController extends GameBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	@RequestMapping("sforget")
	public String show() {
		return "game/touch/ui/forgetPass";
	}

	/**
	 * 手机号找回密码
	 * 
	 * @return
	 */
	@RequestMapping("findbym")
	public String mobileForget(ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String channel = ConditionUtil.getChannel(request);
        String esid = ConditionUtil.getEsid(request);
        String uid = ConditionUtil.getUid(request);
		model.addAttribute("mobile", mobile); // 将手机号传递到验证码页面，用户此时手机如果修改了绑定，那他会修改他新绑定手机的账号的密码
		if (!FormUserValidator.checkMobile(mobile)) {
			log.info("手机号不合法:" + mobile);
			return renderJSONFailure(response, "手机号不合法");
			//return "game/touch/ui/forgetPass";
		}
		EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
		if (null == eucUser) {
			log.info("该手机号还未注册");
			//msg.addMsg("该手机号还未注册");
			//msg.bindMsg(model);
			//return "game/touch/ui/forgetPass";
			return renderJSONFailure(response, "该手机号还未注册");
		}
		// 从缓存中获取手机号对应的验证码
		String veriCode = mobileService.getVeriCodeByMobile(mobile);
		if (null == veriCode) {
			// 缓存中没有验证码，生成新的验证码
			veriCode = mobileService.setVeriCodeByMobile(mobile);
			log.info("新的验证码是: " + veriCode);
			Map paramMap = getRequestMap(request);
			paramMap.put("mobile", mobile);
			paramMap.put("veriCode", veriCode);
			String longUrl = getPath(request, "/pass/resetpass", paramMap);
			StringBuffer message = new StringBuffer("欢迎使用梵町忘记密码功能,您的验证码是").append(veriCode)
					.append(",或点击 ").append(getShortUrl(request, longUrl)).append(" 进行重设密码");
			StringBuffer messageLog = new StringBuffer("欢迎使用梵町忘记密码功能,您的验证码是******")
			        .append(",或点击 ").append(getShortUrl(request, longUrl)).append(" 进行重设密码");
			SendSmsHelper.sendSms(uid,mobile,message.toString(),messageLog.toString(),channel,esid,SMSType.FORGET_PASS);
			return renderJSONSuccess(response, "验证码已发送，请在手机上查看");
			//return "game/touch/ui/forgetByMobile";
		} else { 
			// 缓存中已有验证码，说明用户刚生成了验证码
			log.info("已有验证码: " + veriCode);
			//msg.addMsg("验证码已发送，请在手机上查看");
			//msg.bindMsg(model);
			return renderJSONFailure(response, "验证码已发送，请在手机上查看");
			//return "game/touch/ui/forgetByMobile";
		}
	}

	/**
	 * 输入新密码
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "resetpass", method = RequestMethod.POST)
	public String resetPass(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String newPwd = request.getParameter("newPwd");
		String cfmpwd = request.getParameter("confirm");
		String veriCode = request.getParameter("veriCode");
		String mobile = request.getParameter("mobile");
		model.addAttribute("mobile", mobile); // 将手机号放入model，作为要验证的手机号
		model.addAttribute("veriCode", veriCode);
		//EucMessage msg = new EucMessage();
		if (null == mobile || "".equals(mobile)) {
			log.warn("手机号非法: " + mobile);
			return renderJSONFailure(response, "手机号不合法");
		}
		String errorMsg = FormUserValidator.checkPassword(newPwd);
		if(!StringUtil.isEmpty(errorMsg)){
			log.debug(errorMsg);
			return renderJSONFailure(response, errorMsg);
		}
		errorMsg = FormUserValidator.checkCfmPassword(newPwd, cfmpwd);
		if(!StringUtil.isEmpty(errorMsg)){
			log.debug(errorMsg);
			return renderJSONFailure(response, errorMsg);
		}
		// 缓存中的验证码
		String vcodeInCache = mobileService.getVeriCodeByMobile(mobile);
		if (null == vcodeInCache) {
			log.debug(mobile + " 手机的验证码在缓存中不存在");
			//msg.addMsg("验证码已过期，请重新获取");
			return renderJSONFailure(response, "验证码已过期，请重新获取");
		} else {
			if (vcodeInCache.equalsIgnoreCase(veriCode)) {
				// 验证码相符
				EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
				if (null != eucUser) {
					eucUserService.updatePasswd(eucUser.getId() + "", newPwd);
					mobileService.delVeriCodeByMobile(mobile);
				} else {
					log.debug("手机号对应用户不存在" + mobile);
					return renderJSONFailure(response, "该手机号不存在任何账号");
				}
			} else {
				// 验证码不符
				log.debug("验证码不符");
				return renderJSONFailure(response, "验证码错误");
			}
		}
		log.debug("密码重设成功");
		request.setAttribute("user", mobile);
		request.setAttribute("pass", newPwd);
		return renderJSONSuccess(response, "密码重设成功,正在回到游戏个人中心。");
	}
	
	@RequestMapping(value = "resetSucc", method = RequestMethod.POST)
	public String resetSucc(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String newPwd = request.getParameter("newPwd");
		String mobile = request.getParameter("mobile");
		request.setAttribute("user", mobile);
		request.setAttribute("pass", newPwd);
		return "forward:/inlogin";
	}
	
	
	
	@RequestMapping(value = "resetpass", method = RequestMethod.GET)
	public String resetPassBySms(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String veriCode = request.getParameter("veriCode");
		model.addAttribute("mobile", mobile); // 将手机号放入model，作为要验证的手机号
		model.addAttribute("veriCode", veriCode);
		return "game/touch/ui/forgetByMobile";
	}
	
}
