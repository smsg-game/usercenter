package com.easou.usercenter.web.api2;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationResult;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.TokenUtils;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.cas.auth.EucToken;
import com.easou.cas.auth.EucUCookie;
import com.easou.cas.authenticateion.Md5PwdEncoder;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JHead;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.DESPlus;
import com.easou.common.util.MD5Util;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.validation.FormUserValidator;

/**
 * 登录接口控制器，提供用户名登录，忘记密码
 * 
 * @author jay
 * @since 2013.01.14
 * @version 1.0
 */
@Controller
@RequestMapping("/api2/")
public class Api2LoginController extends AbstractAPI2Controller {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	@Autowired
	private MobileService mobileService;
	@Resource
	private CentralAuthenticationService centralAuthenticationService;
	@Autowired
	private TicketRegistry ticketRegistry;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;
	@Resource(name = "asynUserManager")
	private AsynManager asynUserManager;

	@RequestMapping("login.json")
	@ResponseBody
	public String login(final HttpServletRequest request,
			final HttpServletResponse response) {

		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				// 获取用户名
				String loginName = getRequestBean().getBody().getString("username"); // 获取用户名  	此用户名：mobile、username、email都有可能
				String password = getRequestBean().getBody().getString("password");	// 获取密码
				String remember = getRequestBean().getBody().getString("remember");	// 是否返回U
				
				RequestInfo info = fetchRequestInfo(request);
				
				// 用户名密码为空
				if (StringUtil.isEmpty(loginName)|| StringUtil.isEmpty(password)) {
					BizLogUtil.loginLog(info, "", loginName != null ? loginName : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, LoginType.DEFAULT, "", "", LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", qn, appId, null);
					return busiErrorBean(jbean, "3", "用户名或密码为空");
				}
				
				String username = null;
				try {
					EucUser user = eucUserService.queryUserByIds(loginName,
							MD5Util.md5(password));
					// 如果用户不存在
					if (user == null) {
						return busiErrorBean(jbean, "3", "登录失败!");
					}
					username = user.getName();
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
					return busiErrorBean(jbean, "3", "登录失败!");
				}
				return getLoginResult(jbean, username, password, remember, info);
			}
		};
		return abc.genReturnJson();
	}

	/**
	 * 请求重设密码(发送验证码短信)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestResetPass.json")
	@ResponseBody
	public String requestResetPass(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String mobile = getRequestBean().getBody().getString("mobile");
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, "1", "手机号不合法");
				} else {
					EucUser eucUser = eucUserService
							.queryUserInfoByMobile(mobile);
					if (null == eucUser) {
						return busiErrorBean(jbean, "2", "该手机号还未注册");
					}
					String veriCode = mobileService.getVeriCodeByMobile(mobile);
					if (null == veriCode) {
						veriCode = mobileService.setVeriCodeByMobile(mobile);
					} else {
						return busiErrorBean(jbean, "3", "验证码已发送,请稍后再试");
					}
					String newpwd = veriCode;
					eucUserService.updatePasswd(eucUser.getId() + "", newpwd);
					
					log.debug("update password successful!");
					
//					JBody jbody = new JBody();
//					jbody.putContent("veriCode", "");
//					jbean.setBody(jbody);
					// 需要发短信
//					String message = "欢迎使用梵町忘记密码功能,您的验证码是" + veriCode;
//					String messageLog = "欢迎使用梵町忘记密码功能,您的验证码是******";
					SendSmsHelper
							.sendNewSms(ConditionUtil.getUid(request), mobile,
									veriCode, veriCode,
									ConditionUtil.getChannel(request),
									ConditionUtil.getEsid(request),
									SMSType.FORGET_PASS);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	/**
	 * 确认重设密码,根据手机号查找到相应用户后重设密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("applyResetPass.json")
	@ResponseBody
	public String applyResetPass(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				final String mobile = getRequestBean().getBody().getString("mobile");
				final String newpwd = getRequestBean().getBody().getString("newpwd");
				final String veriCode = getRequestBean().getBody().getString("veriCode");
				final String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, "1", "手机号不合法");
				}
				// 缓存中的验证码
				String vcodeInCache = mobileService.getVeriCodeByMobile(mobile);
				if (null == vcodeInCache) {
					log.debug(mobile + " 手机的验证码在缓存中不存在");
					return busiErrorBean(jbean, "4", "验证码已过期，请重新获取");
				}
				if (vcodeInCache.equalsIgnoreCase(veriCode)) {
					// 验证码相符,查找手机号对应用户
					EucUser eucUser = eucUserService
							.queryUserInfoByMobile(mobile);
					if (null != eucUser) {
						final String userName = eucUser.getName();
						final String oldPwd = eucUser.getPasswd();
						
						String checkPass = FormUserValidator
								.checkPassword(newpwd);
						if (!"".equals(checkPass)) {
							return busiErrorBean(jbean, "3", checkPass);
						}
						eucUserService.updatePasswd(eucUser.getId() + "", newpwd);
						
						log.debug("update password successful!");
						
						// 修改成功，清除ticket
						final String oldToken = TokenUtils.getTokenByUserInfo(userName, oldPwd, true);
						
						log.warn("applyResetPass:$$#########################################");
						log.warn("userName:" + userName);
						log.warn("oldToken:" + oldToken);
						ticketRegistry.deleteTicket(oldToken);
						log.debug("remove ticket from cache!");
						
						// 删除缓存中的验证码(不删除，让它自动过期，防止用户重复请求)
						// mobileService.delVeriCodeByMobile(mobile);
						// 进行登录
						return getLoginResult(jbean, userName, newpwd, "true", fetchRequestInfo(getRequestBean()));
					} else {
						log.debug("手机号对应用户不存在 " + mobile);
						return busiErrorBean(jbean, "2", "无绑定该手机号的用户");
					}
				} else {
					log.debug("验证码不符");
					return busiErrorBean(jbean, "5", "验证码不符");
				}
			}

		};
		return abc.genReturnJson();
	}
	
	protected RequestInfo fetchRequestInfo(JBean jbean) {
		RequestInfo info=new RequestInfo();
		if(jbean != null) {
			JHead jhead = jbean.getHead();
			if(jhead != null) {
				info.setSource(jhead.getSource());
				info.setAppId(jhead.getAppId());
				info.setQn(jhead.getQn());
				info.setAgent(jhead.getAgent() != null ? jhead.getAgent() : "");
			}
		}
		return info;
	}
	
	/**
	 * 进行登录
	 * @param jbean
	 * @param username
	 * @param password
	 * @param remember
	 * @param info
	 * @return
	 */
	private JBean getLoginResult(JBean jbean, String username, String password, String remember, RequestInfo info) {
		JBody jbody = new JBody();
		// 创建用户登陆对象
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
		credentials.setUsername(username);
		credentials.setPassword(password);
		log.warn("login:$$#########################################");
		log.warn("username:" + username);
		try {
			// 验证用户信息，获取TGT
			String ticketGrantingTicketId = centralAuthenticationService.createTicketGrantingTicket(credentials);
			log.warn("ticketGrantingTicketId:" + ticketGrantingTicketId);
			// 根据TGT获取用户信息
			Ticket ticket = ticketRegistry.getTicket(ticketGrantingTicketId);
			// 成功登录
			if (ticket != null && ticket instanceof TicketGrantingTicket) {
				// 获取用户信息
				Authentication authentication = ((TicketGrantingTicket) ticket).getAuthentication();
				Principal principal = authentication.getPrincipal();
				if (log.isDebugEnabled()) {
					log.debug("authentication is success,user'id[" + principal.getId() + "]");
				}
				// 获取用户信息
				EucUser eucUser = eucUserService.queryUserInfoById(principal.getId());
				/* 返回用户信息 */
				JUser jUser = transJUser(eucUser, new JUser());
				jbody.put("user", jUser);
				// 记住密码
				if (null!=remember && Boolean.valueOf(remember)) {
					addUCookie(jbody, credentials.getUsername(), credentials.getPassword());
				}
				// 添加TOKEN
				addToken(jbody, ticketGrantingTicketId);
				jbean.setBody(jbody);
				//异步更新数据
				EucUser tempUser = new EucUser();
				tempUser.setId(eucUser.getId());
				tempUser.setLastLoginTime(new Date(System.currentTimeMillis()));
				asynUserManager.asynUpdate(tempUser);
				
				BizLogUtil.loginLog(info, principal.getId(), username != null ? username : "", Way.INTERFACE, LogConstant.RESULT_SUCCESS, LoginType.DEFAULT, "", ticketGrantingTicketId, LogConstant.LOGIN_RESULT_SUCCESS, "", info.getQn(), info.getAppId(), eucUser.getRegisterTime());
				return jbean;
			} else {	// TGC已经失效
				BizLogUtil.loginLog(info, "", username != null ? username : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, LoginType.DEFAULT, "", ticketGrantingTicketId != null ? ticketGrantingTicketId : "", LogConstant.LOGIN_RESULT_TGC_ERROR, "", info.getQn(), info.getAppId(), null);
				return busiErrorBean(jbean, "7", "TGC已经失效");
			}

		} catch (TicketException e) {
			// BizLogUtil.loginLog("", userName!=null?userName:"",
			// Way.INTERFACE,
			// LogConstant.RESULT_FAILURE, loginType, "", "",
			// ticketGrantingTicketId!=null?ticketGrantingTicketId:"",channel,clientEsid,LogConstant.LOGIN_RESULT_PASSWORD_ERROR,clientEsid,uid);
			return busiErrorBean(jbean, "9", "密码错误");
		}
	}

	private void addToken(JBody jbody, String tgt) {
		EucToken token = new EucToken();
		token.setToken(tgt);
		token.setDomain(ticketGrantingTicketCookieGenerator.getCookieDomain());
		token.setPath(ticketGrantingTicketCookieGenerator.getCookiePath());
		token.setAge(ticketGrantingTicketCookieGenerator.getCookieMaxAge());
		jbody.put("token", token);
	}
	
	private void addUCookie(JBody jbody, String username, String password) {
		/* 返回U参数 */
		DESPlus des = new DESPlus();
		String u = des
				.encrypt(username + "$" + pwdEncoder
						.encode(password));
		EucUCookie ucookie = new EucUCookie();
		ucookie.setU(u);
		ucookie.setDomain(uinfoCookieGenerator.getCookieDomain());
		ucookie.setPath(uinfoCookieGenerator.getCookiePath());
		ucookie.setAge(uinfoCookieGenerator.getCookieMaxAge());
		jbody.put("U", ucookie);
	}

}
