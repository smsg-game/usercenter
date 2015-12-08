package com.easou.usercenter.client.api;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.cas.client.EucInternalApiCall;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JUser;

/**
 * EucInternalApiCall类单元测试类
 * 
 * @author damon
 * @since 2012.11.30
 * @version 1.0
 * 
 */
public class EucInternalApiCallTest extends JUnitEucApiCallAction {

	private static final Log LOG = LogFactory
			.getLog(EucInternalApiCallTest.class);

	/**
	 * 绑定手机号接口验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void testApplyBindMobile() throws Exception {
		LOG.info("开始绑定手机号接口[ EucInternalApiCall.applyBindMobile]验证...");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// TODO 随机生成登录用户
		EucApiResult<EucAuthResult> result = EucApiAuthCall
				.autoAKeyRegist(request,response,true,getRequestInfo());
		if (isReturnSuc(result)) {
			Assert.assertTrue("登录失败", true);
		}
		// 生成随机手机号
		String tMobile = proRandomMobile();
		String tPasswd = ((ExpJUser) result.getResult().getUser()).getPasswd();
		// TODO 请求绑定
		EucApiResult<String> bResult = EucInternalApiCall.requestBindMobile(
				result.getResult().getUser().getId(), tMobile, false,
				getRequestInfo());
		if (isReturnSuc(bResult)) {
			Assert.assertTrue("请求绑定", bResult.getResult() != null);
		} else {
			Assert.assertFalse("请求绑定失败【" + getErrorDesc(bResult) + "】", true);
		}
		/*EucApiResult<JUser> aResult = EucInternalApiCall.applyBindMobile(result
				.getResult().getUser().getId(), tMobile, bResult.getResult(),
				getRequestInfo());
		if (isReturnSuc(aResult)) {
			Assert.assertTrue("验证绑定", aResult.getResult() != null);
		} else {
			Assert.assertFalse("请求绑定失败【" + getErrorDesc(aResult) + "】", true);
		}*/
		// TODO 用绑定后的手机号登录验证
		//login(tMobile, tPasswd);
		LOG.info("结束绑定手机号接口[ EucInternalApiCall.applyBindMobile]验证...");

	}

	/**
	 * 直接给用户绑定手机号接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDirectlyBindMobileString() throws Exception {
		LOG.info("开始【绑定手机号】接口[ EucInternalApiCall.directlyBindMobileString]验证...");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// TODO 随机生成登录用户
		EucApiResult<EucAuthResult> result = EucApiAuthCall
				.autoAKeyRegist(request,response,true,getRequestInfo());
		if (isReturnSuc(result)) {
			Assert.assertTrue("登录失败", true);
		}
		// 生成随机手机号
		String tMobile = proRandomMobile();
		String tPasswd = ((ExpJUser) result.getResult().getUser()).getPasswd();
		// TODO 绑定手机号
		EucApiResult<JUser> dResult = EucInternalApiCall
				.directlyBindMobileString(result.getResult().getUser().getId(),
						tMobile, getRequestInfo());
		if (isReturnSuc(dResult)) {
			Assert.assertTrue("直接绑定手机号", true);
		} else {
			Assert.assertTrue("直接绑定手机号失败【" + getErrorDesc(dResult) + "】", true);
		}
		// TODO 用绑定后的手机号登录验证
		login(tMobile, tPasswd);
		LOG.info("结束【绑定手机号】接口[ EucInternalApiCall.directlyBindMobileString]验证。");
	}

	/**
	 * 直接通过手机号和密码注册(内部接口)验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegByRealMobile() throws Exception {
		LOG.info("开始【直接通过手机号和密码注册】接口[ EucInternalApiCall.regByRealMobile]验证...");
		String tMobile = proRandomMobile();
		EucApiResult<JUser> result = EucInternalApiCall.regByRealMobile(
				tMobile, LOGIN_MOBILE_PASS_WD, getRequestInfo());
		if (isReturnSuc(result)) {
			Assert.assertTrue("直接通过手机号和密码注册", result.getResult() != null);
		} else {
			Assert.assertFalse("直接通过手机号和密码注册失败【" + getErrorDesc(result) + "】",
					true);
		}
		// DOTO 用新手机号登录验证
		login(tMobile, LOGIN_MOBILE_PASS_WD);
		LOG.info("结束【直接通过手机号和密码注册】接口[ EucInternalApiCall.regByRealMobile]验证。");

	}

	/**
	 * 重置用户密码
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePass() throws Exception {
		LOG.info("开始【重置用户密码】接口[ EucInternalApiCall.updatePass]验证...");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		// TODO 随机生成登录用户
		EucApiResult<EucAuthResult> result = EucApiAuthCall
				.autoAKeyRegist(request,response,true,getRequestInfo());
		if (isReturnSuc(result)) {
			Assert.assertTrue("登录失败", true);
		}
		String userName = result.getResult().getUser().getName();
		String oPasswd =((ExpJUser)result.getResult().getUser()).getPasswd();
		//新密码
		String nPasswd=LOGIN_MOBILE_PASS_WD;
		EucApiResult<Boolean> uResult = 
				EucInternalApiCall.updatePass(result.getResult().getUser().getId(),
						oPasswd, nPasswd, getRequestInfo());
		if(isReturnSuc(uResult)){
			Assert.assertTrue("重置用户密码",uResult.getResult());
		}else{
			Assert.assertFalse("重置用户密码失败【"+getErrorDesc(uResult)+"】",uResult.getResult());
		}
		//TODO 用新密码进行登录验证
		login(userName,nPasswd);
		LOG.info("结束【重置用户密码】接口[ EucInternalApiCall.updatePass]验证。");

	}

	/**
	 * 更新用户信息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateUser() throws Exception {
		LOG.info("开始【更新用户信息】接口[ EucInternalApiCall.updatePass]验证...");
		//TODO 登录获取用户信息
		EucAuthResult result = login(LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD);
		JUser user = result.getUser();
		user.setCity("台湾");
		//user.setNickName();
		//TODO 更新用户信息
		EucApiResult<JUser> uResult = 
				EucInternalApiCall.updateUser(user.getId(), user, getRequestInfo());
		if(isReturnSuc(uResult)){
			Assert.assertTrue("更新用户信息",uResult.getResult()!=null);
		}else{
			Assert.assertFalse("更新用户信息【"+getErrorDesc(uResult)+"】",true);
		}
		//TODO 还原用户信息
		EucInternalApiCall.updateUser(result.getUser().getId(), result.getUser(), getRequestInfo());
		LOG.info("结束【更新用户信息】接口[ EucInternalApiCall.updatePass]验证。");
	}
}
