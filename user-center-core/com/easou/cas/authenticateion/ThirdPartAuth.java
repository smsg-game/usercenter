package com.easou.cas.authenticateion;

import org.apache.log4j.Logger;
import org.jasig.cas.authentication.AuthenticationResult;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;

import com.easou.cas.login.ValidateUserInfoHolder;

public class ThirdPartAuth extends
		AbstractPreAndPostProcessingAuthenticationHandler {
	protected static Logger log = Logger.getLogger(ThirdPartAuth.class);

	@Override
	public AuthenticationResult doAuthentication(Credentials credentials)
			throws AuthenticationException {
		try {
			AccessTokenCredentials cre = (AccessTokenCredentials) credentials;
			ThirdPartUserInfo uinfo = new ThirdPartUserInfo();
			if (cre.getEasouId() == null || cre.getAccToken() == null
					|| cre.getThirdId() == null) {
				throw new Exception("验证失败!");
			}
			uinfo.setEasouId(cre.getEasouId());
			uinfo.setAccToken(cre.getAccToken());
			uinfo.setThirdId(cre.getThirdId());
			uinfo.setType(cre.getType());
			ValidateUserInfoHolder.setUinfo(uinfo);
			return new AuthenticationResult(true, uinfo);
		} catch (Exception ex) {
			log.error("验证失败了", ex);
		}
		return new AuthenticationResult(false, null);
	}

	@Override
	public boolean supports(Credentials credentials) {
		log.info("验证第三方有效性...");
		if (credentials instanceof AccessTokenCredentials)
			return true;
		return false;
	}
}