package com.easou.usercenter.client.api;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.cas.client.EucApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.InAuthParametric;

/**
 * EucApiCall类单元测试类
 * 
 * @author damon
 * @since 2012.11.27
 * @version 1.0
 * 
 */
public class EucApiCallTest extends JUnitEucApiCallAction {

	/** 新绑定手机号 */
	private static final String NEW_BIND_MOBILE = "15002080897";
	/** 是否需要短息下发 */
	private static final boolean isSendSms = true;

	protected static final Log LOG = LogFactory.getLog(EucApiCallTest.class);

	@Test
	public void testAutoAKeyRegist() {
		RequestInfo info = new RequestInfo();
		info.setEsid("");
		info.setUid("");
		info.setSource("test");
		LOG.info("开始验证一键注册接口[EucApiCall.autoAKeyRegist]...");
		try {
			EucApiResult<ExpJUser> result = EucApiCall.autoAKeyRegist(info);
			if (CodeConstant.OK.equals(result.getResultCode())) {// 注册成功
				// 获取用户
				ExpJUser expJUser = result.getResult();
				// 登录验证注册的用户是否有效
				EucApiResult<EucAuthResult> loginResult = EucApiAuthCall.login(
						expJUser.getName(), expJUser.getPasswd(), "app", info);
				//
				if (CodeConstant.OK.equals(result.getResultCode())) {
					LOG.info("一键注册功能正常,注册成功的用户名[" + expJUser.getName()
							+ "]注册密码[" + expJUser.getPasswd() + "]");
					Assert.assertTrue(true);
				} else {
					LOG.error("一键注册异常，注册后无法正常登录...");
					Assert.assertFalse(true);
				}
			} else {
				LOG.error("一键注册失败");
				Assert.assertFalse(true);
			}

		} catch (EucParserException e) {
			LOG.error("程序异常，一键注册用户失败", e);
			Assert.assertFalse(true);
			e.printStackTrace();
		} finally {
			LOG.info("结束一键注册接口[EucApiCall.autoAKeyRegist]验证。");
		}
	}

