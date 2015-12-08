package com.easou.usercenter.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.api.ClientConfig;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.Way;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormEmail;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/emregist")
public class EmailRegisterController extends BaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model) {
		model.addAttribute("formEmail", new FormEmail());
		return "default/ui/emailRegist";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formEmail") FormEmail formEmail,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		String email = formEmail.getEmail();
		String passwd = formEmail.getPassword();
		RequestInfo info = fetchRequestInfo(request);
		String qn=ConditionUtil.getQn(request);
		String appId = ClientConfig.getProperty("appId");
		if (!FormUserValidator.checkEmail(email)) {
			result.reject("notvaildEmail", "email地址不正确");
			return "default/ui/emailRegist";
		}
		String s = FormUserValidator.checkPassword(passwd);
		if(!"".equals(s)) {
			result.reject("FormUser.passwd[error]", s);
			return "default/ui/emailRegist";
		}
		EucUser user = eucUserService.queryUserInfoByEmail(email);
		if (null != user) {
			log.debug("该邮箱已被注册");
			result.reject("FormUser.mobile[repeat]", "该邮箱已被注册");
			BizLogUtil.registLog(info,email,null, Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
					LogConstant.REGIS_RESULT_MSISDN_EXIST,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);		
			return "default/ui/emailRegist";
		} else {
			EucUser eucUser = eucUserService.insertUserByEmailRegist(email, passwd, qn);
			if (null == eucUser) {
				log.warn("email注册失败,数据库生成用户错误");
				result.reject("regFail", "注册失败");
				return "default/ui/emailRegist";
			} else {
				log.debug("eamil注册插入成功");
				request.setAttribute("user", email);
				request.setAttribute("pass", passwd);
				//注册成功，记录日志
				BizLogUtil.registLog(info,email,eucUser.getId(), Way.PAGE,LogConstant.REGIS_TYPE_NAME,  LogConstant.RESULT_SUCCESS, "",
						LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
				if (null != request.getQueryString()) {
					return "forward:/inlogin?" + request.getQueryString();
				} else {
					return "forward:/inlogin";
				}
			}
		}
	}
}
