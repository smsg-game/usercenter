package com.easou.usercenter.client.api;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.cas.client.EucApiCall;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.InAuthParametric;
import com.easou.common.util.CookieUtil;

/**
 * EucApiAuthCall类单元测试类
 * 
 * @author damon
 * @since 2012.11.27
 * @version 1.0
 *
 */
public class EucApiAuthCallTest extends JUnitEucApiCallAction {
	
	private static final Log LOG = LogFactory.getLog(EucApiAuthCallTest.class);
	
	protected static EucService eucService = EucService.getInstance();
	
	private static AuthParametric authPara = new InAuthParametric();
	


	/**
	 * 测试登录接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLogin() throws Exception {
		LOG.info("开始登录接口[EucApiCall.autoAKeyRegist]验证...");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// 是否记住密码
		Boolean[] autoLogins = new Boolean[2];
		autoLogins[0] = true;// 记住密码
		autoLogins[1] = false;// 不记住密码
		for (Boolean autoLogin : autoLogins) {
			// 验证登录
			EucApiResult<EucAuthResult> result = EucApiAuthCall.login(request,
					response, LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, autoLogin, getRequestInfo());
			if (isReturnSuc(result)) {// 登录成功
				Assert.assertTrue("登录接口可以成功登录", true);
				if (autoLogin) {
					// 判断COOKIE是否写入
					Assert.assertTrue("登录接口要求记住密码标识[" + autoLogin
							+ "],未能成功写入Cookie",
							response.getCookie(CookieUtil.COOKIE_U) != null);
				}
				Assert.assertTrue("登录接口EASOUTGC写入COOKIE失败",
						response.getCookie(CookieUtil.COOKIE_TGC) != null);
			} else {// 登录失败
				Assert.assertFalse("登录接口异常，登录失败[" + getErrorDesc(result) + "]",
						true);
			}
		}
		LOG.info("结束登录接口[EucApiCall.autoAKeyRegist]验证。");

	}

	/**
	 * 测试自动登录接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAuthLoginApp() throws Exception {
		LOG.info("开始自动登录接口[EucApiCall.autoLogin]验证...");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// 测试前需要预先登录,而且必须是记住密码登录
		EucApiResult<EucAuthResult> result = EucApiAuthCall.login(request,
				response, LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, true, getRequestInfo());
		if (!isReturnSuc(result)) {
			Assert.assertFalse("登录失败", true);
			Assert.assertEquals("登录失败",
					response.getCookie(CookieUtil.COOKIE_U) != null);
			Assert.assertEquals("登录失败",
					response.getCookie(CookieUtil.COOKIE_TGC) != null);
		}

		//EucParser eup = new EucParser("HG%^&*DF", "1.0", "euc");
		request.setRequestURI("/api/login");
		request.setMethod("POST");
		/*{// cookie U 登录
			JBody jb = new JBody();
			jb.put(CookieUtil.COOKIE_U, response.getCookie(CookieUtil.COOKIE_U)
					.getValue());
			// jb.putContent("userName", LOGIN_MOBILE);
			// jb.putContent("password", LOGIN_MOBILE_PASS_WD);
			String json = eup.parserJsonString(jb, System.currentTimeMillis()
					/ 1000 + "");
			request.setContent(json.getBytes());
			this.excuteAction(request, response);
			String reJson = response.getContentAsString();
			LOG.info("登录结果："+reJson);
			JBean jBean = eup.parserJsonResult(reJson);
			// EucToken token = jBean.getBody().getObject("token",
			// EucToken.class);
			JUser user = jBean.getBody().getObject("user", JUser.class);
			// boolean isRegist = jBean.getBody().getBooleanValue("isRegist");
			// String esid = jBean.getBody().getString("esid");
			Assert.assertNotNull("自动登录接口异常，登录失败。", user);
		}*/
		{// cookie TGC 登录
			JBody jb = new JBody();
			jb.put(CookieUtil.COOKIE_TGC,
					response.getCookie(CookieUtil.COOKIE_TGC).getValue());
			// jb.putContent("userName", LOGIN_MOBILE);
			// jb.putContent("password", LOGIN_MOBILE_PASS_WD);
			JBean jBean = EucService.getInstance().getResult("/api/login", jb, authPara, null);		
			// EucToken token = jBean.getBody().getObject("token",
			// EucToken.class);
			JUser user = jBean.getBody().getObject("user", JUser.class);
			// boolean isRegist = jBean.getBody().getBooleanValue("isRegist");
			// String esid = jBean.getBody().getString("esid");
			Assert.assertNotNull("自动登录接口异常，登录失败。", user);
		}
		// System.out.println(response.getContentAsString());
		LOG.info("结束登录接口[EucApiCall.autoLogin]验证。");

	}

	/**
	 * 应用登录接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoginApp() throws Exception {
		LOG.info("开始app登录接口[EucApiCall.autoLogin]验证。");
		EucApiResult<EucAuthResult> result = 
				EucApiAuthCall.login(LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, "app", getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("app登录接口正常",true);
		}else{
			Assert.assertFalse("app登录接口异常["+getErrorDesc(result)+"]",true);
		}
		LOG.info("结束app登录接口[EucApiCall.autoLogin]验证。");
	}
	
	/**
	 * 验证登出接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoginout()throws Exception {
		LOG.info("开始登出接口[EucApiCall.loginout]验证...");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// 验证登录
		EucApiResult<EucAuthResult> result = EucApiAuthCall.login(request,
							response, LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, false, getRequestInfo());
		if(!isReturnSuc(result)){
			Assert.assertFalse("登录失败",true);
		}
		Assert.assertTrue("登录失败",response.getCookie(CookieUtil.COOKIE_TGC)!=null);
		String tgc = response.getCookie(CookieUtil.COOKIE_TGC).getValue();
		MockHttpServletResponse tResponse = new MockHttpServletResponse();
		//EucParser eup = new EucParser("HG%^&*DF", "1.0", "euc");
		request.setRequestURI("/api/logout");
		request.setMethod("POST");
		JBody jb = new JBody();
		//传递参数
		jb.put(CookieUtil.COOKIE_TGC, tgc);
		/*String json = eup.parserJsonString(jb, System.currentTimeMillis()
				/ 1000 + "");*/
		JBean jBean = EucService.getInstance().getResult("/api/logout", jb, authPara, null);		
		//request.setContent(json.getBytes());
		//执行退出操作
		//this.excuteAction(request, tResponse);
		
		//退出后再去访问用户中，是否还是登录状态
		/*tResponse = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setRequestURI("/api/login");
		request.setMethod("POST");
		request.setContent(json.getBytes());
		//执行自动登录操作
		this.excuteAction(request, tResponse);
		JBean jBean1 = eup.parserJsonResult(tResponse.getContentAsString());
		EucApiResult tResult = new EucApiResult(jBean);
		if(!isReturnSuc(tResult)){//登录不成功说明退出成功，否则失败
			Assert.assertTrue("登录接口正常，用户退出成功",true);
		}else{
			Assert.assertFalse("登录接口异常，用户退出失败",true);
		}*/
		LOG.info("结束登出接口[EucApiCall.loginout]验证...");
	}

	/**
	 * 一键注册接口
	 */
	/*@Test
	public void testAutoAKeyRegistApp() throws Exception {
		LOG.info("开始验证一键注册接口[EucApiCall.autoAKeyRegist]...");
		try {
			EucApiResult<EucAuthResult> result = EucApiAuthCall
					.autoAKeyRegist(getRequestInfo());
			if (result.getResultCode() == EucApiResult.SUCCESS_CODE) {// 注册成功
				Assert.assertNotNull("一键注册功能异常,无返回结果", result.getResult());
				// 获取用户
				ExpJUser expJUser = (ExpJUser)result.getResult().getUser();
				Assert.assertNotNull("一键注册功能异常,无返回用户信息", expJUser);
				Assert.assertNotNull("一键注册功能异常,密码不能为空", expJUser.getPasswd());
				// 登录验证注册的用户是否有效
				EucApiResult<EucAuthResult> loginResult = EucApiAuthCall.login(
						expJUser.getName(), expJUser.getPasswd(), "app",
						getRequestInfo());
				//
				if (loginResult.getResultCode() == EucApiResult.SUCCESS_CODE) {
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
	*/
	/**
	 * 一键注册接口
	 *//*
	@Test
	public void testAutoAKeyRegist() throws Exception {
		LOG.info("开始验证一键注册接口[EucApiCall.autoAKeyRegist]...");
		try {
			MockHttpServletRequest request = new MockHttpServletRequest();
			MockHttpServletResponse response = new MockHttpServletResponse();
			EucApiResult<EucAuthResult> result = EucApiAuthCall
					.autoAKeyRegist(request,response,true,getRequestInfo());
			if (result.getResultCode() == EucApiResult.SUCCESS_CODE) {// 注册成功
				Assert.assertNotNull("一键注册功能异常,无返回结果", result.getResult());
				// 获取用户
				ExpJUser expJUser = (ExpJUser)result.getResult().getUser();
				Assert.assertNotNull("一键注册功能异常,无返回用户信息", expJUser);
				Assert.assertNotNull("一键注册功能异常,密码不能为空", expJUser.getPasswd());
				Assert.assertNotNull("一键注册功能异常,COOKIE中没有写入TGC信息",response.getCookie(CookieUtil.COOKIE_TGC));
				Assert.assertNotNull("一键注册功能异常,COOKIE中没有写入U信息",response.getCookie(CookieUtil.COOKIE_U));
				// 登录验证注册的用户是否有效
				EucApiResult<EucAuthResult> loginResult = EucApiAuthCall.login(
						expJUser.getName(), expJUser.getPasswd(), "app",
						getRequestInfo());
				//
				if (loginResult.getResultCode() == EucApiResult.SUCCESS_CODE) {
					LOG.info("一键注册功能正常,注册成功的用户名[" + expJUser.getName()
							+ "]注册密码[" + expJUser.getPasswd() + "]");
					Assert.assertTrue(true);
				} else {
					LOG.error("一键注册异常，注册后无法正常登录...");
					Assert.assertFalse(true);
				}
			} else {
				LOG.error("一键注册失败【"+getErrorDesc(result)+"】");
				Assert.assertFalse(true);
			}

		} catch (EucParserException e) {
			LOG.error("程序异常，一键注册用户失败", e);
			Assert.assertFalse(true);
			e.printStackTrace();
		} finally {
			LOG.info("结束一键注册接口[EucApiCall.autoAKeyRegist]验证。");
		}
	}*/
	
	/**
	 * 直接通过手机号和密码注册(内部接口)
	 */
	//@SimpleTest
	public void testRegByRealMobile()throws Exception {
		LOG.info("开始验证注册【手机密码】接口[EucApiCall.regByRealMobile]...");
		EucApiResult<EucAuthResult> result = 
				EucApiAuthCall.regByRealMobile(proRandomMobile(), LOGIN_MOBILE_PASS_WD, getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("注册【手机密码】接口正常，注册成功。",true);
		}else{
			Assert.assertFalse("注册【手机密码】接口异常,注册失败["+getErrorDesc(result)+"]",true);
		}
		LOG.info("结束验证注册【手机密码】接口[EucApiCall.regByRealMobile]...");
		
	}
	
	/**
	 * 登录名密码注册后直接登录
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegisterByNameApp()throws Exception {
		
		LOG.info("开始验证注册【录名密码】接口[EucApiCall.regByRealMobile]...");
		EucApiResult<EucAuthResult> result = 
				EucApiAuthCall.registerByName(proRandomName(), LOGIN_MOBILE_PASS_WD, "app", getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("注册【录名密码】接口正常，注册成功。",true);
		}else{
			Assert.assertFalse("注册【录名密码】接口异常,注册失败["+getErrorDesc(result)+"]",true);
		}
		LOG.info("结束验证注册【录名密码】接口[EucApiCall.regByRealMobile]...");
		
	}
	
	/**
	 * 登录名密码注册后直接登录
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegisterByName()throws Exception {
		
		LOG.info("开始验证注册【录名密码】接口[EucApiCall.regByRealMobile]...");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		EucApiResult<EucAuthResult> result = 
				EucApiAuthCall.registerByName(request,response,proRandomName(), LOGIN_MOBILE_PASS_WD, true, getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("注册【录名密码】接口正常，注册成功。",true);
			Assert.assertTrue("注册【录名密码】接口正常，登录成功。",response.getCookie(CookieUtil.COOKIE_TGC)!=null);
			Assert.assertTrue("注册【录名密码】接口正常，登录成功。",response.getCookie(CookieUtil.COOKIE_U)!=null);
		}else{
			Assert.assertFalse("注册【录名密码】接口异常,注册失败["+getErrorDesc(result)+"]",true);
		}
		LOG.info("结束验证注册【录名密码】接口[EucApiCall.regByRealMobile]...");
		
	}
	
	/**
	 * 客户端通过验证码验证注册
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
		EucApiResult<EucAuthResult> result = 
				EucApiAuthCall.regByVericode(mobile, LOGIN_MOBILE_PASS_WD, codeResult.getResult(), getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("获取验证码失败",true);
		}else{
			Assert.assertFalse("获取验证码失败,失败原因["+getErrorDesc(result)+"]",true);
		}
		LOG.info("结束验证注册【验证码】接口[EucApiCall.regByVericode]...");
	}
	
	
}
