package com.easou.cas.client;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
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
public class EucInternalApiCall {

	protected static EucService eucService =EucService.getInstance();
	
	private static AuthParametric<RequestInfo> authPara = new InAuthParametric();
	
	/**
	 * 请求绑定手机号，系统会生成一个手机号与密码对
	 * @param mobile 需要绑定的手机号
	 * @param isSendMsg	是否发送系统定义短信
	 * @param info
	 * @return	返回验证码
	 * @throws EucParserException
	 */
	public static EucApiResult<String> requestBindMobile(long id, String mobile, boolean isSendMsg, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("id", id + "");
		jbody.putContent("mobile", mobile);
		if (isSendMsg) {
			jbody.putContent("sendMsg", true);
		}
		JBean jbean = eucService.getResult("/api/requestBindMobile", jbody,authPara, info);
		EucApiResult<String> result = new EucApiResult<String>(jbean);
		JBody jb = jbean.getBody();
		if (null != jb) {
			result.setResult(jb.getString("veriCode"));
		} 
		return result;
	}
	
	/**
	 * 根据id绑定手机号
	 * @param id
	 * @param mobile
	 * @param veriCode
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> applyBindMobile(long id, String mobile, String veriCode, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("id", id + "");
		jbody.putContent("mobile", mobile);
		jbody.putContent("veriCode", veriCode);
		JBean jbean = eucService.getResult("/api/applyBindMobile", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if(jbean.getBody()!=null){
		    JUser jUser = jbean.getBody().getObject("user",JUser.class);
		    result.setResult(jUser);
		}
		return result;
	}
	
	/**
	 * 更新用户信息
	 * @param id
	 * @param juser
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> updateUser(long id, JUser juser, RequestInfo info) throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("id", id + "");
		jbody.putContent("user", juser);
		JBean jbean = eucService.getResult("/api/updateUser", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if(jbean.getBody()!=null){
		    JUser jUser = jbean.getBody().getObject("user",JUser.class);
		    result.setResult(jUser);
		}
		return result;
	}
	
	/**
	 * 更新用户密码
	 * @param id
	 * @param oldPass
	 * @param newPass
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<Boolean> updatePass(long id, String oldPass, String newPass, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("id", id + "");
		jbody.putContent("oldPass", oldPass);
		jbody.putContent("newPass", newPass);
		JBean jbean = eucService.getResult("/api/updatePasswd", jbody,authPara, info);
		EucApiResult<Boolean> result = new EucApiResult<Boolean>(jbean);
		if(result.getResultCode().equals(CodeConstant.OK)){
			result.setResult(true);
		}else{
			result.setResult(false);
		}
		return result;
	}
	
	/**
	 * 直接通过手机号和密码注册(内部接口)
	 * @param mobile
	 * @param password
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> regByRealMobile(String mobile,
			String password, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("mobile", mobile);
		jbody.putContent("password", password);
		JBean jbean = eucService.getResult("/api/regByRealMobile", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if(jbean.getBody()!=null){
		    JUser jUser = jbean.getBody().getObject("user",JUser.class);
		    result.setResult(jUser);
		}
		return result;
	}
	
	/**
	 * 直接给用户绑定手机号接口,手机号必须是应用真正获得的用户手机(内部接口)
	 * @param id
	 * @param mobile
	 * @param info
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> directlyBindMobileString(long id,
			String mobile, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		// 是否需要鉴权
		jbody.putContent("id", id);
		jbody.putContent("mobile", mobile);
		JBean jbean = eucService.getResult("/api/directlyBindMobile", jbody,authPara, info);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser jUser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}
}

