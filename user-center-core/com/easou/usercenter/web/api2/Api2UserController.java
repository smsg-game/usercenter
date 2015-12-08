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
import com.easou.cas.auth.EucUCookie;
import com.easou.cas.authenticateion.Md5PwdEncoder;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.Way;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.DESPlus;
import com.easou.common.util.MD5Util;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api2/")
public class Api2UserController extends AbstractAPI2Controller {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	
	@Resource
	private CentralAuthenticationService centralAuthenticationService;
	@Autowired
	private TicketRegistry ticketRegistry;
	@Resource(name = "asynUserManager")
	private AsynManager asynUserManager;
	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;
	@Autowired
	private Md5PwdEncoder pwdEncoder;

	/**
	 * 根据用户id获取用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserById.json")
	@ResponseBody
	public String getUserById(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				Long id = getRequestBean().getBody().getLong("id");
				if (null == id) {
					return busiErrorBean(jbean, "1", "id参数错误");
				}
				EucUser eucUser = eucUserService.queryUserInfoById(id + "");
				if (null != eucUser) {
					JBody jbody = new JBody();
					jbody.putContent("user", transJUser(eucUser, new JUser()));
					jbean.setBody(jbody); // 查询到用户，加入返回体
				} else {
					return busiErrorBean(jbean, "2", "没找到用户");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}

	/**
	 * 根据用户名获取用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserByName.json")
	@ResponseBody
	public String getUserByName(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String userName = getRequestBean().getBody().getString(
						"username");
				if (null == userName) {
					return busiErrorBean(jbean, "1", "用户名参数错误");
				}
				EucUser eucUser = eucUserService.queryUserInfoByName(userName);
				if (null != eucUser) {
					JBody jbody = new JBody();
					jbody.putContent("user", transJUser(eucUser, new JUser()));
					jbean.setBody(jbody); // 查询到用户，加入返回体
				} else {
					return busiErrorBean(jbean, "2", "没找到用户");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}

	@RequestMapping("updateUser.json")
	@ResponseBody
	public String updateUser(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				JDesc jdesc = new JDesc(); // 错误描述

				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "无效的TOKEN");
				}
				EucUser eucUser = eucUserService.queryUserInfoById(userId + ""); // 要修改的用户
				if (null == eucUser) {
					jdesc.addReason("3", "修改的用户不存在");
				} else {
					boolean hasChange = false;
					EucUser updateUser = new EucUser(); // 要更新的类
					String nickName = null == getRequestBean().getBody()
							.getString("nickName") ? null : getRequestBean()
							.getBody().getString("nickName").trim();
					Integer birthday = getRequestBean().getBody().getInteger(
							"birthday");
					Integer sex = getRequestBean().getBody().getInteger("sex");
					String city = null == getRequestBean().getBody().getString(
							"city") ? null : getRequestBean().getBody()
							.getString("city").trim();
					Integer occuId = getRequestBean().getBody().getInteger(
							"occuId");

					// 昵称
					if (checkNickName(nickName, eucUser, jdesc)) {
						updateUser.setNickName(nickName);
						eucUser.setNickName(nickName);
						hasChange = true;
					}

					// 出生年份
					if (checkBirthday(birthday, eucUser, jdesc)) {
						updateUser.setBirthday(birthday);
						eucUser.setBirthday(birthday);
						hasChange = true;
					}

					// 性别
					if (checkSex(sex, eucUser, jdesc)) {
						updateUser.setSex(sex);
						eucUser.setSex(sex);
						hasChange = true;
					}

					// 城市
					if (checkCity(city, eucUser, jdesc)) {
						updateUser.setCity(city);
						eucUser.setCity(city);
						hasChange = true;
					}

					// 职业
					if (checkOccuId(occuId, eucUser, jdesc)) {
						updateUser.setOccuId(occuId);
						eucUser.setOccuId(occuId);
						hasChange = true;
					}

					if (hasChange) {
						eucUserService.updateUserById(userId + "", updateUser);
						JBody jbody = new JBody();
						jbody.putContent("user",
								transJUser(eucUser, new JUser()));
						jbean.setBody(jbody);
					} else {
						// 什么动作都没产生
						if (0 == jdesc.size())
							jdesc.addReason("9", "无信息更新");
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

	/**
	 * 通过用户凭证(token,id)更新密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updatePasswd.json")
	@ResponseBody
	public String updatePasswd(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				String oldPass = getRequestBean().getBody()
						.getString("oldPass");
				String newPass = getRequestBean().getBody()
						.getString("newPass");
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "密码错误，请重新登录");
				}
				EucUser eucUser = eucUserService.queryUserInfoById(userId + "");
				if (null == eucUser) {
					return busiErrorBean(jbean, "2", "没找到用户");
				}
				if (MD5Util.md5(oldPass).equals(eucUser.getPasswd())) {
					String s = FormUserValidator.checkPassword(newPass);
					if (!"".equals(s))
						return busiErrorBean(jbean, "4", s); // 密码错误
					eucUserService.updatePasswd(userId + "", newPass);
					log.debug("update password successful!");
					
					// 修改成功，清除ticket
					ticketRegistry.deleteTicket(token);
					log.debug("remove ticket from cache!");
					
					// 进行登录
//					jbean = getLoginResult(jbean, eucUser.getName(), newPass, "true", fetchRequestInfo(getRequestBean()));
//					log.debug("relogin the user!");
				} else {
					return busiErrorBean(jbean, "3", "旧密码错误");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	/**
	 * 通过用户凭证(token,id)更新密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updatePasswd2.json")
	@ResponseBody
	public String updatePasswd2(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				String newPass = getRequestBean().getBody()
						.getString("newPass");
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "密码错误，请重新登录");
				}
				EucUser eucUser = eucUserService.queryUserInfoById(userId + "");
				if (null == eucUser) {
					return busiErrorBean(jbean, "2", "没找到用户");
				}
				String s = FormUserValidator.checkPassword(newPass);
				if (!"".equals(s))
					return busiErrorBean(jbean, "4", s); // 密码错误
				eucUserService.updatePasswd(userId + "", newPass);
				
				log.debug("update password2 successful!");
				
				// 修改成功，清除ticket
				ticketRegistry.deleteTicket(token);
				log.debug("remove ticket from cache!");
				
				// 进行登录
//				jbean = getLoginResult(jbean, eucUser.getName(), newPass, "true", fetchRequestInfo(getRequestBean()));
//				log.debug("relogin the user!");
				return jbean;
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
		try {
			// 验证用户信息，获取TGT
			String ticketGrantingTicketId = centralAuthenticationService.createTicketGrantingTicket(credentials);
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
	
	private boolean checkNickName(String nickName, EucUser eucUser, JDesc jdesc) {
		if (null == nickName || nickName.equals(eucUser.getNickName())) {
			return false;
		}
		String s = FormUserValidator.checkNickName(nickName);
		if (!"".equals(s)) {
			jdesc.addReason("5", s);
			return false;
		}
		return true;
	}
	
	
	
}
