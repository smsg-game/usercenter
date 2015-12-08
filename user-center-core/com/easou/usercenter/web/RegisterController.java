package com.easou.usercenter.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.common.util.MD5Util;
import com.easou.common.util.RandomKeyGenerator;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
public class RegisterController extends BaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;
	
	@RequestMapping("/aregist")
	public String execute(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String mobile = request.getParameter("am");	// 自动获取的手机号
		String srt = request.getParameter("srt");	// 密文
		Map paramMap = getRequestMap(request);
		paramMap.remove("am");
		paramMap.remove("srt");
		if(null==mobile || null == srt) {
			log.info("非法操作");
			return "redirect:/mregist" + this.getQueryParam(paramMap);
		}
		String srtFormMobile = MD5Util.md5(mobile + SSOConfig.getSecertKey());
		if(!srtFormMobile.equals(srt)) {
			log.info("密文不正确");
			return "redirect:/mregist" + this.getQueryParam(paramMap);
		}
		if (!FormUserValidator.checkMobile(mobile)) {
			log.info("手机号不合法");
			return "redirect:/mregist" + this.getQueryParam(paramMap);
		} else {
			EucUser user = eucUserService.queryUserInfoByMobile(mobile);
			if (null != user) {
				return redirectTo(model, request, "该手机号已被注册，请使用手动注册", paramMap);
			} else {
				String passwd = RandomKeyGenerator.genNumber(6);
				EucUser eucUser = eucUserService.insertUserForMobileRegist(
						mobile, MD5Util.md5(passwd), null);
				if (null == eucUser) {
					return redirectTo(model, request, "系统错误，请使用手动注册", paramMap);
				}
				log.debug("自动注册成功：" + mobile);
				request.setAttribute("user", mobile);
				request.setAttribute("pass", passwd);
				return "forward:/inlogin" + getQueryParam(paramMap);
			}
		}
	}

	@RequestMapping("/smsregist")
	public String registBySms(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String passwd = request.getParameter("pwd");
		Map paramMap = getRequestMap(request);
		paramMap.remove("mobile");
		paramMap.remove("pwd");
		if (null == mobile || null == passwd) {
			return "redirect:/login" + this.getQueryParam(paramMap);
		}
		if (!FormUserValidator.checkMobile(mobile)) {
			return "redirect:/login" + this.getQueryParam(paramMap);
		} else {
			EucUser user = eucUserService.queryUserInfoByMobile(mobile);
			if (null != user) {
				return "redirect:/login" + this.getQueryParam(paramMap);
			} else {
				String pwd2 = mobileService.getRegisterMobile(mobile);
				String pwd = MD5Util.md5(passwd);
				if (pwd2 == null)
					return redirectTo(model, request, "临时密码已失效，请重新注册", paramMap);
				if (log.isDebugEnabled()) {
					log.debug("获取的pwd为: " + pwd2 + " 参数pwd:" + pwd);
				}
				if (pwd.equals(pwd2)) {
					// 添手机号信息入库
					EucUser eucUser = eucUserService.insertUserForMobileRegist(
							mobile, pwd, null);
					if (null == eucUser) {
						return "redirect:/login" + this.getQueryParam(paramMap);
					}
					log.debug("短信注册成功：" + mobile);
					request.setAttribute("user", mobile);
					request.setAttribute("pass", passwd);
					return "forward:/inlogin" + getQueryParam(paramMap);
				} else {
					return "redirect:/login" + this.getQueryParam(paramMap);
				}
			}
		}
	}

	/**
	 * 去掉参数中的mobile,veriCode,重新定向到指定页面
	 * 
	 * @param model
	 * @param request
	 * @param message
	 * @return
	 */
	private String redirectTo(ModelMap model, HttpServletRequest request,
			String message, Map paramMap) {
		log.info(message);
		model.addAttribute("redirUrl", getPath(request, "/mregist", paramMap));
		model.addAttribute("retMessage", message + "，系统将在3 秒后返回...");
		return "result";
	}
}