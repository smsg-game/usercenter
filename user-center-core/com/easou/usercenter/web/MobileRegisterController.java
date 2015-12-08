package com.easou.usercenter.web;

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

import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.form.FormUser;

@Controller
@RequestMapping("/mregist")
public class MobileRegisterController extends BaseController {

//	@Resource(name = "eucUserServiceImpl")
//	private EucUserService eucUserService;
//
//	@Autowired
//	private MobileService mobileService;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request, final HttpServletResponse response) {
		model.addAttribute("formUser", new FormUser());
		String uid = ConditionUtil.getUid(request);
		String qn = ConditionUtil.getQn(request);
		String agent = ConditionUtil.getAppAgent(request);
		return "default/ui/register";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
//		String mobile = formUser.getMobile();
//		//重定向地址
//		String service = request.getParameter("service")!=null?request.getParameter("service"):"";
//        String channel = ConditionUtil.getChannel(request);
//        String esid = ConditionUtil.getEsid(request);
//        String uid = ConditionUtil.getUid(request);
//		if (!FormUserValidator.checkMobile(mobile)) {
//			log.debug("手机号不合法");
//			result.reject("FormUser.mobile[notvaild]", "手机号不合法");
//			//注册成功，记录日志
//			BizLogUtil.registLog(ConditionUtil.getUid(request),mobile,Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE,  service,"",
//					LogConstant.REGIS_RESULT_MSISDN_ILLEGAL,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE);		
//			return "default/ui/register";
//		}
//		EucUser user = eucUserService.queryUserInfoByMobile(mobile);
//		if (null != user) {
//			log.debug("该手机号已被注册");
//			result.reject("FormUser.mobile[repeat]", "该手机号已被注册");
//			BizLogUtil.registLog(ConditionUtil.getUid(request),mobile,Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, service,"",
//					LogConstant.REGIS_RESULT_MSISDN_EXIST,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE);		
//			return "default/ui/register";
//		} else {
//			String pass = RandomKeyGenerator.genNumber(6);
//			String rst = mobileService.registerMobile(mobile, pass);
//			if (MobileService.SUCCESS.equals(rst)) {
//				Map paramMap = getRequestMap(request);
//				paramMap.put("mobile",mobile);
//				paramMap.put("pwd",pass);
//				String longUrl = getPath(request, "/smsregist", paramMap);
//				String shortUrl = getShortUrl(request, longUrl);
//				log.debug("短链接为 " + shortUrl);
//				StringBuffer message = new StringBuffer("欢迎注册梵町,您的密码是").append(
//						pass).append(",请点击 ").append(shortUrl).append(" 完成注册");
//				StringBuffer messageLog = new StringBuffer("欢迎注册梵町,您的密码是******").append(",请点击 ").append(shortUrl).append(" 完成注册");
//				//if (SendMessageUtil.send(uid,mobile, message.toString(),channel,esid,SMSType.REGISTER)) {
//				    SendSmsHelper.sendSms(uid,mobile, message.toString(),messageLog.toString(),
//				    		channel,esid,SMSType.REGISTER);
//					log.debug("密码发送成功");
//					paramMap.remove("mobile");
//					paramMap.remove("pwd");
//					paramMap.put("user", mobile);
//					model.addAttribute("redirUrl", getLoginPath(request, paramMap));
//					model.addAttribute("retMessage", "密码发送成功，请使用收到的密码登录");
//					BizLogUtil.registLog(ConditionUtil.getUid(request),mobile,Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS,  service,"",
//							LogConstant.REGIS_RESULT_SUCCESS,channel,esid,LogConstant.REGIS_ACTION_MSISDN_REQUEST);	
//					return "result";
//				/*} else {// TODO 调整
//					log.debug("短信发送失败");
//					result.reject("smsError", "短信发送失败，请稍后重试");
//					mobileService.delRegisterMobile(mobile);
//					BizLogUtil.registLog(ConditionUtil.getUid(request),mobile,Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE,  service,"",
//							LogConstant.REGIS_RESULT_MSISDN_SMS_FAILURE,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE);	
//					return "default/ui/register";
//				}*/
//
//			} else if (MobileService.REG_REPEAT_ERR.equals(rst)) {
//				result.reject("mobileDoing", "该手机号正在注册中");
//				BizLogUtil.registLog(ConditionUtil.getUid(request),mobile,Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, service,"",
//						LogConstant.REGIS_RESULT_MSISDN_EXIST,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE);	
//				return "default/ui/register";
//			} else {
//				log.warn("注册失败: " + rst);
//				result.reject("regFail", "注册失败");
//				BizLogUtil.registLog(ConditionUtil.getUid(request),mobile,Way.PAGE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, service,"",
//						LogConstant.REGIS_RESULT_EXCEPTION,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE);	
//				return "default/ui/register";
//			}
//		}
		return "";
	}

//	private String getLoginPath(HttpServletRequest request, Map paramMap) {
//		String loginPath=SSOConfig.getProperty("domain.readonly");
//		StringBuffer sb=new StringBuffer(loginPath);
//		if (!"/".equals(request.getContextPath())) {
//			sb.append(request.getContextPath());
//		}
//		sb.append("/login");
//		if(paramMap.size()>0) {
//			sb.append("?");
//		}
//		for (Iterator iterator = paramMap.keySet().iterator(); iterator.hasNext();) {
//			String key = (String) iterator.next();
//			sb.append(key).append("=");
//			sb.append((String)paramMap.get(key));
//			if(iterator.hasNext()) {
//				sb.append("&");
//			}
//		}
//		log.debug(sb.toString());
//		return sb.toString();
//	}
}
