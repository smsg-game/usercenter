package com.easou.usercenter.util;

import com.easou.common.util.StringUtil;
import com.easou.session.SessionId;
import com.easou.session.SessionIdInfo;

public class SessionUtil {

	public static String getRandomPwd(String esid) {
		SessionIdInfo idinfo = SessionId.explain(esid);
		StringBuffer str = new StringBuffer();
		str.append(idinfo.getFirstDate() == 0 ? idinfo.getCreateDate() : idinfo
				.getFirstDate());
		str.append(idinfo.getSerialNo());
		return str.toString();
	}
	
	public static String getNewRandomPwd(String esid) {
		SessionIdInfo idinfo = SessionId.explain(esid);
		StringBuffer str = new StringBuffer();
		str.append(idinfo.getFirstDate() == 0 ? idinfo.getCreateDate() : idinfo
				.getFirstDate());
		str.append(StringUtil.random(4094));
		return str.toString();
	}
}
