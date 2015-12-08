package com.easou.usercenter.web.api;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.cas.auth.EucToken;
import com.easou.cas.authenticateion.Md5PwdEncoder;
import com.easou.cas.ticket.MemCacheTicketRegistry;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.AppType;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.Way;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.DESPlus;
import com.easou.common.util.StringUtil;
import com.easou.session.SessionId;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;

/**
 * 登录接口 登录条件分三种情况 
 * 1、TGT登录；应用需要获取浏览器的TGT并作为登录条件传递给此接口
 * 2、COOKIE登录；应用需要获取浏览器COOKIE中用户信息作为登录条件传递给此接口 
 * 3、用户名密码登录；应用提供用户名和密码作为登录条件传递给接口
 * 
 * 登录顺序：
 * 如果登录类型是auto（自动登录）则先检测是否有TGT,有则用TGT进行登录；
 * 如果无TGT检测是否有自动登录标识，有则进行COOKIE登录
 * 如果登录类型为default（用户名密码登录），则直接进行用户名和密码登录
 * 
 * 登录情况判断：
 * 如果没 有登录类型，且没有登录用户名和密码，则进行自动登录
 * 如果没登录类型，但有用户名和密码，则进行用户名密码登录
 * 如果登录类型是default（用户名密码登录），但无登录用户名密码等参数，则返回错误信息
 * 
 * @author damon
 * @since 2012.05.17
 * @version 1.0
 * 
 * 返回结果增加token参数,值为tgt key
 * 主要是给客户端作为鉴权key
 * 
 * @author damon
 */
@Controller
@RequestMapping("/api/")
public class ApiLoginController extends AbstractAPIController {

	private static Logger LOG = Logger.getLogger(ApiLoginController.class);
	// 用户登录类型
	public static final String LOGIN_TYPE = "loginType";
	// 应用类型
	public static final String APP_TYPE = "appType";
	// 记住密码标识
	public static final String REMEMBER_PASSWORD = "remember";

	public final static String TGC_VERIFICATION_FAIL = "7";

	@Resource
	private EucUserService userService;
	@Resource
	CentralAuthenticationService centralAuthenticationService;
	@Autowired
	TicketRegistry ticketRegistry;
	@Autowired
	Md5PwdEncoder pwdEncoder;
	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;
	@Resource(name = "asynUserManager")
	private AsynManager asynUserManager;

