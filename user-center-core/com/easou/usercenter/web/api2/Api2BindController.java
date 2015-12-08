package com.easou.usercenter.web.api2;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.cas.auth.EucToken;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.constant.SMSType;
import com.easou.common.util.CookieUtil;
import com.easou.common.util.MD5Util;
import com.easou.common.util.RandomKeyGenerator;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.SendMailUtil;
import com.easou.usercenter.web.utils.Languge;
import com.easou.usercenter.web.utils.MailConfig;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api2/")
public class Api2BindController extends AbstractAPI2Controller {

	@Resource(name = "eucUserServiceImpl")
	protected EucUserService eucUserService;
	@Autowired
	private MobileService mobileService;
	@Autowired
	private TicketRegistry ticketRegistry;
	/**
	 * 请求绑定手机(发送验证码)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestBindMobile.json")
	@ResponseBody
	public String requestBindMobile(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String mobile = getRequestBean().getBody().getString("mobile");
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "无效的TOKEN");
				}
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, "2", "手机号不合法");
				} else {
					EucUser eucUser = eucUserService
							.queryUserInfoByMobile(mobile);
					if (null != eucUser) {
						return busiErrorBean(jbean, "3", "该手机号已被绑定");
					} else {
						String veriCode = mobileService
								.getVeriCodeByMobile(mobile);
						if (null == veriCode) {
							veriCode = mobileService
									.setVeriCodeByMobile(mobile);
						} else {
							return busiErrorBean(jbean, "4",
									"验证码已发送,请稍后再试");
						}
						JBody jbody = new JBody();
						jbody.putContent("veriCode", "");
						jbean.setBody(jbody);
						// 需要发短信
//						String message = "验证码：" + veriCode + "，感谢您对梵町的支持!";
//						String messageLog = "验证码：xxxx，感谢您对梵町的支持!";
						// 渠道
						String channel = ConditionUtil.getChannel(request);
						String esid = ConditionUtil.getEsid(request);
						String uid = ConditionUtil.getUid(request);
						SendSmsHelper.sendNewSms(uid, mobile, veriCode, veriCode,
								channel, esid, SMSType.BIND);
					}
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	/**
	 * 提交绑定手机,根据id号来绑定新手机(填入验证码后绑定)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("applyBindMobile.json")
	@ResponseBody
	public String applyBindMobile(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String mobile = getRequestBean().getBody().getString("mobile");
				String veriCode = getRequestBean().getBody().getString(
						"veriCode");
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "无效的TOKEN");
				}
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, "2", "手机号不正确");
				}
				String veriCodeEuc = mobileService.getVeriCodeByMobile(mobile);
				if (null == veriCodeEuc) {
					// 内存中不存在验证码
					return busiErrorBean(jbean, "3", "验证码已失效");
				}
				EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
				if (null != eucUser) {
					return busiErrorBean(jbean, "6", "该手机号已被绑定");
				}
				if (veriCodeEuc.equalsIgnoreCase(veriCode)) {
					EucUser user = eucUserService.queryUserInfoById(userId + "");
					if (null == user) {
						return busiErrorBean(jbean, "5", "用户不存在");
					}
					EucUser updUser = new EucUser();
					updUser.setMobile(mobile);
					user.setMobile(mobile);
					eucUserService.updateUserById(userId + "", updUser);
					JBody jbody = new JBody();
					jbody.putContent("user", transJUser(user, new JUser()));
					jbean.setBody(jbody); // 查询到用户，加入返回体
				} else {
					// 验证码不匹配
					return busiErrorBean(jbean, "4", "验证码错误");
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}

	
//	private static final String bindEmailTitle = "绑定邮箱";
//	private static final String bindEmailcontent = "<p>点击链接完成绑定: <a href='#domain#/api2/confirmEmail.do?code=#code#'>点击此处确认绑定邮箱</a></p><p>此邮件不用回复，如果您意外收到该邮件，请忽略之。</p>";
	/**
	 * 提交绑定邮箱,根据id号来绑定新手机(填入验证码后绑定)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("applyBindEmail.json")
	@ResponseBody
	public String applyBindEmail(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		final String domain = getDomainFromRequest(request);
		//final Languge lan = Languge.English;
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {
			@Override
			public JBean buildJBean(JBean jbean) {
				String email = getRequestBean().getBody().getString("email");
				String lang = getRequestBean().getBody().getString("lan");
				Languge lan = Languge.getName(lang);
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "无效的TOKEN");
				}
				if (!FormUserValidator.checkEmail(email)) {
					return busiErrorBean(jbean, "2", "email格式不对");
				}
				EucUser my = eucUserService.queryUserInfoById(userId+"");
				//校验邮箱是否已经被绑定了
				EucUser eucUser = eucUserService.queryUserInfoByEmail(email);
				if(null!=eucUser){
					return busiErrorBean(jbean, "3", "email已经被绑定了");
				}
                //发送验证邮件
				String code = MD5Util.md5(email+userId+RandomKeyGenerator.generate(6));
				
				if(mobileService.setVeriCodeByEmail(code, email,userId+"",lan.name())){
					MailConfig config = MailConfig.instance();
				    String content = config.getMailConfirmContent(lan).replaceAll("#code#", code);
				    content = content.replaceAll("#domain#", domain);
				    content = content.replaceAll("#username#", my.getName());
				    SendMailUtil.send(email, config.getMailConfirmTitle(lan),content);
				}
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	private static String getDomainFromRequest(HttpServletRequest request){
		String requestUrl = request.getRequestURL().toString();
		String requestUri = request.getServletPath();
		String result = requestUrl.replace(requestUri, "");
		return result;
	}
	private static final String ERROR_MSG = "UNKNOW ERROR";
	
	private static final String ERROR_PARAM_AVALID = "PARAM_AVALID";
	
	private static final String ERROR_HAVE_BIND = "ERROR,EMAIL HAVE BIND!";
	
	/**
	 * 确认绑定邮箱
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("confirmEmail.do")
	@ResponseBody
	public String confirmEmail(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		String code = request.getParameter("code");
		if(StringUtils.isEmpty(code)){
			return ERROR_PARAM_AVALID;
		}
		String conent = mobileService.getEmailByVeriCode(code);
		if(StringUtils.isEmpty(conent)){
			return ERROR_MSG;
		}
		String[] contents = conent.split(":");
		String email = contents[0];
		String userId = contents[1];
		String lanStr = contents[2];
		//校验邮箱是否已经被绑定了
		EucUser eucUser = eucUserService.queryUserInfoByEmail(email);
		if(null!=eucUser){
			return ERROR_HAVE_BIND;
		}
		eucUser = eucUserService.queryUserInfoById(userId);
		if(null==eucUser){
			return ERROR_MSG;
		}
		Languge lan = Languge.getName(lanStr);
		EucUser updUser = new EucUser();
		updUser.setEmail(email);
		eucUserService.updateUserById(userId, updUser);
		String content =  MailConfig.instance().getMailConfirmResult(lan);
		content = content.replaceAll("#email#", email);
		return content;
	}
	
	
//	private static final String findPwdTitle = "密码找回";
//	private static final String findPwdContent = "您的新密码为:[#passwd#]";
	/**
	 * 根据邮箱找回密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("findpwdByEmail.json")
	@ResponseBody
	public String findpwdByEmail(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {
			@Override
			public JBean buildJBean(JBean jbean) {
//				String token = getRequestBean().getBody().getString(
//						CookieUtil.COOKIE_TGC);
//				EucToken eucToken = new EucToken();
//				eucToken.setToken(token);
//				Long userId = ServiceFactory.getInstance()
//						.getAuthVerifyService().verify(eucToken);
//				if (null == userId || 0 == userId.longValue()) {
//					return busiErrorBean(jbean, "1", "无效的TOKEN");
//				}
				String email = getRequestBean().getBody().getString("email");
				String lang = getRequestBean().getBody().getString("lan");
				Languge lan = Languge.getName(lang);
				//校验邮箱是否已经被绑定了
				EucUser eucUser = eucUserService.queryUserInfoByEmail(email);
				if(null==eucUser){
					return busiErrorBean(jbean, "2", "无效的用户");
				}
				if(StringUtils.isEmpty(eucUser.getEmail())){
					return busiErrorBean(jbean, "3", "还没有绑定邮箱");
				}
				String newPasswd = RandomKeyGenerator.generateMIX(8);
				eucUserService.updatePasswd(eucUser.getId()+"", newPasswd);
				
				// 修改成功，清除ticket
				final String userName = eucUser.getName();
				final String oldPwd = eucUser.getPasswd();
				final String oldToken = TokenUtils.getTokenByUserInfo(userName, oldPwd, true);
				
				log.warn("applyResetPass:$$#########################################");
				log.warn("userName:" + userName);
				log.warn("oldToken:" + oldToken);
				ticketRegistry.deleteTicket(oldToken);
				log.debug("remove ticket from cache!");
				
                //发送邮件
				MailConfig config = MailConfig.instance();
				String content = config.getMailBackContent(lan).replaceAll("#passwd#", newPasswd);
				content = content.replaceAll("#username#", eucUser.getName());
				SendMailUtil.send(eucUser.getEmail(), config.getMailBackTitle(lan), content);
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	public static void main(String[] args) {
		SendMailUtil.send("252493618@qq.com", "ceshi", "fuck you!");
	}
}
