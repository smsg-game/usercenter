package com.easou.usercenter.web;

import java.util.Date;

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

import com.easou.usercenter.entity.EucSignature;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucSignatureService;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.SignatureUtil;
import com.easou.usercenter.web.form.FormEmail;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/pass/emresetpass")
public class EmailResetPassController extends BaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private EucSignatureService eucSignatureSerivce;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		String secert = request.getParameter("s");
		Long userId = SignatureUtil.decodeSecert(secert);
		if (null == userId) {
			model.addAttribute("redirUrl", this.getPath(request, "", null));
			model.addAttribute("retMessage", "抱歉，错误链接");
			return "result";
		}
		EucUser eucUser = eucUserService.queryUserInfoById(userId + "");
		if(null == eucUser) {
			// 没有对应用户
			model.addAttribute("redirUrl", this.getPath(request, "", null));
			model.addAttribute("retMessage", "抱歉，错误链接");
			return "result";
		}
		if(!checkSignature(userId, secert)) {
			model.addAttribute("redirUrl", this.getPath(request, "", null));
			model.addAttribute("retMessage", "抱歉，密匙已失效");
			return "result";
		}
		model.addAttribute("formEmail", new FormEmail());
		model.addAttribute("secert", secert);
		return "default/ui/setpass";
	}

	/**
	 * 邮箱找回密码
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String execute(@ModelAttribute("formEmail") FormEmail formEmail,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		String password = formEmail.getPassword();
		String confirm = formEmail.getConfirm();
		
		if(null==confirm || !confirm.equals(password)) {
			result.reject("FormEmail.password[confirm]", "重复密码不匹配");
			return "default/ui/setpass";
		}
		String s = FormUserValidator.checkPassword(password);
		if (!"".equals(s)) {
			result.reject("FormEmail.password[check]", s);
			return "default/ui/setpass";
		}
		String secert = request.getParameter("secert");
		if(null!=secert) {
			Long userId  = SignatureUtil.decodeSecert(secert);
			if(null!=userId) {
				if(checkSignature(userId, secert)) {
					eucUserService.updatePasswd(userId+"", password);
					model.addAttribute("redirUrl", this.getPath(request, "", null));
					model.addAttribute("retMessage", "修改成功");
				return "result";
				} else {
					log.warn("email重设密码非法操作");
					model.addAttribute("redirUrl", this.getPath(request, "", null));
					model.addAttribute("retMessage", "非法操作");
					return "result";
				}
			} else {
				model.addAttribute("redirUrl", this.getPath(request, "", null));
				model.addAttribute("retMessage", "非法操作");
				return "result";
			}
		} else {
			log.warn("email重设密码非法操作");
			model.addAttribute("redirUrl", this.getPath(request, "", null));
			model.addAttribute("retMessage", "非法操作");
			return "result";
		}
	}
	
	/**
	 * 检查提交的密匙是否与数据库中的密匙匹配
	 * @param userId
	 * @param secert
	 * @return
	 */
	private boolean checkSignature(Long userId, String secert) {
		EucSignature signature = eucSignatureSerivce.queryById(userId);
		if(null==signature) {
			return false;
		} else {
			if(new Date().after(signature.getExpire())) {
				// 过期
				return false;
			}
			if(!secert.equals(SignatureUtil.encodeSecert(signature.getId(),signature.getKey()))) {
				// 密文不正确
				return false;
			}
			return true;
		}
	}
}