	/**
	 * 验证requestBindMobile和applyBindMobile接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testApplyBindMobile() throws Exception {
		LOG.info("开始请求绑定和验证绑定接口[EucApiCall.requestBindMobile和applyBindMobile]验证...");
		// TODO 登录
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		EucApiResult<EucAuthResult> result = EucApiAuthCall.login(request,
				response, LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, true,
				getRequestInfo());
		if (!isReturnSuc(result)) {// 登录失败
			Assert.assertFalse("", true);
		}
		// TODO 获取绑定验证码
		EucApiResult<String> rBindResult1 = EucApiCall.requestBindMobile(result
				.getResult().getToken(), NEW_BIND_MOBILE, isSendSms,
				getRequestInfo());
		if (isReturnSuc(rBindResult1)) {// 判断获取绑定信息是否成功
			Assert.assertTrue("", rBindResult1.getResult() != null);
		} else {
			Assert.assertFalse("获取绑定信息失败[" + getErrorDesc(rBindResult1) + "]",
					true);
		}
		/*// TODO 验证绑定
		EucApiResult<JUser> aBindResult1 = EucApiCall.applyBindMobile(result
				.getResult().getToken(), NEW_BIND_MOBILE, rBindResult1
				.getResult(), getRequestInfo());
		if (isReturnSuc(aBindResult1)) {
			Assert.assertTrue("验证绑定", aBindResult1.getResult() != null);
			// TODO 绑定成功后，原绑定手机号应不能 成功登录
			EucApiResult<EucAuthResult> result2 = EucApiAuthCall.login(request,
					response, LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, true,
					getRequestInfo());
			if (isReturnSuc(result2)) {
				Assert.assertFalse("绑定成功后，原绑定手机号应不能 成功登录", result2.getResult() != null);
			}
		}
		// TODO 还原绑定手机号
		{
			// TODO 获取绑定验证码
			EucApiResult<String> rBindResult2 = EucApiCall.requestBindMobile(
					result.getResult().getToken(), LOGIN_MOBILE, false,
					getRequestInfo());
			if (isReturnSuc(rBindResult2)) {// 判断获取绑定信息是否成功
				Assert.assertTrue("获取绑定", rBindResult2.getResult() != null);
			} else {
				Assert.assertFalse("获取绑定失败【"+getErrorDesc(rBindResult2)+"】", true);
			}
			// TODO 验证绑定
			EucApiResult<JUser> aBindResult2 = EucApiCall.applyBindMobile(
					result.getResult().getToken(), LOGIN_MOBILE,
					rBindResult2.getResult(), getRequestInfo());
			if (isReturnSuc(aBindResult2)) {
				Assert.assertTrue("还原绑定手机号", aBindResult2.getResult() != null);
			} else {
				Assert.assertFalse("还原绑定手机号失败", true);
			}
		}*/
		LOG.info("结束请求绑定和验证绑定接口[EucApiCall.requestBindMobile和applyBindMobile]验证。");
	}
	
	/**
	 * 获取职业信息列表
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetOccupations()throws Exception {
		LOG.info("开始获取职业信息列表接口[EucApiCall.requestBindMobile和applyBindMobile]验证...");
		EucApiResult<List<String[]>> result = EucApiCall.getOccupations(getRequestInfo());
		if (CodeConstant.OK.equals(result.getResultCode())){
			Assert.assertTrue("获取职业信息列表",true);
		}else{
			Assert.assertFalse("获取职业信息列表失败【"+getErrorDesc(result)+"】",true);
		}
		LOG.info("结束获取职业信息列表接口[EucApiCall.requestBindMobile和applyBindMobile]验证。");
	}
	
	/**
	 * 根据ID获取用户信息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserbyId()throws Exception {
		LOG.info("开始根据ID获取用户信息接口[EucApiCall.requestBindMobile和applyBindMobile]验证...");
		//TODO 登录
		EucAuthResult authResult = login();
		//TODO 通过ID获取用户信息
		EucApiResult<JUser> userResult = 
				EucApiCall.getUserbyId(authResult.getUser().getId(), getRequestInfo());
		if (CodeConstant.OK.equals(userResult.getResultCode())){
			Assert.assertTrue("根据ID获取用户信息",true);
		}else{
			Assert.assertFalse("根据ID获取用户信息失败【"+getErrorDesc(userResult)+"】",true);
		}
		LOG.info("结束根据ID获取用户信息接口[EucApiCall.requestBindMobile和applyBindMobile]验证。");
	}
	
	/**
	 * 根据用户手机号获取用户信息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserbyMobile()throws Exception {
		LOG.info("开始根据用户手机号获取用户信息接口[EucApiCall.getUserbyMobile]验证...");
		EucApiResult<JUser> result = EucApiCall.getUserbyMobile(LOGIN_MOBILE, getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("根据用户手机号获取用户信息",result.getResult()!=null);
		}else{
			Assert.assertTrue("根据用户手机号获取用户信息失败【"+getErrorDesc(result)+"】",result.getResult()!=null);
		}
		LOG.info("结束根据用户手机号获取用户信息接口[EucApiCall.getUserbyMobile]验证。");
	}
	/**
	 *  客户端通过验证码验证注册
	 *  
	 * @throws Exception
	 */
	@Test
	public void testRegByVericode()throws Exception {
		LOG.info("开始验证注册【验证码】接口[EucApiCall.regByVericode]...");
		String mobile =proRandomMobile();
		EucApiResult<String> codeResult = EucApiCall.requestVericode(mobile, LOGIN_MOBILE_PASS_WD, false, getRequestInfo());
		if(!isReturnSuc(codeResult)){
			Assert.assertFalse("获取验证码失败,失败原因["+getErrorDesc(codeResult)+"]",true);
		}
		EucApiResult<JUser> result = 
				EucApiCall.regByVericode(mobile, LOGIN_MOBILE_PASS_WD, codeResult.getResult(), getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("获取验证码失败",true);
		}else{
			Assert.assertFalse("获取验证码失败,失败原因["+getErrorDesc(result)+"]",true);
		}
		LOG.info("结束验证注册【验证码】接口[EucApiCall.regByVericode]。");
		
	}
	
	/**
	 * 确认重置密码
	 * 
	 * @throws Exception
	 */
	@Test
	public void testApplyResetPass()throws Exception {
		LOG.info("开始重置密码【验证码】接口[EucApiCall.applyResetPass]...");
		EucApiResult<String> result = 
				EucApiCall.requestResetPass(LOGIN_MOBILE, false, getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("获取验证码",result.getResult()!=null);
		}else{
			Assert.assertFalse("获取验证码失败【"+getErrorDesc(result)+"】",true);
		}	
		/*EucApiResult<Boolean> reResult = EucApiCall.applyResetPass(LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, result.getResult(), getRequestInfo());
		if(isReturnSuc(reResult)){
			Assert.assertTrue("更新密码",reResult.getResult());
		}else{
			Assert.assertFalse("更新密码失败【"+getErrorDesc(result)+"】",true);
		}*/
		LOG.info("结束重置密码【验证码】接口[EucApiCall.applyResetPass]。");
	}
	
	/**
	 * 手机号注册接口验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegistByMobile()throws Exception {
		LOG.info("开始手机号注册接口[EucApiCall.registByMobile]验证...");
		EucApiResult<String> result = EucApiCall.registByMobile(proRandomMobile(), true, getRequestInfo());
		LOG.info("返回["+result.getResult()+"]");
		if(isReturnSuc(result)){
			Assert.assertTrue("手机号注册接口验证",true);
		}else{
			Assert.assertFalse("手机号注册接口验证失败【"+getErrorDesc(result)+"】",true);
		}
		LOG.info("结束手机号注册接口[EucApiCall.registByMobile]验证。");
	}
	
	/**
	 * 用户名注册接口验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegistByName()throws Exception {
		LOG.info("开始用户名注册接口[EucApiCall.registByMobile]验证...");
		EucApiResult<JUser> result = EucApiCall.registByName(proRandomName(), LOGIN_MOBILE_PASS_WD, getRequestInfo());
		//LOG.info("返回["+result.getResult()+"]");
		if(isReturnSuc(result)){
			Assert.assertTrue("用户名注册接口验证",result.getResult()!=null);
		}else{
			Assert.assertFalse("用户名注册接口验证失败【"+getErrorDesc(result)+"】",true);
		}
		LOG.info("结束用户名注册接口[EucApiCall.registByMobile]验证。");
	}
	
	/**
	 * 重置密码接口验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePass()throws Exception {
		LOG.info("开始用户名注册接口[EucApiCall.updatePass]验证...");
		//TODO 随机生成用户
		String tempPassWd = "123456";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		EucApiResult<EucAuthResult> loginResult = EucApiAuthCall.autoAKeyRegist(getRequestInfo());
		if(!isReturnSuc(loginResult)){
			Assert.assertFalse("登录失败",true);
		}
		//TODO 重置密码
		EucApiResult<Boolean> uResult = 
				EucApiCall.updatePass(loginResult.getResult().getToken(), 
						((ExpJUser)loginResult.getResult().getUser()).getPasswd(), tempPassWd, getRequestInfo());
		if(isReturnSuc(uResult)){
			Assert.assertTrue("重置密码接口验证",uResult.getResult());
		}else{
			Assert.assertFalse("重置密码接口验证失败【"+getErrorDesc(uResult)+"】",uResult.getResult());
		}
		//TODO 用新密码登录
		EucApiResult<EucAuthResult> lResult = 
				EucApiAuthCall.login(loginResult.getResult().getUser().getName(),
					    tempPassWd, "app", getRequestInfo());
		if(isReturnSuc(lResult)){
			Assert.assertTrue("重置密码后用新密码登录",lResult.getResult()!=null);
		}else{
			Assert.assertFalse("置密码后用新密码登录失败【"+getErrorDesc(lResult)+"】",true);
		}

		LOG.info("结束用户名注册接口[EucApiCall.updatePass]验证。");
	}
	
}
