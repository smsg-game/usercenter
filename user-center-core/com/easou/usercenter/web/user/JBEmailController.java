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

@Controller
@RequestMapping("/user/jbEmail")
public class JBEmailController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		model.addAttribute("formEmail", new FormEmail());
		model.addAttribute("id", av.getUserId());
		return "default/ui/jbindEmail";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String execute(@ModelAttribute("formEmail") FormEmail formEmail,
			BindingResult result, ModelMap model, final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			return "result";
		}
		// 渠道
//		String channel = ConditionUtil.getChannel(request);
//		String esid = ConditionUtil.getEsid(request);
//		String uid = ConditionUtil.getUid(request);
		String email = formEmail.getEmail();
		String passwd = formEmail.getPassword();
		model.addAttribute("id", id);
		EucUser eucUser = eucUserService.queryUserInfoById(id);
		if(null==eucUser.getEmail() || !email.equals(eucUser.getEmail())) {
			result.reject("notMatchMail", "原邮箱地址不正确");
			return "default/ui/jbindEmail";
		}
		if(null==passwd || !MD5Util.md5(passwd).equals(eucUser.getPasswd())) {
			result.reject("notMatchPass", "密码不正确");
			return "default/ui/jbindEmail";
		}
		eucUserService.updateEmailById(id, null);
		model.addAttribute("redirUrl", av.getBackUrl());
		model.addAttribute("retMessage", "取消成功，系统将在3 秒后返回");
		return "result";
	}
}
