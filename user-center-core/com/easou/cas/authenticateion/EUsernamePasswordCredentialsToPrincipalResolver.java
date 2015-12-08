package com.easou.cas.authenticateion;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.authentication.AuthenticationResult;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.BeanUtils;

import com.easou.common.api.JUser;
import com.easou.usercenter.entity.EucUser;

public class EUsernamePasswordCredentialsToPrincipalResolver implements
		CredentialsToPrincipalResolver {
	@Resource(name = "usrInfoCookie")
	CookieRetrievingCookieGenerator cookie;

	public CookieRetrievingCookieGenerator getCookie() {
		return cookie;
	}

	/**
	 * Return true if Credentials are UsernamePasswordCredentials, false
	 * otherwise.
	 */
	public boolean supports(final Credentials credentials) {
		return credentials != null
				&& UsernamePasswordCredentials.class
						.isAssignableFrom(credentials.getClass());
	}

	@Override
	public Principal resolvePrincipal(Credentials credentials,
			AuthenticationResult authResult) {
		EucUser eucUser = (EucUser) authResult.getAuthInfo();
		UsernamePasswordCredentials cre = (UsernamePasswordCredentials) credentials;
		Map<String, Object> attri = new HashMap<String, Object>();
		//attri.put("user", user);
		if ("true".equals(cre.getIsCookie())) {
			attri.put("maxAge", cookie.getCookieMaxAge());
		}
		//通过短信获取手机密码登录
		if(authResult.isRegist()){
			attri.put("isRegist",authResult.isRegist());
		}
		attri.put("regTime", eucUser.getRegisterTime());
		return new SimplePrincipal(String.valueOf(eucUser.getId()), attri);
	}

}
