package com.easou.usercenter.web.user;

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
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.form.FormVerify;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/user/bindcfm")
public class BindMobileCfmController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;
	@Resource(name = "asynGameManager")
	private AsynGameManager asynGameManager;

	@RequestMapping(method=RequestMethod.POST)
	public String execute(@ModelAttribute("formVerify") FormVerify FormVerify,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			return "result";
		}
		// 渠道
		String channel = ConditionUtil.getChannel(request);
		String esid = ConditionUtil.getEsid(request);
		String uid = ConditionUtil.getUid(request);
		String mobile = request.getParameter("mobile");
		String action = request.getParameter("act");
//		model.addAttribute("id", id);
//		model.addAttribute("mobile", mobile);
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
				return "default/ui/bindMobile";
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
				return "default/ui/bindMobile";
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
					return "default/ui/bindMobile";
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
						
						//清除已使用的验证码
						mobileService.delVeriCodeByMobile(mobile);
						return "result";
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
						return "default/ui/bindMobileCfm";
					}
				}
			}
		} else { // 开始验证
			model.addAttribute("formVerify", new FormVerify());
//			result.reject("vcodeIssend", "验证码已发送至您的手机，请注意查收");
			return "default/ui/bindMobileCfm";
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String bindCfmBySms(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		return "redirect:/user/userCenter" + getQueryString(request);
	}

	public AsynGameManager getAsynGameManager() {
		return asynGameManager;
	}

	public void setAsynGameManager(AsynGameManager asynGameManager) {
		this.asynGameManager = asynGameManager;
	}
	
//		ArrestValidation av = checkLogin(request);
//		String channel = ConditionUtil.getChannel(request);
//		String esid = ConditionUtil.getEsid(request);
//		String uid = ConditionUtil.getUid(request);
//
//		// 获取当前用户信息
//		EucUser cerUser = eucUserService.queryUserInfoById(av.getUserId());
//		String mobile = request.getParameter("mobile");
//		String veriCode = request.getParameter("veriCode");
//		// EucMessage msg = new EucMessage();
//		if (!FormUserValidator.checkMobile(mobile)) {
//			BizLogUtil.bindLog(uid,av.getUserId(),cerUser!=null?cerUser.getMobile():"", mobile, 
//					Way.PAGE,BindActivity.VERIFICATION,veriCode!=null?veriCode:"", LogConstant.RESULT_FAILURE, av.getBackUrl(),channel,esid,LogConstant.BING_MSISDN_ILLEGAL);
//			return redirectTo(model, request, "手机号不合法");
//		}
//		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
//		if (null != user) {
//			BizLogUtil.bindLog(uid,av.getUserId(),cerUser!=null?cerUser.getMobile():"", mobile, 
//					Way.PAGE,BindActivity.VERIFICATION,veriCode!=null?veriCode:"", LogConstant.RESULT_FAILURE, av.getBackUrl(),channel,esid,LogConstant.BING_MSISDN_EXIST);
//			return redirectTo(model, request, "该手机号已被注册");
//		} else {
//			String cacheVeriCode = mobileService.getVeriCodeByMobile(mobile);
//			if (null == cacheVeriCode) {
//				BizLogUtil.bindLog(uid,av.getUserId(),cerUser!=null?cerUser.getMobile():"", mobile, 
//						Way.PAGE,BindActivity.VERIFICATION,veriCode!=null?veriCode:"", LogConstant.RESULT_FAILURE, av.getBackUrl(),channel,esid,LogConstant.BING_MSISDN_EXIST);
//				return redirectTo(model, request, "验证码已失效");
//			} else {
//				if (cacheVeriCode.equalsIgnoreCase(veriCode)) {
//					EucUser updUser = new EucUser();
//					updUser.setMobile(mobile);
//					eucUserService.updateUserById(av.getUserId(), updUser);
//					mobileService.delVeriCodeByMobile(mobile);
//					log.debug("手机号绑定成功");
//					model.addAttribute("redirUrl", av.getBackUrl());
//					model.addAttribute("retMessage", "修改成功，系统将在3 秒后返回");
//					BizLogUtil.bindLog(uid,av.getUserId(),cerUser!=null?cerUser.getMobile():"", mobile, 
//							Way.PAGE,BindActivity.VERIFICATION,cacheVeriCode,LogConstant.RESULT_SUCCESS, av.getBackUrl(),channel,esid,LogConstant.BING_MSISDN_SUCCESS);
//					return "result";
//				} else {
//					BizLogUtil.bindLog(uid,av.getUserId(),cerUser!=null?cerUser.getMobile():"", mobile, 
//							Way.PAGE,BindActivity.VERIFICATION, cacheVeriCode+"|"+veriCode,LogConstant.RESULT_FAILURE, av.getBackUrl(),channel,esid,LogConstant.BING_MSISDN_CODE_ERROR);
//					return redirectTo(model, request, "验证码错误");
//				}
//			}
//		}
//	
//	/**
//	 * 去掉参数中的mobile,veriCode,重新定向到指定页面
//	 * @param model
//	 * @param request
//	 * @param message
//	 * @return
//	 */
//	private String redirectTo(ModelMap model, HttpServletRequest request, String message) {
//		Map paramMap = getRequestMap(request);
//		paramMap.remove("mobile");
//		paramMap.remove("veriCode");
//		log.info(message);
//		model.addAttribute("redirUrl", getPath(request, "/user/sbind", paramMap));
//		model.addAttribute("retMessage", message + "，系统将在3 秒后返回...");
//		return "result";
//	}
}
