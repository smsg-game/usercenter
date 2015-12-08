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
import com.easou.usercenter.web.form.FormUser;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/jbMobile")
public class JBMobileController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		
		//防止恶意访问
		if(!Boolean.TRUE.toString().equals(
				request.getSession().getAttribute("ID_IDENTIFIED"))){
			return "redirect:/user/check/show";
		}
		request.getSession().removeAttribute("ID_IDENTIFIED");
		
		ArrestValidation av = checkLogin(request, model);
		model.addAttribute("formUser", new FormUser());
		model.addAttribute("id", av.getUserId());
		return "default/ui/jbindMobile";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String execute(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, ModelMap model, final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			return "result";
		}
		// 渠道
//		String channel = ConditionUtil.getChannel(request);
//		String esid = ConditionUtil.getEsid(request);
//		String uid = ConditionUtil.getUid(request);
		String mobile = formUser.getMobile();
		String passwd = formUser.getPassword();
		model.addAttribute("id", id);
		if(!FormUserValidator.checkMobile(mobile)) {
			result.reject("notvaildMobile", "您输入的不是一个正确的手机号");
			return "default/ui/jbindMobile";
		}
		EucUser eucUser = eucUserService.queryUserInfoById(id);
		if(null==eucUser.getMobile() || !mobile.equals(eucUser.getMobile())) {
			result.reject("notMatchMob", "原手机号不正确");
			return "default/ui/jbindMobile";
		}
		if(null==passwd || !MD5Util.md5(passwd).equals(eucUser.getPasswd())) {
			result.reject("notMatchPass", "密码不正确");
			return "default/ui/jbindMobile";
		}
		eucUserService.updateMobileById(id, null);
		//model.addAttribute("redirUrl", av.getBackUrl());
		model.addAttribute("redirUrl",getDefaultBackUrl(request));
		model.addAttribute("retMessage", "取消成功，系统将在3 秒后返回");
		return "result";
	}
}
