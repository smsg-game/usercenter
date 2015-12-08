package com.easou.usercenter.web.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.util.MobileUtil;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;

@Controller
@RequestMapping("/user/check")
public class CheckUserIdentifiedController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;
	
	private static final String BIND = "bindMobile";
	private static final String JBIND = "jbMobile";

	@RequestMapping("/show")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		if(!av.isLogin()) {
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		if (null == user) {
			// 该id用户不存在
			removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		
		//清除已使用的验证码
		mobileService.delVeriCodeByMobile(user.getMobile());
		
		model.addAttribute("id",user.getId());
		model.addAttribute("mobile",MobileUtil.secertString(user.getMobile()));
		model.addAttribute("email",MobileUtil.secertString(user.getEmail()));
		model.addAttribute("nickName", user.getNickName());
		model.addAttribute("type", request.getParameter("type"));
		return "default/ui/checkUserIdentified";
	}

	@RequestMapping("/getVeriCode")
	public String getVeriCode(final HttpServletRequest request, final HttpServletResponse response, 
			ModelMap model) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
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
		String mobile = eucUserService.queryUserInfoById(id).getMobile();
		model.addAttribute("id", id);
		model.addAttribute("mobile", MobileUtil.secertString(mobile));
		model.addAttribute("type", request.getParameter("type"));
		
		
		// 当前用户信息
		String curMobile = eucUserService.queryUserInfoById(id).getMobile();
		if (null == curMobile) {
			curMobile = "";
		}
		
		String veriCode = mobileService.setVeriCodeByMobile(mobile);
		// 需要发短信
		String message = "验证码: "+veriCode+", 感谢您对梵町的支持!";
		String messageLog = "验证码: xxxx, 感谢您对梵町的支持!";

		SendSmsHelper.sendSms(uid, mobile, message,messageLog, channel, esid,
				SMSType.BIND);
		log.debug("验证码发送成功");
		BizLogUtil.bindLog(uid, av.getUserId(), curMobile, mobile,
				Way.PAGE, BindActivity.REQUEST, veriCode,
				LogConstant.RESULT_SUCCESS, av.getBackUrl(), channel,
				esid, LogConstant.BING_MSISDN_PUSH_SMS_SUCCESS);
		
//		model.addAttribute("redirUrl", "/user/check/show");
		return "default/ui/checkUserVeriCode";
	}
	
	@RequestMapping("/checkUserIdentified")
	public String checkUserIdentified(final HttpServletRequest request, final HttpServletResponse response, 
			ModelMap model) {
		ArrestValidation av = checkLogin(request, model);
		String id = request.getParameter("id");
		if (!av.isLogin() || null == id || !id.equals(av.getUserId())) {
			this.removeLoginAssert(request);
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "你无权操作，系统将在3 秒后返回...");
			return "result";
		}
		
		model.addAttribute("id", id);
		model.addAttribute("type", request.getParameter("type"));
		
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		if (null == user) {
			model.addAttribute("errorUser", "用户不存在!");
			return "default/ui/checkUserVeriCode";
		}
		
		final String mobile = user.getMobile();
		model.addAttribute("mobile", MobileUtil.secertString(mobile));
		
		
		String veriCode = request.getParameter("veriCode");
		if(veriCode == null || veriCode.length() == 0){
			veriCode = "";
		}
		
		final String veriCodeEuc = mobileService.getVeriCodeByMobile(mobile);
		if (veriCode.equalsIgnoreCase(veriCodeEuc)) {
			mobileService.delVeriCodeByMobile(mobile);
			request.getSession().setAttribute("ID_IDENTIFIED", Boolean.TRUE.toString());
			
			//清理所有参数
			model.clear();
			
			final String type = request.getParameter("type");
			if(BIND.equals(type)){
				return "redirect:/user/bindMobile";
			}else {
				return "redirect:/user/jbMobile";
			}
		} else {
			// 验证码不匹配
			model.addAttribute("notvaildMobile", "验证码错误!");
			return "default/ui/checkUserVeriCode";
		}
	}
	
}
