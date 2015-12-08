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
import com.easou.cas.client.EucInternalApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParser;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.InAuthParametric;
import com.easou.common.util.CookieUtil;
/**
 * 如发现有问题，可以将某个测试方法单独copy到此类进行运行
 * 
 * @author damon
 * @since 2012.11.30
 * @version 1.0
 *
 */
public class SimpleTest extends JUnitEucApiCallAction {
	
	private static final Log LOG = LogFactory.getLog(SimpleTest.class);
	
	private static AuthParametric authPara = new InAuthParametric();
		
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
}
