package com.easou.cas.sdk;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.auth.EucToken;
import com.easou.cas.auth.EucUCookie;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JReason;
import com.easou.common.api.JUser;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.OAuthParametric;

/**
 * 
 * 
 * @author jay
 * @since 2013.01.16
 * @version 1.0
 */
public class EucRegistCall {
	
	protected static EucService eucService = EucService.getInstance();
	
	protected static final Log LOG = LogFactory.getLog(EucRegistCall.class);
	
	public static String registByNameUri = "/api2/registByName.json";
	
	public static String autoRegistUri = "/api2/autoRegist.json";
	
	public static String requestMobileCodeUri = "/api2/requestMobileCode.json";
	
	public static String regByMobileCodeUri = "/api2/regByMobileCode.json";
	
	private static AuthParametric oAuthPara = new OAuthParametric();
	
	/**
	 * 登录名密码注册
	 * @param userName	登录名
	 * @param password		密码
	 * @param remember	是否需要回传记住密码cookie内容
	 * @return
	 */
	public static EucApiResult<EucAuthResult> registByName(String userName,String password, boolean remember){
		JBody jbody = new JBody();
		jbody.putContent("userName", userName);
		jbody.putContent("password", password);
		if(remember) {	// 为真传remember参数
			jbody.putContent("remember", remember);
		}
		try {
			JBean jbean = eucService.getResult(registByNameUri, jbody,oAuthPara, null);
			EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token", EucToken.class);
				EucUCookie u = jbean.getBody().getObject("U", EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token,jUser,u,String.valueOf(jUser.getId()),false);
				result.setResult(eucAuthResult);
				return result;
			}
		} catch (EucParserException e) {
			LOG.error("Parser json error!", e);
		}
		return buildNullResult("9", "注册未成功", null);
	}
	
	/**
	 * 自动注册
	 * @param remember	是否需要回传记住密码cookie内容
	 * @return
	 */
	public static EucApiResult<EucAuthResult> autoRegist(boolean remember){
		JBody jbody = new JBody();
		if(remember) {	// 为真传remember参数
			jbody.putContent("remember", remember);
		}
		try {
			JBean jbean = eucService.getResult(autoRegistUri, jbody,oAuthPara, null);
			EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token", EucToken.class);
				EucUCookie u = jbean.getBody().getObject("U", EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token,jUser,u,String.valueOf(jUser.getId()),false);
				result.setResult(eucAuthResult);
				return result;
			}
		} catch (EucParserException e) {
			LOG.error("Parser json error!", e);
		}
		return buildNullResult("9", "注册未成功", null);
	}
	
	/**
	 * 手机验证码注册
	 * @param mobile			注册手机号
	 * @param password		注册密码
	 * @param veriCode		注册验证码
	 * @param remember	是否需要回传记住密码cookie内容
	 * @return
	 */
	public static EucApiResult<EucAuthResult> regByMobileCode(String mobile, String password, String veriCode, boolean remember){
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		jbody.putContent("password", password);
		jbody.putContent("veriCode", veriCode);
		if(remember) {	// 为真传remember参数
			jbody.putContent("remember", remember);
		}
		try {
			JBean jbean = eucService.getResult(regByMobileCodeUri, jbody,oAuthPara, null);
			EucApiResult<EucAuthResult> result = new EucApiResult<EucAuthResult>(jbean);
			if (jbean.getBody() != null) {
				JUser jUser = jbean.getBody().getObject("user", JUser.class);
				EucToken token = jbean.getBody().getObject("token", EucToken.class);
				EucUCookie u = jbean.getBody().getObject("U", EucUCookie.class);
				EucAuthResult eucAuthResult = new EucAuthResult(token,jUser,u,String.valueOf(jUser.getId()),false);
				result.setResult(eucAuthResult);
				return result;
			}
		} catch (EucParserException e) {
			LOG.error("Parser json error!", e);
		}
		return buildNullResult("9", "注册未成功", null);
	}
	
	/**
	 * 获得手机验证码，服务端会结该手机号发送一个验证码
	 * @param mobile		手机号
	 * @return
	 */
	public static EucApiResult<Object> requestMobileCode(String mobile){
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		try {
			JBean jbean = eucService.getResult(requestMobileCodeUri, jbody,oAuthPara, null);
			EucApiResult<Object> result = new EucApiResult<Object>(jbean);
			result.setResult(null);
		} catch (EucParserException e) {
			LOG.error("Parser json error!", e);
		}
		return buildNullResult("9", "注册未成功", null);
	}
	
	/**
	 * 假如客户端解析报错，则返回一个失败信息
	 * 
	 * @return
	 */
	private static <T> EucApiResult<T> buildNullResult(String code,
			String content, T t) {
		EucApiResult<T> result = new EucApiResult<T>();
		result.setResultCode(CodeConstant.BUSI_ERROR);
		List<JReason> desc = new ArrayList<JReason>();
		desc.add(new JReason(code, content));
		result.setDescList(desc);
		result.setResult(t);
		return result;
	}

}
