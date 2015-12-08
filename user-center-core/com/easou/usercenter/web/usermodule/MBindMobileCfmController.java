package com.easou.usercenter.web.usermodule;

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

import com.easou.common.constant.Way;
import com.easou.usercenter.asyn.AsynGameManager;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormVerify;
import com.easou.usercenter.web.user.ArrestValidation;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user")
public class MBindMobileCfmController extends MUserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;
	@Resource(name = "asynGameManager")
	private AsynGameManager asynGameManager;

	@RequestMapping(value="/mbindcfm", method=RequestMethod.POST)
	public String execute(@ModelAttribute("formVerify") FormVerify FormVerify,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			String index = SSOConfig.getProperty("domain.readonly") + "/user/usersetting.html?m=1";
			return "redirect:"+index;
		}
		// 渠道
		String channel = ConditionUtil.getChannel(request);
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String mobile = request.getParameter("mobile");
		String action = request.getParameter("act");
		model.addAttribute("id", id);
		model.addAttribute("mobile", mobile);
		if ("verify".equals(action)) { // 进行验证
			String curMobile = eucUserService.queryUserInfoById(id).getMobile();
			if (!FormUserValidator.checkMobile(mobile)) {
				log.debug("手机号不合法");
				BizLogUtil.bindLog(uid, id, curMobile, mobile,
						Way.PAGE, BindActivity.VERIFICATION, "",
						LogConstant.RESULT_FAILURE, av.getBackUrl(), channel,
						esid, LogConstant.BING_MSISDN_ILLEGAL);
				result.reject("notvaildMobile", "手机号不合法");
				model.addAttribute("id", id);
				model.addAttribute("mobile", mobile);
				model.addAttribute("msg","手机号不合法");
				return "usermodule/phone-binding";
			}
			EucUser user = eucUserService.queryUserInfoByMobile(mobile);
			if (null != user) {
				log.debug("该手机号已被注册");
				BizLogUtil.bindLog(uid, id, curMobile, mobile,
						Way.PAGE, BindActivity.VERIFICATION, "",
						LogConstant.RESULT_FAILURE, av.getBackUrl(), channel,
						esid, LogConstant.BING_MSISDN_EXIST);
				result.reject("repeatMobile", "该手机号已被注册");
				model.addAttribute("id", id);
				model.addAttribute("mobile", mobile);
				model.addAttribute("msg","该手机号已被注册");
				return "usermodule/phone-binding";
			} else {
				String veriCode = request.getParameter("veriCode");
				String cacheVeriCode = mobileService
						.getVeriCodeByMobile(mobile);
				if (null == cacheVeriCode) {
					log.debug("验证码已失效");
					BizLogUtil.bindLog(uid, id, curMobile, mobile,
							Way.PAGE, BindActivity.VERIFICATION,
							veriCode != null ? veriCode : "",
							LogConstant.RESULT_FAILURE, av.getBackUrl(),
							channel, esid, LogConstant.BING_MSISDN_CODE_FAIL);
					result.reject("notvalidVcode", "验证码已失效");
					model.addAttribute("id", id);
					model.addAttribute("mobile", mobile);
					model.addAttribute("msg","验证码已失效");
					return "usermodule/phone-binding-cfm";
				} else {
					if (cacheVeriCode.equalsIgnoreCase(veriCode)) {
						eucUserService.updateMobileById(id, mobile);
						mobileService.delVeriCodeByMobile(mobile);
						log.debug("手机号绑定成功");
						BizLogUtil.bindLog(uid, id, curMobile,
								mobile, Way.PAGE, BindActivity.VERIFICATION,
								cacheVeriCode, LogConstant.RESULT_SUCCESS, av
										.getBackUrl(), channel, esid,
								LogConstant.BING_MSISDN_SUCCESS);
						model.addAttribute("redirUrl",getDefaultBackUrl(request));
						//model.addAttribute("redirUrl", av.getBackUrl());
						model.addAttribute("retMessage", "修改成功，系统将在3 秒后返回");
						
						/*TODO 异步调用送EB接口*/
						/*if(request.getParameter("chn")!=null
								&&"33".equals(request.getParameter("chn").toString())){
						    asynGameManager.addEb(id, mobile);
						}*/
						String index = SSOConfig.getProperty("domain.readonly") + "/user/usersetting.html?m=1";
						return "redirect:"+index;
					} else {
						log.debug("验证码错误");
						BizLogUtil.bindLog(uid, id, curMobile,
								mobile, Way.PAGE, BindActivity.VERIFICATION,
								cacheVeriCode + "|" + veriCode,
								LogConstant.RESULT_SUCCESS, av.getBackUrl(),
								channel, esid,
								LogConstant.BING_MSISDN_CODE_ERROR);
						result.reject("errorVcode", "验证码错误");
						model.addAttribute("id", id);
						model.addAttribute("mobile", mobile);
						model.addAttribute("msg","验证码错误");
						return "usermodule/phone-binding-cfm";
					}
				}
			}
		} else { // 开始验证
			model.addAttribute("formVerify", new FormVerify());
//			result.reject("vcodeIssend", "验证码已发送至您的手机，请注意查收");
			return "usermodule/phone-binding-cfm";
		}
	}
	
	@RequestMapping(value="/mbindcfm", method=RequestMethod.GET)
	public String bindCfmBySms(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		String mobile = request.getParameter("mobile");
		if (null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			String index = SSOConfig.getProperty("domain.readonly") + "/user/usersetting.html?m=1";
			return "redirect:"+index;
		}
		String veriCode = mobileService.getVeriCodeByMobile(mobile);
		if(veriCode != null && !"".equals(veriCode)) {
			model.addAttribute("msg", "该手机号已发送了验证码");
		}
		model.addAttribute("id", id);
		model.addAttribute("mobile", mobile);
		return "usermodule/phone-binding-cfm";
	}

	public AsynGameManager getAsynGameManager() {
		return asynGameManager;
	}

	public void setAsynGameManager(AsynGameManager asynGameManager) {
		this.asynGameManager = asynGameManager;
	}
}
