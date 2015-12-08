package com.easou.usercenter.web.api;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JUser;
import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.BindActivity;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api/")
public class ApiMobileController extends AbstractAPIController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private MobileService mobileService;

	/**
	 * 直接绑定接口 在可以取到用户手机号的情况下，直接根据用户ID和手机号进行绑定的行为。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("directlyBindMobile")
	@ResponseBody
	public String directlyBindMobile(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				String channel = ConditionUtil.getChannel(request);
				String esid = ConditionUtil.getEsid(request);
				String uid = ConditionUtil.getUid(request);
				JDesc jdesc = new JDesc(); // 错误描述
				Long userId = getRequestBean().getBody().getLong("id");
				if (null != userId && 0 != userId.longValue()) {
					String mobile = getRequestBean().getBody().getString(
							"mobile");
					EucUser user = eucUserService
							.queryUserInfoById(userId + "");
					String curMobile = null == user.getMobile() ? "" : user
							.getMobile();
					if (null == user) {
						return busiErrorBean(jbean, jdesc, "5", "用户不存在");
					}
					if (FormUserValidator.checkMobile(mobile)) {
						EucUser eucUser = eucUserService
								.queryUserInfoByMobile(mobile);
						if (null != eucUser) {
							BizLogUtil.bindLog(uid, userId + "", curMobile,
									mobile != null ? mobile : "",
									Way.INTERFACE, BindActivity.DIRECTLY, "",
									LogConstant.RESULT_FAILURE, "", channel,
									esid, LogConstant.BING_MSISDN_EXIST);
							return busiErrorBean(jbean, jdesc, "3", "该手机号已被绑定");
						} else {
							EucUser updUser = new EucUser();
							updUser.setMobile(mobile);
							user.setMobile(mobile);
							eucUserService.updateUserById(userId + "", updUser);
							JBody jbody = new JBody();
							jbody.putContent("user", transJUser(user,
									new JUser()));
							jbean.setBody(jbody); // 查询到用户，加入返回体
							BizLogUtil.bindLog(uid, userId + "", curMobile,
									mobile != null ? mobile : "",
									Way.INTERFACE, BindActivity.DIRECTLY, "",
									LogConstant.RESULT_SUCCESS, "", channel,
									esid, LogConstant.BING_MSISDN_SUCCESS);
						}
					} else {
						BizLogUtil.bindLog(uid, userId + "", curMobile,
								mobile != null ? mobile : "", Way.INTERFACE,
								BindActivity.DIRECTLY, "",
								LogConstant.RESULT_FAILURE, "", channel, esid,
								LogConstant.BING_MSISDN_ILLEGAL);
						return busiErrorBean(jbean, jdesc, "2", "mobile参数错误");
					}
				} else {
					return busiErrorBean(jbean, jdesc, "1", "id参数错误");
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
	 * 请求绑定手机(发送验证码)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestBindMobile")
	@ResponseBody
	public String requestBindMobile(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				// 渠道
				String channel = ConditionUtil.getChannel(request);
				String esid = ConditionUtil.getEsid(request);
				String uid = ConditionUtil.getUid(request);
				JDesc jdesc = new JDesc(); // 错误描述
				Long userId = null;
				if (this.getCheckMode() == CHECK_BY_TOKEN) {// TOKEN校验方式
					userId = this.getAuthToken().getId();
				} else {
					userId = getRequestBean().getBody().getLong("id");
				}
				String mobile = getRequestBean().getBody().getString("mobile");
				if (!FormUserValidator.checkMobile(mobile)) {
					BizLogUtil.bindLog(uid, userId != null ? userId.toString()
							: "", "", mobile != null ? mobile : "",
							Way.INTERFACE, BindActivity.REQUEST, "",
							LogConstant.RESULT_FAILURE, "", channel, esid,
							LogConstant.BING_MSISDN_ILLEGAL);
					return busiErrorBean(jbean, jdesc, "1", "手机号不合法");
				} else {
					EucUser eucUser = eucUserService
							.queryUserInfoByMobile(mobile);
					if (null != eucUser) {
						BizLogUtil.bindLog(uid, userId != null ? userId
								.toString() : "", "", mobile != null ? mobile
								: "", Way.INTERFACE, BindActivity.REQUEST, "",
								LogConstant.RESULT_FAILURE, "", channel, esid,
								LogConstant.BING_MSISDN_EXIST);
						return busiErrorBean(jbean, jdesc, "2", "该手机号已被绑定");
					} else {
						String veriCode = mobileService
								.getVeriCodeByMobile(mobile);
						if (null == veriCode) {
							veriCode = mobileService
									.setVeriCodeByMobile(mobile);
						} else {
							return busiErrorBean(jbean, jdesc, "3",
									"验证码已发送,请稍后再试");
						}
						JBody jbody = new JBody();
						jbody.putContent("veriCode", "");
						jbean.setBody(jbody);
						if (isSendMsg(getRequestBean().getBody())) {
							// 需要发短信
							String message = "验证码："+veriCode+"，感谢您对梵町的支持!";
							String messageLog = "验证码：xxxx，感谢您对梵町的支持!";
							SendSmsHelper.sendSms(
									uid, mobile,
									message,messageLog,channel,
									esid,
									SMSType.BIND);
						}
						BizLogUtil.bindLog(uid, userId != null ? userId
								.toString() : "", "", mobile != null ? mobile
								: "", Way.INTERFACE, BindActivity.REQUEST,
								veriCode, LogConstant.RESULT_SUCCESS, "",
								channel, esid,
								LogConstant.BING_MSISDN_PUSH_SMS_SUCCESS);
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
	 * 提交绑定手机,根据id号来绑定新手机(填入验证码后绑定)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("applyBindMobile")
	@ResponseBody
	public String applyBindMobile(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				String channel = ConditionUtil.getChannel(request);
				String esid = ConditionUtil.getEsid(request);
				String uid = ConditionUtil.getUid(request);
				JDesc jdesc = new JDesc(); // 错误描述
				Long userId = null;
				if (this.getCheckMode() == CHECK_BY_TOKEN) {// TOKEN校验方式
					userId = this.getAuthToken().getId();
				} else {
					userId = getRequestBean().getBody().getLong("id");
				}
				if (null != userId && 0 != userId.longValue()) {
					EucUser user = eucUserService
							.queryUserInfoById(userId + "");
					if (null == user) {
						return busiErrorBean(jbean, jdesc, "5", "用户不存在");
					}
					String mobile = getRequestBean().getBody().getString(
							"mobile");
					String veriCode = getRequestBean().getBody().getString(
							"veriCode");
					String curMobile = null==user.getMobile()?"":user.getMobile();
					if (FormUserValidator.checkMobile(mobile)) {
						String veriCodeEuc = mobileService
								.getVeriCodeByMobile(mobile);
						if (null != veriCodeEuc) {
							// 内存中存在验证码
							if (veriCodeEuc.equalsIgnoreCase(veriCode)) {

								EucUser updUser = new EucUser();
								updUser.setMobile(mobile);
								user.setMobile(mobile);
								eucUserService.updateUserById(userId + "",
										updUser);
								JBody jbody = new JBody();
								jbody.putContent("user", transJUser(user,
										new JUser()));
								jbean.setBody(jbody); // 查询到用户，加入返回体
								BizLogUtil.bindLog(uid, userId+"", curMobile,
										mobile != null ? mobile : "",
										Way.INTERFACE,
										BindActivity.VERIFICATION, veriCode,
										LogConstant.RESULT_SUCCESS, "",
										channel, esid,
										LogConstant.BING_MSISDN_SUCCESS);
							} else {
								BizLogUtil.bindLog(uid, userId+"",curMobile,
										mobile != null ? mobile : "",
										Way.INTERFACE,
										BindActivity.VERIFICATION, veriCode,
										LogConstant.RESULT_FAILURE, "",
										channel, esid,
										LogConstant.BING_MSISDN_CODE_ERROR);
								return busiErrorBean(jbean, jdesc, "4", "验证码错误");
							}
						} else {
							BizLogUtil.bindLog(uid, userId+"", curMobile, mobile != null ? mobile
									: "", Way.INTERFACE,
									BindActivity.VERIFICATION, "",
									LogConstant.RESULT_FAILURE, "", channel,
									esid, LogConstant.BING_MSISDN_CODE_FAIL);
							return busiErrorBean(jbean, jdesc, "3", "验证码已失效");
						}
					} else {
						BizLogUtil.bindLog(uid, userId+"", curMobile,
								mobile != null ? mobile : "", Way.INTERFACE,
								BindActivity.VERIFICATION, "",
								LogConstant.RESULT_FAILURE, "", channel, esid,
								LogConstant.BING_MSISDN_ILLEGAL);
						return busiErrorBean(jbean, jdesc, "2", "mobile参数错误");
					}
				} else {
					return busiErrorBean(jbean, jdesc, "1", "id参数错误");
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
	 * 请求重设密码(发送验证码短信)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestResetPass")
	@ResponseBody
	public String requestResetPass(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				String mobile = getRequestBean().getBody().getString("mobile");
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, jdesc, "1", "手机号不合法");
				} else {
					EucUser eucUser = eucUserService
							.queryUserInfoByMobile(mobile);
					if (null == eucUser) {
						return busiErrorBean(jbean, jdesc, "2", "该手机号还未注册");
					}
					String veriCode = mobileService.getVeriCodeByMobile(mobile);
					if (null == veriCode) {
						veriCode = mobileService.setVeriCodeByMobile(mobile);
					} else {
						return busiErrorBean(jbean, jdesc, "3", "验证码已发送,请稍后再试");
					}
					JBody jbody = new JBody();
					jbody.putContent("veriCode", "");
					jbean.setBody(jbody);
					if (isSendMsg(getRequestBean().getBody())) {
						// 需要发短信
						String message = "欢迎使用梵町忘记密码功能,您的验证码是" + veriCode;
						String messageLog = "欢迎使用梵町忘记密码功能,您的验证码是******";
						SendSmsHelper.sendSms(ConditionUtil.getUid(request),
								mobile, message,messageLog, ConditionUtil
										.getChannel(request), ConditionUtil
										.getEsid(request), SMSType.FORGET_PASS);
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
	 * 确认重设密码,根据手机号查找到相应用户后重设密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("applyResetPass")
	@ResponseBody
	public String applyResetPass(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				String mobile = getRequestBean().getBody().getString("mobile");
				String newpwd = getRequestBean().getBody().getString("newpwd");
				String veriCode = getRequestBean().getBody().getString(
						"veriCode");
				if (!FormUserValidator.checkMobile(mobile)) {
					return busiErrorBean(jbean, jdesc, "1", "手机号不合法");
				} else {
					// 缓存中的验证码
					String vcodeInCache = mobileService
							.getVeriCodeByMobile(mobile);
					if (null == vcodeInCache) {
						log.debug(mobile + " 手机的验证码在缓存中不存在");
						return busiErrorBean(jbean, jdesc, "4", "验证码已过期，请重新获取");
					} else {
						if (vcodeInCache.equalsIgnoreCase(veriCode)) {
							// 验证码相符
							EucUser eucUser = eucUserService
									.queryUserInfoByMobile(mobile);
							if (null != eucUser) {
								String checkPass = FormUserValidator
										.checkPassword(newpwd);
								if (!"".equals(checkPass)) {
									return busiErrorBean(jbean, jdesc, "3",
											checkPass);
								}
								eucUserService.updatePasswd(eucUser.getId()
										+ "", newpwd);
								mobileService.delVeriCodeByMobile(mobile);
							} else {
								log.debug("手机号对应用户不存在 " + mobile);
								return busiErrorBean(jbean, jdesc, "2",
										"无绑定该手机号的用户");
							}
						} else {
							// 验证码不符
							log.debug("验证码不符");
							return busiErrorBean(jbean, jdesc, "5", "验证码不符");
						}
					}
					log.debug("密码重设成功");
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
}