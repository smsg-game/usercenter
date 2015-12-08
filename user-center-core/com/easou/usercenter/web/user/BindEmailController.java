package com.easou.usercenter.web.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.util.MD5Util;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.form.FormEmail;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/bindEmail")
public class BindEmailController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		if (null == user) {
			this.removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		model.addAttribute("id", user.getId() + "");
		model.addAttribute("formEmail", new FormEmail());
		return "default/ui/bindEmail";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String execute(@ModelAttribute("formEmail") FormEmail formEmail,BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			return "result";
		}
		String email = formEmail.getEmail();
		String passwd = formEmail.getPassword();
		model.addAttribute("email", email);
		model.addAttribute("id", id);
		if (!FormUserValidator.checkEmail(email)) {
			result.reject("notvaildEmail", "email地址不正确");
			return "default/ui/bindEmail";
		}
		EucUser _eucUser = eucUserService.queryUserInfoById(id);
		if(null==passwd || !MD5Util.md5(passwd).equals(_eucUser.getPasswd())) {
			// 密码为空或不等
			result.reject("errorPasswd", "登录密码不正确");
			return "default/ui/bindEmail";
		}
		email = email.toLowerCase();
		EucUser user = eucUserService.queryUserInfoByEmail(email);
		if (null != user) {
			log.debug("该邮箱已被注册");
			result.reject("repeatEmail", "该邮箱已被注册");
			return "default/ui/bindEmail";
		} else {
			EucUser updUser = new EucUser();
			updUser.setEmail(email);
			if(eucUserService.updateUserById(av.getUserId(), updUser)) {
				model.addAttribute("redirUrl", av.getBackUrl());
				model.addAttribute("retMessage", "设置成功，系统将在3 秒后返回");
				return "result";
			} else {
				log.warn("更新用户邮箱失败:" + av.getUserId());
				result.reject("failerror", "注册失败");
				return "default/ui/bindEmail";
			}
		}
	}
}
