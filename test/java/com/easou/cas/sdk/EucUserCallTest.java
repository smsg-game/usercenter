package com.easou.cas.sdk;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.easou.cas.auth.EucAuthResult;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.JUser;
import com.easou.usercenter.client.api.JUnitEucApiCallAction;

public class EucUserCallTest extends JUnitEucApiCallAction {

	private static final Log LOG = LogFactory.getLog(EucUserCallTest.class);
	
	//@Test
	public void testGetUserById() throws Exception {
		EucApiResult<EucAuthResult> result = EucOApiAuthCall.login(LOGIN_MOBILE,
				LOGIN_MOBILE_PASS_WD,true,null);
		if (CodeConstant.OK.equals(result.getResultCode())) {// 登录成功
			Assert.assertTrue("登录接口可以成功登录", true);
		} else {
			Assert.assertFalse("登录接口异常，登录失败[" + getErrorDesc(result) + "]",
					true);
		}
		EucApiResult<JUser> r1 = EucUserCall.getUserById(
				result.getResult().getToken().getToken(), result.getResult().getUser().getId());
		if (CodeConstant.OK.equals(r1.getResultCode())) {// 登录成功
			Assert.assertTrue("根据ID获取用户信息接口可以成功，用户:"+r1.getResult().getNickName(), true);
		} else {
			Assert.assertFalse("根据ID获取用户信息接口异常，失败原因[" + getErrorDesc(r1) + "]",
					true);
		}
	}
	
	@Test
	public void testUpdateUserInfo()throws Exception {
		final String nickName = "damon123456";
		EucApiResult<EucAuthResult> result = EucOApiAuthCall.login(LOGIN_MOBILE,
				LOGIN_MOBILE_PASS_WD,true,null);
		if (CodeConstant.OK.equals(result.getResultCode())) {// 登录成功
			Assert.assertTrue("登录接口可以成功登录", true);
		} else {
			Assert.assertFalse("登录接口异常，登录失败[" + getErrorDesc(result) + "]",
					true);
		}
		JUser user = new JUser();
		user.setNickName(nickName);
		user.setSex(1);
		user.setCity("深圳");
		EucApiResult<JUser> r1 = EucUserCall.updateUser(result.getResult().getToken().token, user);
		if (CodeConstant.OK.equals(r1.getResultCode())) {// 登录成功
			Assert.assertEquals(nickName, r1.getResult().getNickName());
		}else {
			Assert.assertTrue("修改用户信息失败[" + getErrorDesc(r1) + "]",true);
		}
	}
	
	protected <T> String getErrorDesc(EucApiResult<T> result) {
		if(CodeConstant.NOT_AUTH.equals(result.getResultCode())){
			return "无调用接口的权限";
		}
		return result.getDescList() != null? result.getDescList().get(0)
				.getD() : "无异常信息";
	}

}
