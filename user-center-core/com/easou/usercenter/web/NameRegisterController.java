package com.easou.usercenter.web;

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

import com.easou.common.api.ClientConfig;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.Way;
import com.easou.common.util.MD5Util;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormUser;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/nregist")
public class NameRegisterController extends BaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model) {
		model.addAttribute("formUser", new FormUser());
		return "default/ui/nameRegist";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, final HttpServletRequest request,
			final HttpServletResponse response) {

		String username = formUser.getUsername().toLowerCase().trim();
		String password = formUser.getPassword();
		String confirm = formUser.getConfirm();
		RequestInfo info = fetchRequestInfo(request);
		String qn = ConditionUtil.getQn(request);
		String appId = ClientConfig.getProperty("appId");
		try {
			
			validator.validate(formUser, result);
			if (result.hasErrors()) {
				log.debug("验证失败");
				//记录日志
				BizLogUtil.registLog(info,username, null, Way.PAGE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
						LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);					
				return "default/ui/nameRegist";
			}

			boolean error = false;
			String s = new String(FormUserValidator.checkUserName(username));
			if (!"".equals(s)) {
				result.reject("FormUser.username[check]", s);
				error = true;
			}
			s = new String(FormUserValidator.checkPassword(password));
			if (!"".equals(s)) {
				result.reject("FormUser.password[check]", s);
				error = true;
			}
			s = new String(FormUserValidator
					.checkCfmPassword(password, confirm));
			if (!"".equals(s)) {
				result.reject("FormUser.password[confirm]", s);
				error = true;
			}
			if (error) {
				log.debug("表单校验失败");
				BizLogUtil.registLog(info,username, null, Way.PAGE,LogConstant.REGIS_TYPE_NAME,LogConstant.RESULT_FAILURE,"",
						LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);					
				return "default/ui/nameRegist";
			}
			EucUser user = eucUserService.queryUserInfoByName(username);
			if (null != user) {
				log.debug("该用户已注册");
				result.reject("FormUser.username[repeat]", "登录名已注册");
				//记录日志
				BizLogUtil.registLog(info,username,null, Way.PAGE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE,"",
						LogConstant.REGIS_RESULT_NAME_EXIST,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);				
				return "default/ui/nameRegist";
			}
			EucUser eucUser = eucUserService.insertUserForUnameRegist(username,
					MD5Util.md5(password).toLowerCase(), qn);
			
			if (null != eucUser) {
				request.setAttribute("user", username);
				request.setAttribute("pass", password);
				//注册成功，记录日志
				BizLogUtil.registLog(info,username,eucUser.getId(), Way.PAGE,LogConstant.REGIS_TYPE_NAME,  LogConstant.RESULT_SUCCESS,"",
						LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
				//log.info("redirect url["+request.getParameter("service")+"]");
				if (null != request.getQueryString()) {
					return "forward:/inlogin?" + request.getQueryString();
				} else {
					return "forward:/inlogin";
				}
			} else {
				result.reject("FormUser.username", "注册失败");
				//注册失败，记录日志
				BizLogUtil.registLog(info,username, null, Way.PAGE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE,"",
						LogConstant.REGIS_RESULT_EXCEPTION,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
				return "default/ui/nameRegist";
			}
		} catch (Exception e) {
			log.error(e, e);//注册失败，记录日志
			BizLogUtil.registLog(info,username, null, Way.PAGE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE,"",
					LogConstant.REGIS_RESULT_EXCEPTION,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);		
			return "errors";
		}
	}
}