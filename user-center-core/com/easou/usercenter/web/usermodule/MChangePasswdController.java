package com.easou.usercenter.web.usermodule;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.json.JsonUtil;
import com.easou.usercenter.ErrorDesc;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.form.FormPass;
import com.easou.usercenter.web.user.ArrestValidation;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/mchangepass")
public class MChangePasswdController extends MUserBaseController {

	@Resource(name="eucUserServiceImpl")
	private EucUserService eucUserService;
	
	@Autowired
	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse respons) {
		ArrestValidation av = checkLogin(request, model);
		String usermoduleUrl = SSOConfig.getProperty("usermodule.url");
		if(!av.isLogin()) {
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "redirect:" + usermoduleUrl;
		}
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		if(null==eucUser) {
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "redirect:" + usermoduleUrl;
		}
		if(null==eucUser.getPasswd()) {
			model.addAttribute("nullPass", "1");
		}
		model.addAttribute("formPass", new FormPass());
		return "usermodule/changePasswd";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formPass") FormPass formPass,
			BindingResult result, ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		ErrorDesc errorDesc = new ErrorDesc(0, "");
		if(!av.isLogin()) {
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			errorDesc = new ErrorDesc(1001, "用户未登录，请登录后再操作");
		}
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		if(null==eucUser) {
			log.debug("用户不存在");
			removeLoginAssert(request);
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "你要修改密码的用户不存在");
			errorDesc = new ErrorDesc(1002, "用户不存在");
		}
		String newpwd = formPass.getNewpwd();
		validator.validate(formPass, result);
		if (result.hasErrors()) {
			log.info("验证失败");
			errorDesc = new ErrorDesc(1003, "验证失败");
		}
		boolean error = false;
		String s = new String(FormUserValidator.checkPassword(newpwd));
		if(!"".equals(s)) {
			result.reject("FormPass[newpwd]", s);
			error = true;
		}
		if(error) {
			log.info("确认密码等验证失败");
			errorDesc = new ErrorDesc(1004, "确认密码等验证失败");
		}
		eucUserService.updatePasswd(av.getUserId(), newpwd);
		log.debug("更新密码成功");
		//model.addAttribute("redirUrl", av.getBackUrl());
		model.addAttribute("redirUrl",getDefaultBackUrl(request));
		model.addAttribute("retMessage", "修改成功，系统将在3 秒后返回");
		String jsonResult = JsonUtil.parserObjToJsonStr(errorDesc);
		return renderJSONSuccess(response, jsonResult);
	}
}