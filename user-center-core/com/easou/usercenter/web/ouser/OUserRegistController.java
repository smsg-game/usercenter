package com.easou.usercenter.web.ouser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.form.FormUser;

@Controller
@RequestMapping("/oregist")
public class OUserRegistController extends BaseController {

//	@Resource(name="oUserService")
//	private OUserService oUserService;
//	
//	@Autowired
//	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model) {
		model.addAttribute("formUser", new FormUser());
		return "default/ui/nameRegist";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formUser") FormUser formUser,
			BindingResult result, final HttpServletRequest request,
			final HttpServletResponse response) {
//		try {
//			String username = formUser.getUsername().toLowerCase().trim();
//			String password = formUser.getPassword();
//			String confirm = formUser.getConfirm();
//
//			validator.validate(formUser, result);
//			String service = request.getParameter("service")!=null?
//					request.getParameter("service"):"";
//			String channel = ConditionUtil.getChannel(request);
//			String uid = ConditionUtil.getUid(request);
//			String esid = ConditionUtil.getEsid(request);
//			String appAgent = ConditionUtil.getAppAgent(request);
//			if (result.hasErrors()) {
//				log.debug("验证失败");
//				BizLogUtil.registLog(uid,username, null, Way.PAGE,LogConstant.REGIS_TYPE_EXTERNAL_USER, LogConstant.RESULT_FAILURE, service,"",
//						LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, appAgent);				
//				return "default/ui/nameRegist";
//			}
//
//			boolean error = false;
//			String s = new String(FormUserValidator.checkUserName(username));
//			if (!"".equals(s)) {
//				result.reject("FormUser.username[check]", s);
//				error = true;
//			}
//			s = new String(FormUserValidator.checkPassword(password));
//			if (!"".equals(s)) {
//				result.reject("FormUser.password[check]", s);
//				error = true;
//			}
//			s = new String(FormUserValidator
//					.checkCfmPassword(password, confirm));
//			if (!"".equals(s)) {
//				result.reject("FormUser.password[confirm]", s);
//				error = true;
//			}
//			if (error) {
//				// 帐号密码不符规则
//				BizLogUtil.registLog(uid,username, null, Way.PAGE,LogConstant.REGIS_TYPE_EXTERNAL_USER,LogConstant.RESULT_FAILURE, service,"",
//						LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, appAgent);
//				log.debug("表单校验失败");return "default/ui/nameRegist";
//			}
//
//			EucUser user = oUserService.queryUserInfoByName(username);
//			if (null != user) {
//				log.debug("该用户已注册");
//				result.reject("FormUser.username[repeat]", "登录名已注册");
//				BizLogUtil.registLog(uid,username, null, Way.PAGE,LogConstant.REGIS_TYPE_EXTERNAL_USER, LogConstant.RESULT_FAILURE,  service,LogConstant.LOGIN_TYPE_EXTERNAL_USER_SINA_WEIBO,
//						LogConstant.REGIS_RESULT_NAME_EXIST,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, appAgent);
//				return "default/ui/nameRegist";
//			}
//			ThirdPartUserInfo tinfo = (ThirdPartUserInfo) request.getSession()
//					.getAttribute(OUserConstant.THIRDPART_INFO_SESSION);
//			if (null == tinfo) {
//				log.debug("不是外部用户注册");
//				return "redirect:/login" + getQueryString(request);
//			} else {
//				if (log.isDebugEnabled()) {
//					log.debug("外部用户信息: " + tinfo.getThirdId() + " 网站类型: " + tinfo.getType());
//				}
//				//对应的日志类型
//			    String logType = getLogLoginType(tinfo.getType());
//				OUser ouser = oUserService.queryBindinInfo(tinfo.getThirdId(), tinfo.getType());
//				if(null!=ouser) {
//					log.debug("外部用户已存在，非法操作");
//					BizLogUtil.registLog(uid,username, null, Way.PAGE,LogConstant.REGIS_TYPE_EXTERNAL_USER, LogConstant.RESULT_FAILURE,service,logType,
//							LogConstant.REGIS_RESULT_EXTERNAL_USER_SINA_BIND_EXIST,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, appAgent);
//					return "redirect:/login" + getQueryString(request);
//				}
//				try {
//					EucUser eucUser = oUserService.insertUserAndBindRegist(
//							username, MD5Util.md5(password).toLowerCase(),
//							tinfo);
//					// 保存梵町ID号
////					((ThirdPartUserInfo) request.getSession().getAttribute(
////							OUserConstant.THIRDPART_INFO_SESSION))
////							.setEasouId(String.valueOf(eucUser.getId()));
//					// request.getSession().removeAttribute("uinfo");
//					if (null != eucUser.getId()) {
//						if (log.isDebugEnabled()) {
//							log.debug("第三方用户注册成功, 新用户id" + eucUser.getId());
//						}
//						tinfo.setEasouId(eucUser.getId()+"");
//						request.setAttribute(
//								OUserConstant.THIRDPART_INFO_SESSION, tinfo);
//						BizLogUtil
//								.registLog(uid,	username,eucUser.getId(),Way.PAGE,
//										LogConstant.REGIS_TYPE_EXTERNAL_USER,	LogConstant.RESULT_SUCCESS,
//										service,logType,LogConstant.REGIS_RESULT_SUCCESS,channel,esid,
//										LogConstant.REGIS_ACTION_MSISDN_VALIDATE, appAgent);
//						if (null != request.getQueryString()) {
//							return new StringBuffer("forward:/extLogin?")
//									.append(request.getQueryString())
//									.toString();
//						} else {
//							return "forward:/extLogin";
//						}
//					} else {
//						result.reject("FormUser.username", "注册失败");
//						BizLogUtil
//								.registLog(
//										uid,username,null,Way.PAGE,
//										LogConstant.REGIS_TYPE_EXTERNAL_USER,
//										LogConstant.RESULT_FAILURE,service,logType,
//										LogConstant.REGIS_RESULT_EXCEPTION,channel,esid,
//										LogConstant.REGIS_ACTION_MSISDN_VALIDATE,appAgent);
//						return "default/ui/nameRegist";
//					}
//				} catch (Exception e) {
//					log.error("绑定注册用户失败",e);
//					result.reject("RegistError", "注册失败");
//					BizLogUtil.registLog(uid,username, null, Way.PAGE,LogConstant.REGIS_TYPE_EXTERNAL_USER,LogConstant.RESULT_FAILURE,  service,logType,
//							LogConstant.REGIS_RESULT_EXCEPTION,channel,esid,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, appAgent);
//					return "default/ui/nameRegist";
//				}
//			}
//		} catch (Exception e) {
//			log.error(e, e);
//			return "errors";
//		}
		return null;
	}
	
//	/**
//	 * 根据外部注册类型，获取对应的日志类型
//	 * 
//	 * @param loginType
//	 *     日志类型
//	 * @return
//	 */
//	private String getLogLoginType(String loginType){
//		LoginType type = LoginType.getLoginType(loginType); 
//		if(type==LoginType.WEIBO_SINA){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_SINA_WEIBO;
//		}else if(type==LoginType.WEIBO_RENREN){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_RENREN_WEIBO;
//		}else if(type==LoginType.WEIBO_TQQ){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_TENCENT_WEIBO;
//		}else if(type==LoginType.QQ){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_QQ;
//		}else{
//			try {
//				throw new Exception("no found the loginType["+loginType+"]'s log type....");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
}
