package com.easou.cas.authenticateion;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.AuthenticationResult;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;

public class ThirdCredentialsToPrincipalResolver implements
		CredentialsToPrincipalResolver {

	@Override
	public boolean supports(Credentials credentials) {
		if (credentials instanceof AccessTokenCredentials)
			return true;
		return false;
	}


	@Override
	public Principal resolvePrincipal(Credentials credentials,
			AuthenticationResult authResult) {
		ThirdPartUserInfo loginInfo = (ThirdPartUserInfo) authResult
				.getAuthInfo();
		Map<String, Object> attributes = new HashMap();
		attributes.put("oid", loginInfo.getThirdId());
		attributes.put("accCode", loginInfo.getAccToken());
		// 获取用户ID
		ThirdPrincipal pri = new ThirdPrincipal(loginInfo.getEasouId(),
				attributes);
		return pri;
	}
}
