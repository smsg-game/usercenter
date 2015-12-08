package com.easou.cas.login;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

/**
 * 外部用户登陆
 * 
 * @author river
 * 
 */
public class OAuthenticationAction {
	private CentralAuthenticationService centralAuthenticationService;

	public String auth(RequestContext context, MessageContext messageContext)
			throws Exception {
 
		
		return null;
	}

	public CentralAuthenticationService getCentralAuthenticationService() {
		return centralAuthenticationService;
	}

	public void setCentralAuthenticationService(
			CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}
}