	public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
		return ticketGrantingTicketCookieGenerator;
	}

	public void setTicketGrantingTicketCookieGenerator(
			CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public CookieRetrievingCookieGenerator getUinfoCookieGenerator() {
		return uinfoCookieGenerator;
	}

	public void setUinfoCookieGenerator(
			CookieRetrievingCookieGenerator uinfoCookieGenerator) {
		this.uinfoCookieGenerator = uinfoCookieGenerator;
	}

	public Md5PwdEncoder getPwdEncoder() {
		return pwdEncoder;
	}

	public void setPwdEncoder(Md5PwdEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}

	public CentralAuthenticationService getCentralAuthenticationService() {
		return centralAuthenticationService;
	}

	public void setCentralAuthenticationService(
			CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public EucUserService getUserService() {
		return userService;
	}

	public void setUserService(EucUserService userService) {
		this.userService = userService;
	}

	public AsynManager getAsynUserManager() {
		return asynUserManager;
	}

	public void setAsynUserManager(AsynManager asynUserManager) {
		this.asynUserManager = asynUserManager;
	}

	@RequestMapping("login")
	@ResponseBody
	public String login(final HttpServletRequest request, final HttpServletResponse response) {

		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JHead jHead = new JHead();
				JBody body = new JBody();
				JDesc jdesc = new JDesc(); // 错误描述
				
				// 登录类型，默认为自动登录
				LoginType loginType = LoginType.AUTO;
        // 应用类型
				AppType appType = AppType.WEB;

				// 获取用户名
				String userName = getRequestBean().getBody().getString("userName");
				// 获取密码
				String password = getRequestBean().getBody().getString("password");
				// 记住密码
				String remember = getRequestBean().getBody().getString(REMEMBER_PASSWORD);
				
				// 加密过的用户信息
				String uCookie = getRequestBean().getBody().getString(CookieUtil.COOKIE_U);
				// 登录类型
				String type = getRequestBean().getBody().getString(LOGIN_TYPE);			
				// 应用类型
				String aType = getRequestBean().getBody().getString(APP_TYPE);
				
				RequestInfo info = fetchRequestInfo(request);
				
				if (!StringUtil.isEmpty(aType) && AppType.getAppType(aType) != null) {
					appType = AppType.getAppType(aType);
					if (LOG.isDebugEnabled()) {
						LOG.debug("app type[" + appType.type + "]");
					}
				}

				// 获取登录类型
				if (!StringUtil.isEmpty(type)) {
					loginType = LoginType.getLoginType(type);
				} else if (!StringUtil.isEmpty(userName) || !StringUtil.isEmpty(password)) {
					// 如果用户名和密码不为空，认为用户名密码登录
					loginType = LoginType.DEFAULT;
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("login type[" + loginType.type + "]");
				}

				// 创建用户登陆对象
				UsernamePasswordCredentials credentials = null;
				String ticketGrantingTicketId = null;

				try {
					if (loginType == LoginType.DEFAULT) {
						// 用户名密码登录
						if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
							BizLogUtil.loginLog(info, "", userName != null ? userName : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, loginType, "", "", LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", qn, appId, null);
							jHead.setRet(CodeConstant.BUSI_ERROR);
							jdesc.addReason("3", "用户名或密码为空");
							jbean.setHead(jHead);
							jbean.setDesc(jdesc);
							return jbean;
						} else {
							credentials = new UsernamePasswordCredentials();
							credentials.setUsername(userName);
							credentials.setPassword(password);
							if ("true".equals(remember)) {
								credentials.setIsCookie("true");
							}
							// 验证用户信息，获取TGT
							ticketGrantingTicketId = centralAuthenticationService.createTicketGrantingTicket(credentials);
						}
					} else {	// 自动登录
						// 获取TGC
						ticketGrantingTicketId = getRequestBean().getBody().getString(CookieUtil.COOKIE_TGC);
						// TGT登录
						if (ticketGrantingTicketId != null) {
							//TGC登录, 将在后面进行TGT的验证
							loginType = LoginType.TGC;
							if (LOG.isDebugEnabled()) {
								LOG.debug("get the TGT["	+ ticketGrantingTicketId+"] from COOKIE");
							}
						} else if (!StringUtil.isEmpty(uCookie)) {	// COOKIE登录
							credentials = decrypt(uCookie);
							ticketGrantingTicketId = centralAuthenticationService.createTicketGrantingTicket(credentials);
						} else {
							// 返回 无自动登录信息
							LOG.warn("TGT和U都为空");
						}
					}
				} catch (TicketException e) {
					BizLogUtil.loginLog(info, "", userName != null ? userName : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, loginType, "", ticketGrantingTicketId != null ? ticketGrantingTicketId : "", LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", qn, appId, null);
					
					// TGC登录
					if (LoginType.AUTO == loginType) {
						jdesc.addReason("7", "TGT失效");
						log.error("TGT失效");
					} else {
						jdesc.addReason("9", "TGT生成失败，可能是密码错误");
					}
					jHead.setRet(CodeConstant.BUSI_ERROR);
					jbean.setHead(jHead);
					jbean.setDesc(jdesc);
					return jbean;
				}
				
				// 根据TGT获取用户信息
				Ticket ticket = ticketRegistry.getTicket(ticketGrantingTicketId);
				// 优先TGC登录失败后，判断是否有U，用U尝试登录
				if (ticket == null && LoginType.TGC == loginType && !StringUtil.isEmpty(uCookie)) {
					credentials = decrypt(uCookie);
					if ("true".equals(remember)) {
						credentials.setIsCookie("true");
					}
					// 验证用户信息，获取TGT
					try {
						ticketGrantingTicketId = centralAuthenticationService.createTicketGrantingTicket(credentials);
						ticket = ticketRegistry.getTicket(ticketGrantingTicketId);
					} catch (TicketException e) {
						BizLogUtil.loginLog(info, "", userName != null ? userName : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, loginType, "", ticketGrantingTicketId != null ? ticketGrantingTicketId : "", LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", qn, appId, null);
						
						jHead.setRet(CodeConstant.BUSI_ERROR);
						// 生成TGT失败，可能是密码错误
						// TODO: **Problem** The reason message is different from error message, so we need unify them.
						jdesc.addReason("9", "TGT生成失败，可能是密码错误");
						//jdesc.addReason("5", "U失败，可能是密码发生改变");
						jbean.setHead(jHead);
						jbean.setDesc(jdesc);
						
						log.error("通过U标识登录失败，可能是密码发生改变");
						return jbean;
					}
				}
				
				if (ticketGrantingTicketId == null) {
					BizLogUtil.loginLog(info, "", userName != null ? userName : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, loginType, "", "", LogConstant.LOGIN_RESULT_ERROR, "", qn, appId, null);
					jHead.setRet(CodeConstant.BUSI_ERROR);
					jdesc.addReason("8", "无效的验证信息");
					jbean.setHead(jHead);
					jbean.setDesc(jdesc);
					return jbean;
				}
				
				if (ticket != null && ticket instanceof TicketGrantingTicket) {
					// 更新缓存，延迟缓存时间
					if(ticketRegistry instanceof MemCacheTicketRegistry) {
						((MemCacheTicketRegistry)ticketRegistry).updateTicket(ticket);
					}
					Authentication authentication = ((TicketGrantingTicket) ticket).getAuthentication();
					Principal principal = authentication.getPrincipal();
					if (LOG.isDebugEnabled()) {
						LOG.debug("authentication is success,user'id[" + principal.getId() + "]");
					}
					// 用户ID
					body.put("easouId", principal.getId());
					
					EucUser eucUser = userService.queryUserInfoById(principal.getId());
					//JUser jUser = (JUser) principal.getAttributes().get("user");
					JUser jUser = transJUser(eucUser,new JUser());
					if(jUser == null) {
						jHead.setRet(CodeConstant.BUSI_ERROR);
						jdesc.addReason("6", "无法取到用户[" + principal.getId() + "]的详细信息，存在跨机房访问。");
						jbean.setHead(jHead);
						jbean.setDesc(jdesc);
						return jbean;
					} else {
						Map<String,Object> map = authentication.getPrincipal().getAttributes();
						if (map != null) {
							Object isRegist = map.get("isRegist");
							// 判断是否是通过手机下发短信的形式首次登录的用户
							if (isRegist != null && Boolean.valueOf(isRegist.toString())) {
								body.put("isRegist", isRegist);
							}
							// 判断是否是通过手机下发短信的形式首次登录的用户
							if (isRegist != null && Boolean.valueOf(isRegist.toString())) {
								Long eaId = null;
								try {
									eaId = new Long(principal.getId());
								} catch (Exception e) {
									// do nothing
								}
								
								// 写注册成功日志
								BizLogUtil.registLog(info, userName, eaId, Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS, "", LogConstant.REGIS_RESULT_SUCCESS, LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
							}
						}
						
						EucToken token = new EucToken();
						token.setToken(ticketGrantingTicketId);
						// 生成登录后的esid
						String esid = SessionId.createLogonId(jUser.getId(), info.getEsid(), jUser.getRandomPasswd());
						body.put("esid", esid);
						body.put("token", token);
						body.put("user", jUser);
						// body.put("user", principal.getAttributes().get("user"));

						// 异步更新数据
						EucUser tempUser = new EucUser();
						tempUser.setId(eucUser.getId());
						tempUser.setLastLoginTime(new Date(System.currentTimeMillis()));
						asynUserManager.asynUpdate(tempUser);
						// 登录成功日志
						BizLogUtil.loginLog(info, principal.getId(), userName != null ? userName : "", Way.INTERFACE, LogConstant.RESULT_SUCCESS, loginType, "", ticketGrantingTicketId, LogConstant.LOGIN_RESULT_SUCCESS, "", qn, appId, eucUser.getRegisterTime());
					}
					// 将用户信息保存到COOKIE中
					if (appType != AppType.APP) {// 如果应用类型不是APP类型，则需要写COOKIE
						if ("true".equals(remember)) {
							DESPlus des = new DESPlus();
							String u = des.encrypt((credentials.getUsername() + "$" + pwdEncoder.encode(credentials.getPassword())));
							body.put(CookieUtil.COOKIE_U, u);
							body.put("uDomain", uinfoCookieGenerator.getCookieDomain());
							body.put("uPath", uinfoCookieGenerator.getCookiePath());
							body.put("uAge", uinfoCookieGenerator.getCookieMaxAge());
						}

						body.put(CookieUtil.COOKIE_TGC, ticketGrantingTicketId);
						body.put("tgcDomain", ticketGrantingTicketCookieGenerator.getCookieDomain());
						body.put("tgcPath", ticketGrantingTicketCookieGenerator.getCookiePath());
						body.put("tgcAge", ticketGrantingTicketCookieGenerator.getCookieMaxAge());
					}

					// 返回TGT
					jbean.setBody(body);
					return jbean;
				} else {
					BizLogUtil.loginLog(info, "", userName != null ? userName : "", Way.INTERFACE, LogConstant.RESULT_FAILURE, loginType, "", ticketGrantingTicketId != null ? ticketGrantingTicketId : "", LogConstant.LOGIN_RESULT_TGC_ERROR, "", qn, appId, null);
					jHead.setRet(CodeConstant.BUSI_ERROR);
					jdesc.addReason("7", "TGC已经失效");
					jbean.setHead(jHead);
					jbean.setDesc(jdesc);
					return jbean;
				}
			}
		};
		
		return abc.genReturnJson();
	}

	/**
	 * 解密COOKIE中的用户信息
	 * 
	 * @param uInfo
	 * @return
	 */
	private UsernamePasswordCredentials decrypt(String uInfo) {
		DESPlus des = new DESPlus();
		// 解密用户信息
		String uInfo_ = des.decrypt(uInfo);
		String[] infos = uInfo_.split("\\$");
		if (infos == null) {
			return null;
		}
		// 创建用户登陆对象
		UsernamePasswordCredentials cre = new UsernamePasswordCredentials();
		cre.setUsername(infos[0]);
		if(infos.length>1){
		   cre.setPassword(infos[1]);
		}
		cre.setCookieLogin(true);
		if (LOG.isDebugEnabled()) {
			LOG.debug("decrypt user[name:" + infos[0] + ",password:" + infos[1]
					+ "] from cookie");
		}
		return cre;
	}
}
