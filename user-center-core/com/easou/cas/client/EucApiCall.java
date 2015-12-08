package com.easou.cas.client;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easou.cas.auth.EucToken;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.InAuthParametric;

/**
 * 接口调用
 * 
 * @author jay,damon
 * @since 2012.06.20
 */
public class EucApiCall {

	protected static EucService eucService = EucService.getInstance();
	
	private static AuthParametric<RequestInfo> authPara = new InAuthParametric();

	/**
	 * 按 id获取用户信息
	 * 
	 * @param id
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> getUserbyId(long id, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("id", id + "");
		JBean jbean = eucService.getResult("/api/getUserById", jbody,authPara,info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}

	/**
	 * 按手机号获取用户
	 * 
	 * @param mobile
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> getUserbyMobile(String mobile,
			RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		JBean jbean = eucService.getResult("/api/getUserByMobile", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}

	/**
	 * 获取职业信息列表
	 * 
	 * @param info
	 * @return	返回职业列表,String[0]为代号,String[1]为名称
	 * @throws EucParserException
	 */
	public static EucApiResult<List<String[]>> getOccupations(RequestInfo info)
			throws EucParserException {
		JBean jbean = eucService.getResult("/api/getOccupations", new JBody(),authPara,
				info);
		EucApiResult<List<String[]>> result = new EucApiResult<List<String[]>>(
				jbean);
		JBody jb = jbean.getBody();
		if (null != jb) {
			JSONArray ja = jbean.getBody().getJSONArray("occus");
			List<String[]> occus = new ArrayList<String[]>();
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String[] temp = new String[2];
				temp[0] = jo.getString("ID");
				temp[1] = jo.getString("NAME");
				occus.add(temp);
			}
			result.setResult(occus);
		}
		return result;
	}

