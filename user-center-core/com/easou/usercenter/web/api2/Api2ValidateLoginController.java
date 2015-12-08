package com.easou.usercenter.web.api2;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JHead;
import com.easou.common.api.JUser;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.Way;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.DESPlus;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.asyn.AsynGameManager;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;

/**
 * 外部验证登录 优先验证TOKEN;如果TOKEN无效， 再验证U。U无效，则认为无登录信息
 * 
 * @author jay
 * @since 2013.01.14
 * @version 1.0
 */
@Controller
@RequestMapping("/api2/")
public class Api2ValidateLoginController extends AbstractAPI2Controller {
	
//	private final static String DEFAULT_QN = "1000";

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
/*	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;*/
	@Resource(name = "asynUserManager")
	private AsynManager<EucUser> asynUserManager;
	@Resource(name = "asynGameManager")
	private AsynGameManager asynGameManager;

	public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
		return ticketGrantingTicketCookieGenerator;
	}

	public void setTicketGrantingTicketCookieGenerator(
			CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
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

	public AsynManager<EucUser> getAsynUserManager() {
		return asynUserManager;
	}

	public void setAsynUserManager(AsynManager<EucUser> asynUserManager) {
		this.asynUserManager = asynUserManager;
	}
	
	public AsynGameManager getAsynGameManager() {
		return asynGameManager;
	}

	public void setAsynGameManager(AsynGameManager asynGameManager) {
		this.asynGameManager = asynGameManager;
	}

	@RequestMapping("validate.json")
	@ResponseBody
	public String validate(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				JBody body = new JBody();

				// 登录类型，默认为TGC登录
				LoginType loginType = LoginType.TGC;
				
				// 创建用户登陆对象
				UsernamePasswordCredentials credentials = null;
				// 生成令牌票据
				Ticket ticket = null;
				
				// 登录令牌
				String ticketGrantingTicketId = getRequestBean().getBody()
						.getString(CookieUtil.COOKIE_TGC);
				// 加密过的用户信息
				String uCookie = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_U);
				
				if(StringUtil.isEmpty(ticketGrantingTicketId) && StringUtil.isEmpty(uCookie)) {
					return busiErrorBean(jbean, "7", "无验证信息");
				}
				// TGC验证
				if (!StringUtil.isEmpty(ticketGrantingTicketId)) {
					ticket = ticketRegistry.getTicket(ticketGrantingTicketId);
					if(null!=ticket) {
						log.debug("TGC验证有效");
					}
				}
				// 优先TGC验证无效后，进行Ucookie验证
				if(ticket==null && !StringUtil.isEmpty(uCookie)) {
					loginType = LoginType.AUTO;
					try {
					credentials = decrypt(uCookie);
					ticketGrantingTicketId = centralAuthenticationService
							.createTicketGrantingTicket(credentials);
					ticket = ticketRegistry
							.getTicket(ticketGrantingTicketId);
					} catch (TicketException e) {
						BizLogUtil.loginLogNew("", "", Way.INTERFACE,
								LogConstant.RESULT_FAILURE, loginType, "",
								ticketGrantingTicketId,
								LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", null, qn, appId, source, agent);
						log.warn("通过U标识登录失败，可能是密码发生改变");
						return busiErrorBean(jbean, "9", "密码错误，请重新登录");
					} catch(Exception e) {
						return busiErrorBean(jbean, "8", "无效的验证信息");
					}
				}
				if (ticket!=null && ticket instanceof TicketGrantingTicket) {
					if (ticketRegistry instanceof MemCacheTicketRegistry) {// 更新缓存，延迟缓存时间
						((MemCacheTicketRegistry) ticketRegistry)
								.updateTicket(ticket);
					}
					Authentication authentication = ((TicketGrantingTicket) ticket)
							.getAuthentication();
					Principal principal = authentication.getPrincipal();
					if (log.isDebugEnabled()) {
						log.debug("authentication is success,user'id["
								+ principal.getId() + "]");
					}

					// 获取用户信息
					EucUser eucUser = userService.queryUserInfoById(principal
							.getId());
					
					jbean.getHead().setQn(eucUser.getQn());
					
					/* 返回用户信息 */
					JUser jUser = transJUser(eucUser, new JUser());
					body.put("user", jUser);
					/* 返回TGC */
					EucToken token = new EucToken();
					token.setToken(ticketGrantingTicketId);
					token.setDomain(ticketGrantingTicketCookieGenerator.getCookieDomain());
					token.setPath(ticketGrantingTicketCookieGenerator.getCookiePath());
					token.setAge(ticketGrantingTicketCookieGenerator.getCookieMaxAge());
					body.put("token", token);
					/* 异步更新数据 */
					EucUser tempUser = new EucUser();
					tempUser.setId(eucUser.getId());
					tempUser.setLastLoginTime(new Date(System
							.currentTimeMillis()));
					asynUserManager.asynUpdate(tempUser);
					/* TODO 统计登录游戏的人数 */
					BizLogUtil.gLoginLog(qn, appId, tempUser.getId(),
							LogConstant.RESULT_SUCCESS,
							eucUser.getRegisterTime(),
							tempUser.getLastLoginTime(), agent);
					if(LoginType.AUTO.equals(loginType)) {
						// 自动登录的情况下才记录登录日志
						BizLogUtil.loginLogNew(String.valueOf(eucUser.getId()),
							tempUser.getName(), Way.INTERFACE,
							LogConstant.RESULT_SUCCESS, loginType, "",
							ticketGrantingTicketId,
							LogConstant.LOGIN_RESULT_SUCCESS, "", eucUser.getRegisterTime(), qn, appId, source, agent);
					}
					/* 记录游戏历史记录 */
					if (StringUtil.isEmpty(appId)) {
						asynGameManager
								.savePlayHistory(appId, tempUser.getId());
					}
					jbean.setBody(body);
					return jbean;
				} else {
					// 经过TGC和Ucookie两种方式验证失败
					BizLogUtil.loginLogNew("", "", Way.INTERFACE,
							LogConstant.RESULT_FAILURE, loginType, "",
							ticketGrantingTicketId,
							LogConstant.LOGIN_RESULT_TGC_ERROR, "", null, qn, appId, source, agent);
					return busiErrorBean(jbean, "8", "无效的验证信息");
				}
			}
		};
		return abc.genReturnJson();
	}

	/**
	 * 本接口提供给用户中心自己进行验证，主要是日志上的区别
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("validatenew.json")
	@ResponseBody
	public String validatenew(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				JBody body = new JBody();
				// 登录类型，默认为TGC登录
//				LoginType loginType = LoginType.TGC;

				if(log.isDebugEnabled()) {
					log.debug("登录验证: appId=" + appId + " qn=" + qn + " agent=" + agent);
				}
				
				// 创建用户登陆对象
				UsernamePasswordCredentials credentials = null;
				// 生成令牌票据
				Ticket ticket = null;
				
				// 登录令牌
				String ticketGrantingTicketId = getRequestBean().getBody()
						.getString(CookieUtil.COOKIE_TGC);
				// 加密过的用户信息
				String uCookie = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_U);
				
				if(StringUtil.isEmpty(ticketGrantingTicketId) && StringUtil.isEmpty(uCookie)) {
					return busiErrorBean(jbean, "7", "无验证信息");
				}
				// TGC验证
				if (!StringUtil.isEmpty(ticketGrantingTicketId)) {
					ticket = ticketRegistry.getTicket(ticketGrantingTicketId);
					if(null!=ticket) {
						log.debug("TGC验证有效");
					}
				}
				// 优先TGC验证无效后，进行Ucookie验证
				if(ticket==null && !StringUtil.isEmpty(uCookie)) {
//					loginType = LoginType.AUTO;
					try {
					credentials = decrypt(uCookie);
					ticketGrantingTicketId = centralAuthenticationService
							.createTicketGrantingTicket(credentials);
					ticket = ticketRegistry
							.getTicket(ticketGrantingTicketId);
					} catch (TicketException e) {
						log.warn("通过U标识登录失败，可能是密码发生改变");
						return busiErrorBean(jbean, "9", "验证失败，可能是密码发生改变");
					} catch(Exception e) {
						return busiErrorBean(jbean, "8", "无效的验证信息");
					}
				}
				if (ticket!=null && ticket instanceof TicketGrantingTicket) {
					if (ticketRegistry instanceof MemCacheTicketRegistry) {// 更新缓存，延迟缓存时间
						((MemCacheTicketRegistry) ticketRegistry)
								.updateTicket(ticket);
					}
					Authentication authentication = ((TicketGrantingTicket) ticket)
							.getAuthentication();
					Principal principal = authentication.getPrincipal();
					if (log.isDebugEnabled()) {
						log.debug("authentication is success,user'id["
								+ principal.getId() + "]");
					}

					// 获取用户信息
					EucUser eucUser = userService.queryUserInfoById(principal
							.getId());
					
					jbean.getHead().setQn(eucUser.getQn());
					
					/* 返回用户信息 */
					JUser jUser = transJUser(eucUser, new JUser());
					body.put("user", jUser);
					/* 返回TGC */
					EucToken token = new EucToken();
					token.setToken(ticketGrantingTicketId);
					token.setDomain(ticketGrantingTicketCookieGenerator.getCookieDomain());
					token.setPath(ticketGrantingTicketCookieGenerator.getCookiePath());
					token.setAge(ticketGrantingTicketCookieGenerator.getCookieMaxAge());
					body.put("token", token);
					/* 异步更新数据 */
					EucUser tempUser = new EucUser();
					tempUser.setId(eucUser.getId());
					tempUser.setLastLoginTime(new Date(System
							.currentTimeMillis()));
					asynUserManager.asynUpdate(tempUser);
					jbean.setBody(body);
					return jbean;
				} else {
					// 经过TGC和Ucookie两种方式验证失败
					return busiErrorBean(jbean, "8", "无效的验证信息");
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
	private UsernamePasswordCredentials decrypt(String uInfo)throws Exception{
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
		if (infos.length > 1) {
			cre.setPassword(infos[1]);
		}
		cre.setCookieLogin(true);
		if (log.isDebugEnabled()) {
			log.debug("decrypt user[name:" + infos[0] + ",password:" + infos[1]
					+ "] from cookie");
		}
		return cre;
	}
	
	
	

}
