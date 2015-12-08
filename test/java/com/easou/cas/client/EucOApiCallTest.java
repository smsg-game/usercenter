package com.easou.cas.client;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucToken;
import com.easou.cas.sdk.EucOApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.OAuthParametric;
import com.easou.usercenter.client.api.JUnitEucApiCallAction;

public class EucOApiCallTest extends JUnitEucApiCallAction {

	private static final Log LOG = LogFactory.getLog(EucOApiCallTest.class);

	public static String validateApiUri = "/api2/validate.json";
	
	private static AuthParametric<RequestInfo> oAuthPara = new OAuthParametric();

	@Test
	public void testLogin() throws Exception {
		LOG.info("开始验证登录接口[EucOApiAuthCall.login]...");
		EucApiResult<EucAuthResult> result = EucOApiAuthCall.login(LOGIN_MOBILE,
				LOGIN_MOBILE_PASS_WD,true, null);
		if (CodeConstant.OK.equals(result.getResultCode())) {// 登录成功
			Assert.assertTrue("登录接口可以成功登录", true);
		} else{
			Assert.assertFalse("登录接口异常，登录失败[" + getErrorDesc(result) + "]",
					true);
		}
		LOG.info("结束验证登录接口[EucOApiAuthCall.login]...");
	}

	//@Test
	public void testValidate() throws Exception {
		EucApiResult<EucAuthResult> result = EucOApiAuthCall.login(LOGIN_MOBILE,
				LOGIN_MOBILE_PASS_WD,true, null);
		if (CodeConstant.OK.equals(result.getResultCode())) {// 登录成功
			Assert.assertTrue("登录接口可以成功登录", true);
		} else {
			Assert.assertFalse("登录接口异常，登录失败[" + getErrorDesc(result) + "]",
					true);
		}
		/* 验证U登录 */
		{
			JBody jb = new JBody();
			// jb.put("EASOUTGC", result.getResult().getToken().getToken());
			jb.put("U", result.getResult().getU().getU());

			// 解析返回結果
			JBean jbean = EucService.getInstance().getResult(validateApiUri,
					jb,oAuthPara,null);
			EucApiResult<EucAuthResult> authResult = new EucApiResult<EucAuthResult>(
					jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token",
						EucToken.class);
				// EucUCookie u = jbean.getBody().getObject("U",
				// EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token, jUser,
						null, String.valueOf(jUser.getId()), false);
				authResult.setResult(eucAuthResult);
				if (CodeConstant.OK.equals(result.getResultCode())) {
					Assert.assertTrue("验证验证接口成功", true);
				} else {
					Assert.assertFalse("验证接口异常，验证失败[" + getErrorDesc(result)
							+ "]", true);
				}
			}
		}
		/** 验证TGC */
		{
			JBody jb = new JBody();
			jb.put("EASOUTGC", result.getResult().getToken().getToken());
			// jb.put("U", result.getResult().getU().getU());

			// 解析返回結果
			JBean jbean = EucService.getInstance().getResult(validateApiUri,
					jb,oAuthPara, null);
			EucApiResult<EucAuthResult> authResult = new EucApiResult<EucAuthResult>(
					jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token",
						EucToken.class);
				// EucUCookie u = jbean.getBody().getObject("U",
				// EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token, jUser,
						null, String.valueOf(jUser.getId()), false);
				authResult.setResult(eucAuthResult);
				if (CodeConstant.OK.equals(result.getResultCode())) {
					Assert.assertTrue("验证验证接口成功", true);
				} else {
					Assert.assertFalse("验证接口异常，验证失败[" + getErrorDesc(result)
							+ "]", true);
				}
			}
		}
		/** 验证U(无效）TGC有效 */
		{
			JBody jb = new JBody();
			jb.put("EASOUTGC", "12345678");
			jb.put("U", result.getResult().getU().getU());

			// 解析返回結果
			JBean jbean = EucService.getInstance().getResult(validateApiUri,
					jb,oAuthPara,null);
			EucApiResult<EucAuthResult> authResult = new EucApiResult<EucAuthResult>(
					jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token",
						EucToken.class);
				// EucUCookie u = jbean.getBody().getObject("U",
				// EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token, jUser,
						null, String.valueOf(jUser.getId()), false);
				authResult.setResult(eucAuthResult);
				if (CodeConstant.OK.equals(result.getResultCode())) {
					Assert.assertTrue("验证验证接口成功", true);
				} else {
					Assert.assertFalse("验证接口异常，验证失败[" + getErrorDesc(result)
							+ "]", true);
				}
			}
		}
		/** 验证U(有效）TGC无效 */
		{
			JBody jb = new JBody();
			jb.put("EASOUTGC", result.getResult().getToken().getToken());
		    jb.put("U", "12345678");

			// 解析返回結果
			JBean jbean = EucService.getInstance().getResult(validateApiUri,
					jb,oAuthPara,null);
			EucApiResult<EucAuthResult> authResult = new EucApiResult<EucAuthResult>(
					jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token",
						EucToken.class);
				// EucUCookie u = jbean.getBody().getObject("U",
				// EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token, jUser,
						null, String.valueOf(jUser.getId()), false);
				authResult.setResult(eucAuthResult);
				if (CodeConstant.OK.equals(result.getResultCode())) {
					Assert.assertTrue("验证验证接口成功", true);
				} else {
					Assert.assertFalse("验证接口异常，验证失败[" + getErrorDesc(result)
							+ "]", true);
				}
			}
		}
		/** 验证U(无效）TGC无效 */
		{
			JBody jb = new JBody();
			jb.put("EASOUTGC","12345678");
		    jb.put("U", "12345678");

			// 解析返回結果
			JBean jbean = EucService.getInstance().getResult(validateApiUri,
					jb,oAuthPara,null);
			EucApiResult<EucAuthResult> authResult = new EucApiResult<EucAuthResult>(
					jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token",
						EucToken.class);
				// EucUCookie u = jbean.getBody().getObject("U",
				// EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token, jUser,
						null, String.valueOf(jUser.getId()), false);
				authResult.setResult(eucAuthResult);
				if (CodeConstant.OK.equals(result.getResultCode())){
					Assert.assertTrue("验证验证接口错误", false);
				} else {
					Assert.assertTrue("验证接口正常，验证提示信息[" + getErrorDesc(result)
							+ "]", true);
				}
			}
		}
	}
	
	
	//@Test
	/*public void testValidateServiceTicket() throws Exception {
		
		EucApiResult<EucAuthResult> authResult = EucOApiAuthCall.validateServiceTicket("ST-1-s9ksKlQWo7mDSst2xM5d-sso");
		if(CodeConstant.OK.equals(authResult.getResultCode())){
			Assert.assertTrue("验证成功", true);
			System.out.println(authResult.getResult().getToken());
		}else{
			Assert.assertTrue("验证失败", false);
		}
	}
	
	public static void main(String[] args) {
		try {
			EucApiResult<EucAuthResult> authResult = EucOApiAuthCall.validateServiceTicket("ST-1-s9ksKlQWo7mDSst2xM5d-sso");
			if(CodeConstant.OK.equals(authResult.getResultCode())){
				System.out.println(authResult.getResult().getToken());
			}else{
				System.out.println(authResult.getResultCode());
			}
		} catch (EucParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
