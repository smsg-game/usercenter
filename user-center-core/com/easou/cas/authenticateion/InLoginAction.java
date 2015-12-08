package com.easou.cas.authenticateion;

import org.apache.log4j.Logger;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

public class InLoginAction {

	protected Logger log = Logger.getLogger(getClass());

	public final String init(final RequestContext context,
			final UsernamePasswordCredentials credentials,
			final MessageContext messageContext) {
		try {
			String username = context.getExternalContext().getRequestMap().getString("user");
			String password = context.getExternalContext().getRequestMap().getString("pass");
			if(null==username || null==password) {
				log.info("用户名或密码为空" + username + " " + password);
				messageContext.addMessage(new MessageBuilder().error().code("101")
						.defaultText("用户名密码为空").build());
			}
			credentials.setUsername(username);
			credentials.setPassword(password);
//			credentials.setCookieLogin(true);
			credentials.setIsCookie("true");
			log.info("用户名：" + username + " 密码：" + password);
			String loginTicket=context.getFlowScope().getString("loginTicket");
			context.getRequestScope().put("lt", loginTicket);
			return "success";
		} catch (Exception e) {
			log.error(e,e);
			populateErrorsInstance(e,messageContext);
			return "error";
		}
	}

	private void populateErrorsInstance(Exception e,
			final MessageContext messageContext) {
		try {
			messageContext.addMessage(new MessageBuilder().error().code("103")
					.defaultText(e.getMessage()).build());
		} catch (final Exception fe) {
			log.error(fe.getMessage(), fe);
		}
	}
}