//	/**
//	 * 手机号注册(不发短信)
//	 */
//	public static EucApiResult<String> registByMobile(String mobile,
//			RequestInfo info) throws EucParserException {
//		return registByMobile(mobile, false, info);
//	}

	/**
	 * 通过手机号注册
	 * @param mobile	手机号
	 * @param isSendMsg	是否下发密码短信
	 * @param info		用户访问信息
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<String> registByMobile(String mobile,
			boolean isSendMsg, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		if (isSendMsg) {
			jbody.putContent("sendMsg", true);
		}
		JBean jbean = eucService.getResult("/api/registByMobile", jbody,authPara, info);
		EucApiResult<String> result = new EucApiResult<String>(jbean);
		JBody jb = jbean.getBody();
		if (jb != null) {
			result.setResult(jb.getString("tpass"));
		}
		return result;
	}
	
	/**
	 * 登录名注册接口
	 * 
	 * @param username	登录名
	 * @param password	密码
	 * @param info
	 * @return	用户实体
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> registByName(String username,
			String password, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("username", username);
		jbody.putContent("password", password);
		JBean jbean = eucService.getResult("/api/registByName", jbody,authPara,info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}

	/**
	 * 带鉴权的更新密码接口
	 * 
	 * @param token
	 * @param oldPass
	 * @param newPass
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<Boolean> updatePass(EucToken token,
			String oldPass, String newPass, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		// 鉴权标识
		jbody.putContent("token", token);
		// 是否需要鉴权
		jbody.putContent("isAuth", true);
		jbody.putContent("oldPass", oldPass);
		jbody.putContent("newPass", newPass);
		JBean jbean = eucService.getResult("/api/updatePasswd", jbody,authPara, info);
		EucApiResult<Boolean> result = new EucApiResult<Boolean>(jbean);
		if(result.getResultCode().equals(CodeConstant.OK)) {
			result.setResult(true);
		} else {
			result.setResult(false);
		}
		return result;
	}

	/**
	 * 带鉴权的更新用户信息接口
	 * 
	 * @param token
	 * @param juser
	 * @return
	 * @throws EucParserException
	 * 
	 * @author damon 2012.07.02
	 */
	public static EucApiResult<JUser> updateUser(EucToken token, JUser juser,
			RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		// 鉴权标识
		jbody.putContent("token", token);
		// 是否需要鉴权
		jbody.putContent("isAuth", true);
		jbody.putContent("user", juser);
		JBean jbean = eucService.getResult("/api/updateUser", jbody,authPara,info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}

//	/**
//	 * 请求绑定手机接口
//	 */
//	public static EucApiResult<String> requestBindMobile(EucToken token,
//			String mobile, RequestInfo info) throws EucParserException {
//		return requestBindMobile(token, mobile, false, info);
//	}

	/**
	 * 带鉴权的手机绑定接口（请求）
	 * 
	 * @param token
	 * @param mobile
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<String> requestBindMobile(EucToken token,
			String mobile, boolean isSendMsg, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		// 鉴权标识
		jbody.putContent("token", token);
		// 是否需要鉴权
		jbody.putContent("isAuth", true);
		if (isSendMsg) {
			jbody.putContent("sendMsg", true);
		}
		JBean jbean = eucService.getResult("/api/requestBindMobile", jbody,authPara,
				info);
		EucApiResult<String> result = new EucApiResult<String>(jbean);
		JBody jb = jbean.getBody();
		if (null != jb) {
			result.setResult(jb.getString("veriCode"));
		}
		return result;
	}

	/**
	 * 带鉴权的绑定接口（确认）
	 * 
	 * @param token
	 * @param mobile
	 * @param veriCode
	 * @return
	 * @throws EucParserException
	 * 
	 * @author damon 2012.07.02
	 */
	public static EucApiResult<JUser> applyBindMobile(EucToken token,
			String mobile, String veriCode, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		// 鉴权标识
		jbody.putContent("token", token);
		// 是否需要鉴权
		jbody.putContent("isAuth", true);
		jbody.putContent("mobile", mobile);
		jbody.putContent("veriCode", veriCode);
		JBean jbean = eucService.getResult("/api/applyBindMobile", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}

//	/**
//	 * 请求重设密码接口
//	 */
//	public static EucApiResult<String> requestResetPass(String mobile,
//			RequestInfo info) throws EucParserException {
//		return requestResetPass(mobile, false, info);
//	}
	
	/**
	 * 请求重置密码，将获得验证码
	 * @param mobile 提供一个已绑定的手机号
	 * @param info 应用请求信息(esid,uid,source等)
	 * @return	与手机号对应的验证码
	 * @throws EucParserException
	 */
	public static EucApiResult<String> requestResetPass(String mobile, boolean isSendMsg,
			RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		if (isSendMsg) {
			jbody.putContent("sendMsg", true);
		}
		JBean jbean = eucService
				.getResult("/api/requestResetPass", jbody,authPara, info);
		EucApiResult<String> result = new EucApiResult<String>(jbean);
		JBody jb = jbean.getBody();
		if (null != jb) {
			result.setResult(jb.getString("veriCode"));
		}
		return result;
	}

	/**
	 * 确认重置密码
	 * @param mobile 要重置密码的用户的绑定手机号
	 * @param newpwd 新密码
	 * @param veriCode 收到的验证码
	 * @param info 用户请求信息
	 * @return	是否重置成功
	 * @throws EucParserException
	 */
	public static EucApiResult<Boolean> applyResetPass(String mobile,
			String newpwd, String veriCode, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		jbody.putContent("newpwd", newpwd);
		jbody.putContent("veriCode", veriCode);
		JBean jbean = eucService.getResult("/api/applyResetPass", jbody,authPara, info);
		EucApiResult<Boolean> result = new EucApiResult<Boolean>(jbean);
		if(result.getResultCode().equals(CodeConstant.OK)) {
			result.setResult(true);
		} else {
			result.setResult(false);
		}
		return result;
	}
	
	/**
	 * 客户端注册获取验证码
	 * @param mobile
	 * @param password	该密码只作校验功能,不记录.如果是再次获得验证码,该密码可为null,服务端将不再进行密码验证
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<String> requestVericode(String mobile, String password, boolean isSendMsg, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		if(null!=password) {
			jbody.putContent("password", password);
		}
		if (isSendMsg) {
			jbody.putContent("sendMsg", true);
		}
		JBean jbean = eucService.getResult("/api/requestVericode", jbody,authPara, info);
		EucApiResult<String> result = new EucApiResult<String>(jbean);
		JBody jb = jbean.getBody();
		if (null != jb) {
			result.setResult(jb.getString("veriCode"));
		}
		return result;
	}
	
	/**
	 * 客户端通过验证码验证注册
	 * @param mobile
	 * @param password
	 * @param veriCode
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> regByVericode(String mobile, String password, String veriCode, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		jbody.putContent("password", password);
		jbody.putContent("veriCode", veriCode);
		JBean jbean = eucService.getResult("/api/regByVericode", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}
	
	public static EucApiResult<ExpJUser> autoAKeyRegist(RequestInfo info) throws EucParserException{
		JBody jbody = new JBody();
		jbody.putContent("isNeedLogin", false);
		JBean jbean = eucService.getResult("/api/autoAKeyRegist", jbody,authPara,info);
		EucApiResult<ExpJUser> result = new EucApiResult<ExpJUser>(jbean);
		if (jbean.getBody() != null) {
			ExpJUser jUser = jbean.getBody().getObject("user", ExpJUser.class);
			result.setResult(jUser);
		}
		return result;
	}
}