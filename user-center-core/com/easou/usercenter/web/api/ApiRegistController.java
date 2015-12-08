package com.easou.usercenter.web.api;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.cas.auth.EucToken;
import com.easou.cas.authenticateion.Md5PwdEncoder;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.AppType;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.DESPlus;
import com.easou.common.util.MD5Util;
import com.easou.common.util.RandomKeyGenerator;
import com.easou.common.util.StringUtil;
import com.easou.session.SessionId;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api/")
public class ApiRegistController extends AbstractAPIController {
	// 用户登录类型
	public static final String LOGIN_TYPE = "loginType";
	// 应用类型
	public static final String APP_TYPE = "appType";
	// 记住密码标识
	public static final String REMEMBER_PASSWORD = "remember";
	
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;
	
	@Resource
	CentralAuthenticationService centralAuthenticationService;
	
	@Autowired
	Md5PwdEncoder pwdEncoder;
	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;
	@Resource(name = "asynUserManager")
	private AsynManager asynUserManager;

	@RequestMapping("registByName")
	@ResponseBody
	public String registByName(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				RequestInfo info = fetchRequestInfo(request);
				String username = getRequestBean().getBody().getString("username");
				String password = getRequestBean().getBody().getString("password");
				if(null!=username) {
					username=username.toLowerCase();
				}
				String s = FormUserValidator.checkUserName(username);
				if(!"".equals(s)) {
					jdesc.addReason("1", s);
				}
				s = new String(FormUserValidator.checkPassword(password));
				if(!"".equals(s)) {
					jdesc.addReason("2", s);
				}
				if (jdesc.size() > 0) {
					// 出现用户名密码错误
					BizLogUtil.registLog(info,username, null, Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
					return jbean;
				}
				
				if(null!=eucUserService.queryUserInfoByName(username)) {
					BizLogUtil.registLog(info,username, null, Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_NAME_EXIST,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
					return busiErrorBean(jbean,jdesc,"3","该名称已注册");
				}
				
				EucUser eucUser = eucUserService.insertUserForUnameRegist(username, MD5Util.md5(password), qn);
				if (null != eucUser) {
					JBody jbody = new JBody();
					jbody.putContent("user", transJUser(eucUser,new ExpJUser()));
					jbean.setBody(jbody); // 用户注册成功，返回用户实体
					BizLogUtil.registLog(info,username, eucUser.getId(), Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_SUCCESS, "",
							LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
				} else {
					BizLogUtil.registLog(info,username, null, Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_EXCEPTION,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
					return busiErrorBean(jbean,jdesc,"4","注册失败");
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}
	
	@RequestMapping("regByRealMobile")
	@ResponseBody
	public String regByRealMobile(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				RequestInfo info = fetchRequestInfo(request);
				String mobile = getRequestBean().getBody().getString("mobile");
				String passwd = getRequestBean().getBody().getString("password");
				String logMobile = mobile!=null?mobile:"";

				if (!FormUserValidator.checkMobile(mobile)) {
					BizLogUtil.registLog(info,logMobile, null,
							Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_MSISDN_ILLEGAL,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					log.debug("手机号不合法");
					return busiErrorBean(jbean,jdesc,"1","手机号不合法");
				}
				String s = FormUserValidator.checkPassword(passwd);
				if(!"".equals(s)) {
					BizLogUtil.registLog(info,logMobile, null, Way.INTERFACE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
					return busiErrorBean(jbean,jdesc,"2",s);
				}
				
				EucUser user = eucUserService.queryUserInfoByMobile(mobile);
				if (null != user) {
					BizLogUtil.registLog(info,logMobile, null, 
							Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_MSISDN_EXIST,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);	
					log.debug("该手机号已被注册");
					return busiErrorBean(jbean,jdesc,"3","该手机号已被注册,可直接登录");
				} else {
					EucUser eucUser = eucUserService.insertUserForMobileRegist(mobile, MD5Util.md5(passwd), qn);
					if (null!=eucUser) {
						if(log.isDebugEnabled()) {
							log.debug(mobile + "注册成功, pass:" + passwd);
						}
						JBody jbody = new JBody();
						jbody.putContent("user", transJUser(eucUser,new ExpJUser()));
						jbean.setBody(jbody); // 用户注册成功，返回用户实体
						BizLogUtil.registLog(info,eucUser.getName(), eucUser.getId(), Way.INTERFACE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS, "",
								LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					} else {
						log.warn("注册失败: " + mobile);
						BizLogUtil.registLog(info,logMobile, null, 
								Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
								LogConstant.REGIS_RESULT_EXCEPTION,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
						return busiErrorBean(jbean,jdesc,"4","注册失败");
					}
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	@RequestMapping("registByMobile")
	@ResponseBody
	public String registByMobile(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				RequestInfo info = fetchRequestInfo(request);
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				String mobile = getRequestBean().getBody().getString("mobile");
				String logMobile = mobile != null ? mobile : "";
				if (!FormUserValidator.checkMobile(mobile)) {
					BizLogUtil.registLog(info,logMobile,
							null, Way.INTERFACE,
							LogConstant.REGIS_TYPE_MSISDN,
							LogConstant.RESULT_FAILURE, "", 
							LogConstant.REGIS_RESULT_MSISDN_ILLEGAL,
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					log.debug("手机号不合法");
					return busiErrorBean(jbean, jdesc, "1", "手机号不合法");
				}
				EucUser user = eucUserService.queryUserInfoByMobile(mobile);
				if (null != user) {
					BizLogUtil.registLog(info,
							logMobile, null, Way.INTERFACE,
							LogConstant.REGIS_TYPE_MSISDN,
							LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_MSISDN_EXIST,
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					log.debug("该手机号已被注册");
					return busiErrorBean(jbean, jdesc, "2", "该手机号已被注册,请直接登录");
				} else {
					String pass = RandomKeyGenerator.genNumber(6);
					String rst = mobileService.registerMobile(mobile, pass);
					if (MobileService.SUCCESS.equals(rst)) {
						if (log.isDebugEnabled()) {
							log.debug(mobile + "注册写入缓存成功, pass:" + pass);
						}
						JBody jbody = new JBody();
						jbody.putContent("tpass", "");
						jbean.setBody(jbody);
						if (isSendMsg(getRequestBean().getBody())) {
							// 需要发短信
							String message = "欢迎注册梵町,您的密码是" + pass;
							String messageLog = "欢迎注册梵町,您的密码是******";
							SendSmsHelper.sendSms(info.getUid(),
									mobile, message,messageLog,
									info.getSource(),
									info.getEsid(),
									SMSType.REGISTER);
						}
						/**注释注册请求日志，即只记成功注册的*/
//						BizLogUtil.registLog(info,
//								logMobile, null, Way.INTERFACE,
//								LogConstant.REGIS_TYPE_MSISDN,
//								LogConstant.RESULT_SUCCESS, "", 
//								LogConstant.REGIS_RESULT_SUCCESS,
//								LogConstant.REGIS_ACTION_MSISDN_REQUEST);
					} else if (MobileService.REG_REPEAT_ERR.equals(rst)) {
						return busiErrorBean(jbean, jdesc, "3", "该手机号正在注册中");
					} else {
						BizLogUtil.registLog(info,
								logMobile, null, Way.INTERFACE,
								LogConstant.REGIS_TYPE_MSISDN,
								LogConstant.RESULT_FAILURE, "", 
								LogConstant.REGIS_RESULT_EXCEPTION,
								LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
						log.warn("注册失败: " + rst);
						return busiErrorBean(jbean, jdesc, "4", "注册失败");
					}
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}
	
	
	@RequestMapping("autoAKeyRegist")
	@ResponseBody
	public String autoAKeyRegist(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				RequestInfo info = fetchRequestInfo(request);
			    JBody jbody = new JBody();
			  //应用类型
				AppType appType = AppType.WEB;	    
			    // 记住密码
				String remember = getRequestBean().getBody().getString(
						REMEMBER_PASSWORD);
				// 登录类型
//				String type = getRequestBean().getBody().getString(LOGIN_TYPE);		s		
				// 应用类型
				String aType = getRequestBean().getBody().getString(APP_TYPE);
				if (!StringUtil.isEmpty(aType) 
						&& AppType.getAppType(aType) != null) {
					appType = AppType.getAppType(aType);
					if (log.isDebugEnabled()) {
						log.debug("app type[" + appType.type + "]");
					}
				}
				
				//是否需要登录
				boolean isNeedLogin = 
						getRequestBean().getBody().get("isNeedLogin")!=null?
								getRequestBean().getBody().getBoolean("isNeedLogin"):false;
				
				String passwd = RandomKeyGenerator.genNumber(6);	
				EucUser eucUser = eucUserService.insertUserByDefault(passwd, null, qn);
				if(null!=eucUser){
					//
					ExpJUser expJUser =(ExpJUser)transJUser(eucUser,new ExpJUser());
					//保存密码
					expJUser.setPasswd(passwd);
					BizLogUtil.registLog(info,eucUser.getName(), eucUser.getId(), Way.INTERFACE,LogConstant.REGIS_TYPE_A_KEY, LogConstant.RESULT_SUCCESS, "",
							LogConstant.REGIS_RESULT_SUCCESS,"", qn, appId);
					if(isNeedLogin){
						//TODO 实现登录
						// 创建用户登陆对象
						UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
						credentials.setUsername(expJUser.getName());
						credentials.setPassword(passwd);
						// 验证用户信息，获取TGT
						try {
							String ticketGrantingTicketId = centralAuthenticationService
									.createTicketGrantingTicket(credentials);
							EucToken token = new EucToken();
					        token.setToken(ticketGrantingTicketId);
					        //生成登录后的esid
					        String nEsid = SessionId.createLogonId(expJUser.getId(), info.getEsid(), expJUser.getRandomPasswd());
					        jbody.put("esid", nEsid);
					        jbody.put("token", token);
					        jbody.put("user", expJUser);
					        jbean.setBody(jbody);
					        BizLogUtil.loginLog(info, String.valueOf(eucUser.getId()), "", Way.INTERFACE,
	    							LogConstant.RESULT_SUCCESS, LoginType.DEFAULT, "", ticketGrantingTicketId,LogConstant.REGIS_RESULT_SUCCESS, "", qn, appId, eucUser.getRegisterTime());
					     // 将用户信息保存到COOKIE中
							if (appType != AppType.APP) {//如果应用类型不是APP类型，则需要写COOKIE
								if ("true".equals(remember)) {
									DESPlus des = new DESPlus();
									String u = des.encrypt((credentials.getUsername()
											+ "$" + pwdEncoder.encode(credentials
											.getPassword())));
									jbody.put(CookieUtil.COOKIE_U, u);
									jbody.put("uDomain", uinfoCookieGenerator
											.getCookieDomain());
									jbody.put("uPath", uinfoCookieGenerator
											.getCookiePath());
									jbody.put("uAge", uinfoCookieGenerator
											.getCookieMaxAge());
								}

								jbody.put(CookieUtil.COOKIE_TGC, ticketGrantingTicketId);
								jbody.put("tgcDomain",
										ticketGrantingTicketCookieGenerator
												.getCookieDomain());
								jbody.put("tgcPath", ticketGrantingTicketCookieGenerator
										.getCookiePath());
								jbody.put("tgcAge", ticketGrantingTicketCookieGenerator
										.getCookieMaxAge());
							}
						} catch (TicketException e) {
							JDesc jdesc = new JDesc(); // 错误描述
							log.error("登录失败",e);
							BizLogUtil.loginLog(info, String.valueOf(eucUser.getId()), eucUser.getName(), Way.INTERFACE,
									LogConstant.RESULT_FAILURE, LoginType.DEFAULT, "", 
									"",LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", qn, appId, null);
							return busiErrorBean(jbean,jdesc,"9", "TGT生成失败，可能是密码错误");
						}
					}else{//不进行登录
					    jbody.put("user", expJUser);
		                jbean.setBody(jbody);
					}
				}else{
					// 注册不成功
					BizLogUtil.registLog(info,
							"", null, Way.INTERFACE,
							LogConstant.REGIS_TYPE_MSISDN,
							LogConstant.RESULT_FAILURE, "", 
							LogConstant.REGIS_RESULT_EXCEPTION,
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					JDesc jdesc = new JDesc(); // 错误描述
					return busiErrorBean(jbean,jdesc,"4","注册失败");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
}
