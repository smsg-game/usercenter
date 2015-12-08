package com.easou.usercenter.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.TokenUtils;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.easou.common.constant.SMSType;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/pass/")
public class ForgetPassController extends BaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;
	@Autowired
	private TicketRegistry ticketRegistry;

	@RequestMapping("sforget")
	public String show() {
		return "default/ui/forgetPass";
	}

	/**
	 * 手机号找回密码
	 * 
	 * @return
	 */
	@RequestMapping("findbym")
	public String mobileForget(ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		EucMessage msg = new EucMessage();
		String mobile = request.getParameter("mobile");
		String channel = ConditionUtil.getChannel(request);
        String esid = ConditionUtil.getEsid(request);
        String uid = ConditionUtil.getUid(request);
		model.addAttribute("mobile", mobile); // 将手机号传递到验证码页面，用户此时手机如果修改了绑定，那他会修改他新绑定手机的账号的密码
		if (!FormUserValidator.checkMobile(mobile)) {
			log.info("手机号不合法:" + mobile);
			msg.addMsg("手机号不合法");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
		if (null == eucUser) {
			log.info("该手机号还未注册");
			msg.addMsg("该手机号还未注册");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		// 从缓存中获取手机号对应的验证码
		String veriCode = mobileService.getVeriCodeByMobile(mobile);
		if (null == veriCode) {
			// 缓存中没有验证码，生成新的验证码
			veriCode = mobileService.setVeriCodeByMobile(mobile);
			log.info("新的验证码是: " + veriCode);
//			Map paramMap = getRequestMap(request);
//			paramMap.put("mobile", mobile);
//			paramMap.put("veriCode", veriCode);
//			String longUrl = getPath(request, "/pass/resetpass", paramMap);
			StringBuffer message = new StringBuffer("欢迎使用梵町忘记密码功能,您的验证码是").append(veriCode);
			StringBuffer messageLog = new StringBuffer("欢迎使用梵町忘记密码功能,您的验证码是******");
			SendSmsHelper.sendSms(uid,mobile,message.toString(),messageLog.toString(),channel,esid,SMSType.FORGET_PASS);
			return "default/ui/forgetByMobile";
		} else { 
			// 缓存中已有验证码，说明用户刚生成了验证码
			log.info("已有验证码: " + veriCode);
			msg.addMsg("验证码已发送，请在手机上查看");
			msg.bindMsg(model);
			return "default/ui/forgetByMobile";
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
		EucMessage msg = new EucMessage();
		if (null == mobile || "".equals(mobile)) {
			log.warn("手机号非法: " + mobile);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}

		msg.addMsg(FormUserValidator.checkPassword(newPwd));
		msg.addMsg(FormUserValidator.checkCfmPassword(newPwd, cfmpwd));
		if (msg.hasMsg()) {
			msg.bindMsg(model);
			return "default/ui/forgetByMobile";
		}
		// 缓存中的验证码
		String vcodeInCache = mobileService.getVeriCodeByMobile(mobile);
		if (null == vcodeInCache) {
			log.debug(mobile + " 手机的验证码在缓存中不存在");
			msg.addMsg("验证码已过期，请重新获取");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		} else {
			if (vcodeInCache.equalsIgnoreCase(veriCode)) {
				// 验证码相符
				EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
				if (null != eucUser) {
					final String userName = eucUser.getName();
					final String oldPwd = eucUser.getPasswd();
					
					eucUserService.updatePasswd(eucUser.getId() + "", newPwd);
					mobileService.delVeriCodeByMobile(mobile);
					
					log.debug("update password successful!");
					
					// 修改成功，清除ticket
					final String oldToken = TokenUtils.getTokenByUserInfo(userName, oldPwd, true);
					ticketRegistry.deleteTicket(oldToken);
					log.debug("remove ticket from cache!");
				} else {
					log.debug("手机号对应用户不存在" + mobile);
					msg.addMsg("该手机号不存在任何账号");
					msg.bindMsg(model);
					return "default/ui/forgetPass";
				}
			} else {
				// 验证码不符
				log.debug("验证码不符");
				msg.addMsg("验证码错误");
				msg.bindMsg(model);
				return "default/ui/forgetByMobile";
			}
		}
		log.debug("密码重设成功");
		request.setAttribute("user", mobile);
		request.setAttribute("pass", newPwd);
		return "forward:/logout";
	}
	
	@RequestMapping(value = "resetpass", method = RequestMethod.GET)
	public String resetPassBySms(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String veriCode = request.getParameter("veriCode");
		model.addAttribute("mobile", mobile); // 将手机号放入model，作为要验证的手机号
		model.addAttribute("veriCode", veriCode);
		return "default/ui/forgetByMobile";
	}

	/**
	 * 登录名找回密码
	 * 
	 * @return
	 */
	@RequestMapping("findbyn")
	public String nameForget(ModelMap model,
			@RequestParam("username") String username) {
		EucMessage msg = new EucMessage();
		if (null == username || "".equals(username.trim())) {
			msg.addMsg("请输入您的登录名");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		username = username.toLowerCase();
		model.addAttribute("username", username);
		EucUser eucUser = eucUserService.queryUserInfoByName(username);
		if (null == eucUser) {
			log.info("用户不存在:" + username);
			msg.addMsg("用户不存在");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		String question = eucUser.getQuestion();
		if (null == question || "".equals(question.trim())) {
			log.info("该用户没有设置问题:" + username);
			msg.addMsg("该用户没有设置找回密码");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		model.addAttribute("question", eucUser.getQuestion());
		model.addAttribute("username", eucUser.getName());
		return "default/ui/forgetByName";
	}

	/**
	 * 回答问题
	 * 
	 * @return
	 */
	@RequestMapping("anwques")
	public String answerQues(ModelMap model,
			@RequestParam("username") String username,
			@RequestParam("answer") String answer) {
		model.addAttribute("username", username);
		model.addAttribute("answer", answer);
		EucMessage msg = new EucMessage();
		if (null == answer || "".equals(answer.trim())) {
			log.info("答案没有填写:" + answer);
			msg.addMsg("请填写您的答案");
			msg.bindMsg(model);
			return "forward:/pass/findbyn";
		}
		if (null == username || "".equals(username.trim())) {
			log.warn("用户 名错误: " + username);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		EucUser eucUser = eucUserService.queryUserInfoByName(username);
		if (null == eucUser) {
			log.warn("用户不存在:" + username);
			msg.addMsg("用户不存在");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		if (!answer.equalsIgnoreCase(eucUser.getAnswer())) {
			log.info("答案错误: " + answer);
			msg.addMsg("您的答案不正确");
			msg.bindMsg(model);
			return "forward:/pass/findbyn";
		}
		return "default/ui/forgetNewpwd";
	}

	/**
	 * 输入新密码
	 * 
	 * @return
	 */
	@RequestMapping("newpwd")
	public String entNewPwd(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String newpwd = request.getParameter("newpwd");
		String confirm = request.getParameter("confirm");
		String username = request.getParameter("username");
		String answer = request.getParameter("answer");
		EucMessage msg = new EucMessage();
		msg.addMsg(FormUserValidator.checkPassword(newpwd));
		msg.addMsg(FormUserValidator.checkCfmPassword(newpwd, confirm));
		if (msg.hasMsg()) {
			// 填入内容不正确
			msg.bindMsg(model);
			return "forward:/pass/anwques";
		}
		if (null == username || "".equals(username.trim())) {
			// 防止用户直接输入action uri
			log.warn("用户名错误: " + username);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		if (null == answer || "".equals(answer.trim())) {
			log.warn("答案错误: " + answer);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		EucUser eucUser = eucUserService.queryUserInfoByName(username);
		if (null == eucUser) {
			log.warn("用户不存在: " + username);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		final String userName = eucUser.getName();
		final String oldPwd = eucUser.getPasswd();
		
		if (!answer.equalsIgnoreCase(eucUser.getAnswer())) {
			log.warn("答案错误: " + answer);
			msg.addMsg("非法操作");
			msg.bindMsg(model);
			return "default/ui/forgetPass";
		}
		eucUserService.updatePasswd(eucUser.getId() + "", newpwd);
		log.debug("密码重设成功 ");
		
		log.debug("update password successful!");
		
		// 修改成功，清除ticket
		final String oldToken = TokenUtils.getTokenByUserInfo(userName, oldPwd, true);
		ticketRegistry.deleteTicket(oldToken);
		log.debug("remove ticket from cache!");
		
		request.setAttribute("user", username);
		request.setAttribute("pass", newpwd);
		return "forward:/inlogin";

	}
}
