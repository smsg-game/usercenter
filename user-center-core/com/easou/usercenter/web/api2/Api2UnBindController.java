package com.easou.usercenter.web.api2;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.easou.usercenter.asyn.helper.SendSmsHelper;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.MobileService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api2/")
public class Api2UnBindController extends AbstractAPI2Controller {

	@Resource(name = "eucUserServiceImpl")
	protected EucUserService eucUserService;
	@Autowired
	private MobileService mobileService;

	/**
	 * 请求解除绑定手机(发送验证码)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestUnBindMobile.json")
	@ResponseBody
	public String requestUnBindMobile(final HttpServletRequest request,
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
				}
				
				EucUser eucUser = eucUserService.queryUserInfoById(String.valueOf(userId));
				if(eucUser == null){
					return busiErrorBean(jbean, "3", "用户不存在");
				}
				if(mobile != null && !mobile.equals(eucUser.getMobile())){
					return busiErrorBean(jbean, "4", "该手机号不正确");
				}else {
					String veriCode = mobileService.setVeriCodeByMobile(mobile);
					
					JBody jbody = new JBody();
					jbody.putContent("veriCode", "");
					jbean.setBody(jbody);
					
					// 需要发短信
					String message = "验证码: "+veriCode+", 感谢您对梵町的支持!";
					String messageLog = "验证码: xxxx, 感谢您对梵町的支持!";
					// 渠道
					String channel = ConditionUtil.getChannel(request);
					String esid = ConditionUtil.getEsid(request);
					String uid = ConditionUtil.getUid(request);
					SendSmsHelper.sendSms(uid, mobile, message, messageLog, channel, esid,
							SMSType.BIND);
					log.debug("验证码发送成功");
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	/**
	 * 提交解除绑定手机,根据id号来绑定新手机(填入验证码后解除绑定)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("applyUnBindMobile.json")
	@ResponseBody
	public String applyUnBindMobile(final HttpServletRequest request,
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
					return busiErrorBean(jbean, "2", "手机号不合法");
				}
				
				String veriCodeEuc = mobileService.getVeriCodeByMobile(mobile);
				if (null == veriCodeEuc) {
					// 内存中不存在验证码
					return busiErrorBean(jbean, "3", "验证码已失效");
				}
				EucUser eucUser = eucUserService.queryUserInfoById(String.valueOf(userId));
				if(eucUser == null){
					return busiErrorBean(jbean, "4", "用户不存在");
				}
				if(mobile !=null && !mobile.equals(eucUser.getMobile())){
					return busiErrorBean(jbean, "5", "该手机号不正确");
				}else {
					if (veriCodeEuc.equalsIgnoreCase(veriCode)) {
						mobileService.delVeriCodeByMobile(mobile);
						boolean resBool = eucUserService.updateMobileById(String.valueOf(userId), null);
						if(!resBool){
							return busiErrorBean(jbean, "7", "解绑错误");
						}
						
						eucUser = eucUserService.queryUserInfoById(String.valueOf(userId));
						if (null == eucUser) {
							return busiErrorBean(jbean, "4", "用户不存在");
						}
						JBody jbody = new JBody();
						jbody.putContent("user", transJUser(eucUser, new JUser()));
						jbean.setBody(jbody); // 查询到用户，加入返回体
					} else {
						// 验证码不匹配
						return busiErrorBean(jbean, "6", "验证码错误");
					}
					return jbean;
				}
			}
		};
		return abc.genReturnJson();
	}

}
