package com.easou.usercenter.web.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.ticket.registry.TicketRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.constant.CasConstant;
import com.easou.common.util.MD5Util;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.form.FormPass;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/changepass")
public class ChangePasswdController extends UserBaseController {

	@Resource(name="eucUserServiceImpl")
	private EucUserService eucUserService;
	
	@Autowired
	private Validator validator;

	@Autowired
	private TicketRegistry ticketRegistry;
	
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse respons) {
		ArrestValidation av = checkLogin(request, model);
		if(!av.isLogin()) {
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		if(null==eucUser) {
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		if(null==eucUser.getPasswd()) {
			model.addAttribute("nullPass", "1");
		}
		model.addAttribute("formPass", new FormPass());
		return "default/ui/changePasswd";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formPass") FormPass formPass,
			BindingResult result, ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator
				.retrieveCookieValue(request);
		
		ArrestValidation av = checkLogin(request, model);
		if(!av.isLogin()) {
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		if(null==eucUser) {
			log.debug("用户不存在");
			removeLoginAssert(request);
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "你要修改密码的用户不存在");
			return "result";
		}
		String passwd = formPass.getPasswd();
		String newpwd = formPass.getNewpwd();
		String confirm = formPass.getConfirm();
		validator.validate(formPass, result);
		if (result.hasErrors()) {
			log.info("验证失败");
			return "default/ui/changePasswd";
		}
		boolean error = false;
		String s = new String(FormUserValidator.checkPassword(newpwd));
		if(!"".equals(s)) {
			result.reject("FormPass[newpwd]", s);
			error = true;
		}
		s = new String(FormUserValidator.checkCfmPassword(newpwd, confirm));
		if(!"".equals(s)) {
			result.reject("FormPass[confirm]", s);
			error = true;
		}
		if(error) {
			log.info("确认密码等验证失败");
			return "default/ui/changePasswd";
		}
		if(null==passwd || !MD5Util.md5(passwd).toLowerCase().equals(eucUser.getPasswd())) {
			log.info("原密码错误");
			result.reject("FormPass[confirm]", "原密码错误");
			return "default/ui/changePasswd";
		} else {
			eucUserService.updatePasswd(av.getUserId(), newpwd);
			log.debug("更新密码成功");
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "修改成功，请您重新登录！ 系统将在3 秒后返回");
			
			// 清除session登录信息
			request.getSession().removeAttribute(CasConstant.CONST_CAS_ASSERTION);
			
			// 修改成功，清除ticket
			ticketRegistry.deleteTicket(ticketGrantingTicketId);
			log.debug("remove ticket from cache!");
			
			return "result";
		}
	}
}