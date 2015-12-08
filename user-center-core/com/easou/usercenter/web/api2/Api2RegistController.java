package com.easou.usercenter.web.api2;

import java.util.Date;

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
import com.easou.cas.auth.EucUCookie;
import com.easou.cas.authenticateion.Md5PwdEncoder;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.util.DESPlus;
import com.easou.common.util.MD5Util;
import com.easou.common.util.RandomKeyGenerator;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.service.RegisterService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api2/")
public class Api2RegistController extends AbstractAPI2Controller {
	// 记住密码标识
	public static final String REMEMBER_PASSWORD = "remember";
	
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	@Autowired
	private MobileService mobileService;
	@Autowired
	private RegisterService registerService;
	@Resource
	private CentralAuthenticationService centralAuthenticationService;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	@Resource(name = "usrInfoCookie")
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;
	@Resource(name = "asynUserManager")
	private AsynManager asynUserManager;

	/**
	 * 用户名注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("registByName.json")
	@ResponseBody
	public String registByName(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				RequestInfo info=buildRequestInfo(request, qn, appId, source, agent);
				JDesc jdesc = new JDesc(); // 错误描述
				
				String username = getRequestBean().getBody().getString("username");
				String password = getRequestBean().getBody().getString("password");
				String bookNum= getRequestBean().getBody().getString("bookNum");
				Boolean remember = getRequestBean().getBody().getBoolean(REMEMBER_PASSWORD);
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
					BizLogUtil.registLogNew(username, null, Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);	
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
					return jbean;
				}
				
				if(null!=eucUserService.queryUserInfoByName(username)) {
					BizLogUtil.registLogNew(username, null, Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_NAME_EXIST,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);	
					return busiErrorBean(jbean,"3","该名称已注册");
				} else {
					// 判定是否有预约用户名
					String bnum = registerService.getBookNum(username);
					if(null!=bnum && !bnum.equals(bookNum)) {
						return busiErrorBean(jbean,"3","该名称已注册");
					}
				}
				
				EucUser eucUser = eucUserService.insertUserForUnameRegist(username, MD5Util.md5(password), qn);
				if (null != eucUser) {
					JBody jbody = new JBody();
					jbody.putContent("user", transJUser(eucUser,new JUser()));
					BizLogUtil.registLogNew(username, eucUser.getId(), Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_SUCCESS, "",
							LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);
					if(loginResult(jbody, eucUser, password, remember, info)) {
						jbean.setBody(jbody);// 用户注册成功，返回用户实体
					} else {
						return busiErrorBean(jbean, "9", "登录失败");
					}
				} else {
					// 注册失败
					BizLogUtil.registLogNew(username, null, Way.INTERFACE,LogConstant.REGIS_TYPE_NAME, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_EXCEPTION,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);	
					return busiErrorBean(jbean,"4","注册失败");
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}
	
	/**
	 * 预约一个用户名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("bookingName.json")
	@ResponseBody
	public String bookingName(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {			
				String rndName = "";
				for (int i = 0; i < 5; i++) {
					// 尝试5次临时用户名，确保不重复，如果重复，只能返回最后成功的预约名
					rndName = RandomKeyGenerator.generateMIX(6);
					// 数据库和预约库中无此名
					if(null==eucUserService.queryUserInfoByName(rndName) && null==registerService.getBookNum(rndName))
						break;
				}
				// 生成一个预约名
				String bookNum = registerService.genBookNum(rndName);
				JBody body = new JBody();
				body.putContent("bookName", rndName);
				body.putContent("bookNum", bookNum);
				jbean.setBody(body);
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	/**
	 * 检查用户名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("checkName.json")
	@ResponseBody
	public String checkName(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				
				String username = getRequestBean().getBody().getString("username");
				String s = FormUserValidator.checkUserName(username);
				if(!"".equals(s)) {
					return busiErrorBean(jbean,"1",s);
				}
				if(null!=eucUserService.queryUserInfoByName(username)) {
					return busiErrorBean(jbean,"2","该名称已注册");
				}
				String bookNum = registerService.getBookNum(username);
				if(null!=bookNum) {
					// 该用户名已预约
					return busiErrorBean(jbean,"2","该名称已注册");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	/**
	 * 自动注册接口，注册成功后会登录，接口用post发送json内容:
	 * remember	记住密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("autoRegist.json")
	@ResponseBody
	public String autoRegist(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				RequestInfo info = buildRequestInfo(request, qn, appId, source, agent);
				Boolean remember = getRequestBean().getBody().getBoolean(REMEMBER_PASSWORD);
				// 初始化一个body
				JBody jbody = new JBody();
				// 生成一个随机密码
				String passwd = RandomKeyGenerator.genNumber(6);
				EucUser eucUser = eucUserService.insertUserByDefault(passwd, null, qn);
				if (null != eucUser) {
					// 用户生成成功
					ExpJUser expJUser = (ExpJUser) transJUser(eucUser,
							new ExpJUser());
					// 保存密码
					expJUser.setPasswd(passwd);
					jbody.put("user", expJUser);
					BizLogUtil.registLogNew(eucUser.getName(), eucUser.getId(), Way.INTERFACE,
							LogConstant.REGIS_TYPE_A_KEY,
							LogConstant.RESULT_SUCCESS, "", 
							LogConstant.REGIS_RESULT_SUCCESS, 
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);
					if(loginResult(jbody, eucUser, passwd, remember, info)) {
						jbean.setBody(jbody);// 用户注册成功，返回用户实体
					} else {
						return busiErrorBean(jbean, "9", "登录失败");
					}
				} else {
					BizLogUtil.registLogNew("", null, 
							Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN,
							LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_EXCEPTION,
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);
					return busiErrorBean(jbean, "4", "注册失败");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	/**
	 * 获取验证码接口，接口用post发送json内容:
	 * mobile			手机号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestMobileCode.json")
	@ResponseBody
	public String requestMobileCode(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				// 渠道
				String channel = ConditionUtil.getChannel(request);
				String esid = ConditionUtil.getEsid(request);
				String uid = ConditionUtil.getUid(request);
				JDesc jdesc = new JDesc(); // 错误描述
				String mobile = getRequestBean().getBody().getString("mobile");
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, "1", "手机号不合法");
				}
				EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
				if (null != eucUser) {
					return busiErrorBean(jbean, "3", "该手机号已注册，可直接登录");
				}
				String veriCode = mobileService.getVeriCodeByMobile(mobile);
				if (null == veriCode) {
					// 未有验证码或验证码已超时
					veriCode = mobileService.setVeriCodeByMobile(mobile);
					// 需要发短信
					String message = "欢迎注册梵町用户,您的验证码是" + veriCode;
					String messageLog = "欢迎注册梵町用户,您的验证码是******";
					SendSmsHelper.sendSms(uid, mobile, message,messageLog, channel,
							esid, SMSType.REGISTER);
				} else {
					return busiErrorBean(jbean, "4", "验证码已发送,请稍后再试");
				}
				JBody jbody = new JBody();
				jbean.setBody(jbody);
				
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
	 * 通过验证码和密码进行手机号注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("regByMobileCode.json")
	@ResponseBody
	public String regByMobileCode(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request, response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				JDesc jdesc = new JDesc(); // 错误描述
				RequestInfo info= buildRequestInfo(request, qn, appId, source, agent);
				String mobile = getRequestBean().getBody().getString("mobile");
				String passwd = getRequestBean().getBody().getString("password");
				String veriCode = getRequestBean().getBody().getString("veriCode");
				Boolean remember = getRequestBean().getBody().getBoolean(REMEMBER_PASSWORD);
				String logMobile = mobile!=null?mobile:"";

				if (!FormUserValidator.checkMobile(mobile)) {
					BizLogUtil.registLogNew (logMobile, null,
							Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_MSISDN_ILLEGAL,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);
					log.debug("手机号不合法");
					return busiErrorBean(jbean,"1","手机号不合法");
				}
				String s = FormUserValidator.checkPassword(passwd);
				if(!"".equals(s)) {
					// 密码验证失败
					BizLogUtil.registLogNew("", null, Way.INTERFACE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_VERIFICATION_FAILURE,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);	
					return busiErrorBean(jbean,"2",s);
				}
				String eucVeriCode = mobileService.getVeriCodeByMobile(mobile);
				if(null!=eucVeriCode) {
					if(!eucVeriCode.equalsIgnoreCase(veriCode))
						return busiErrorBean(jbean,"4","验证码不正确");
				} else {
					return busiErrorBean(jbean, "3", "验证码已失效");
				}
				EucUser user = eucUserService.queryUserInfoByMobile(mobile);
				if (null != user) {
					// 手机号已注册
					BizLogUtil.registLogNew(logMobile, null,
							Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
							LogConstant.REGIS_RESULT_MSISDN_EXIST,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);	
					log.debug("该手机号已被注册");
					return busiErrorBean(jbean,"5","该手机号已注册,可直接登录");
				} else {
					EucUser eucUser = eucUserService.insertUserForMobileRegist(mobile, MD5Util.md5(passwd), qn);
					if (null!=eucUser) {
						if(log.isDebugEnabled()) {
							log.debug(mobile + "注册成功, pass:" + passwd);
						}
						JBody jbody = new JBody();
						jbody.putContent("user", transJUser(eucUser,new JUser()));
						BizLogUtil.registLogNew(eucUser.getName(), eucUser.getId(), Way.INTERFACE,LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_SUCCESS, "",
								LogConstant.REGIS_RESULT_SUCCESS,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);
						if(loginResult(jbody, eucUser, passwd, remember, info)) {
							jbean.setBody(jbody);// 用户注册成功，返回用户实体
						} else {
							return busiErrorBean(jbean, "9", "登录失败");
						}
					} else {
						log.warn("注册失败: " + mobile);
						BizLogUtil.registLogNew(logMobile, null, 
								Way.INTERFACE, LogConstant.REGIS_TYPE_MSISDN, LogConstant.RESULT_FAILURE, "",
								LogConstant.REGIS_RESULT_EXCEPTION,LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId, source, agent);
						return busiErrorBean(jbean,"6","注册失败");
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
	 * 注册成功后进行登录
	 * @param jbody
	 * @param user
	 * @param password
	 * @param remember
	 * @param info
	 * @return
	 */
	private boolean loginResult(JBody jbody, EucUser user, String password, Boolean remember, RequestInfo info) {
		// 创建用户登陆对象
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
		credentials.setUsername(user.getName());
		credentials.setPassword(password);
		try {
			// 验证用户信息，获取TGT
			String ticketGrantingTicketId = centralAuthenticationService
					.createTicketGrantingTicket(credentials);
			addToken(jbody, ticketGrantingTicketId);
			if (null != remember && remember.booleanValue()) {
				addUCookie(jbody, credentials.getUsername(), credentials.getPassword());
			}
			//异步更新数据
            EucUser tempUser = new EucUser();
            tempUser.setId(user.getId());
            tempUser.setLastLoginTime(new Date(System.currentTimeMillis()));
            asynUserManager.asynUpdate(tempUser);
			BizLogUtil.loginLogNew(String.valueOf(user.getId()), user.getName(),
					Way.INTERFACE, LogConstant.RESULT_SUCCESS,
					LoginType.DEFAULT, "", ticketGrantingTicketId,
					LogConstant.REGIS_RESULT_SUCCESS, "", user.getRegisterTime(), info.getQn(), info.getAppId(), info.getSource(), info.getAgent());
			return true;
		} catch (TicketException e) {
			log.error("登录失败", e);
			BizLogUtil.loginLogNew(String.valueOf(user.getId()), user.getName(),
					Way.INTERFACE, LogConstant.RESULT_FAILURE,
					LoginType.DEFAULT, "", "",
					LogConstant.LOGIN_RESULT_PASSWORD_ERROR, "", null, info.getQn(), info.getAppId(), info.getSource(), info.getAgent());
			return false;
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
	
	private RequestInfo buildRequestInfo(HttpServletRequest request, String qn, String appId, String source, String agent) {

		RequestInfo info=new RequestInfo();
		info.setUid(ConditionUtil.getUid(request));
		info.setEsid(ConditionUtil.getEsid(request));
		info.setQn(qn);
		info.setAppId(appId);
		info.setSource(source);
		info.setAgent(agent);
		return info;
	}
}