package com.easou.cas.sdk;

import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.OAuthParametric;
import com.easou.common.util.CookieUtil;

public class EucUserCall {

	private static String GET_USER_INFO_BY_ID = "/api2/getUserById.json";

	private static String UPDATE_USER_INFO = "/api2/updateUser.json";

	private static AuthParametric authPara = new OAuthParametric();

	private static EucService eucService = EucService.getInstance();

	/**
	 * 根据用户ID获取信息
	 * 
	 * @param token
	 * @param id
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> getUserById(String token, long id)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.put(CookieUtil.COOKIE_TGC, token);
		jbody.putContent("id", id);
		JBean jBean = eucService.getResult(GET_USER_INFO_BY_ID, jbody,
				authPara, null);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jBean);
		if (jBean.getBody() != null) {
			JUser jUser = jBean.getBody().getObject("user", JUser.class);
			result.setResult(jUser);
		}
		return result;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param token
	 * @param jUser
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<JUser> updateUser(String token, JUser jUser)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.put(CookieUtil.COOKIE_TGC, token);
		// 是否提交修改请求
		//boolean isUpdate = false;
		if (jUser != null) {
			if (jUser.getNickName() != null && !"".equals(jUser.getNickName())) {
				jbody.put("nickName", jUser.getNickName());
				//isUpdate = true;
			}
			if (jUser.getOccuId() != null) {
				jbody.put("occuId", jUser.getOccuId());
				//isUpdate = true;
			}
			if (jUser.getBirthday() != null) {
				jbody.put("birthday", jUser.getBirthday());
				//isUpdate = true;
			}
			if (jUser.getSex() != null) {
				jbody.put("sex", jUser.getSex());
				//isUpdate = true;
			}
			if (jUser.getCity() != null && !"".equals(jUser.getCity())) {
				jbody.put("city", jUser.getCity());
				//isUpdate = true;
			}
			/*if (jUser.getStatus() != null) {
				jbody.put("status", jUser.getStatus());
				//isUpdate = true;
			}*/
		}
		JBean jBean = eucService.getResult(UPDATE_USER_INFO, jbody, authPara,
				null);
		EucApiResult<JUser> result = new EucApiResult<JUser>(jBean);
		if (jBean.getBody() != null) {
			JUser tempUser = jBean.getBody().getObject("user", JUser.class);
			result.setResult(tempUser);
		}
		return result;
	}

}
