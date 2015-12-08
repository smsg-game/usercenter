package org.jasig.cas.web.flow;

import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
/**
 * 移除用户cookie信息
 * 
 * @author damon
 * @since 2012.04.26
 * @version 1.0
 *
 */
public class SSOCookieRemoveAction extends AbstractAction {
	
	private CookieRetrievingCookieGenerator uinfoCookieGenerator;

	public CookieRetrievingCookieGenerator getUinfoCookieGenerator() {
		return uinfoCookieGenerator;
	}

	public void setUinfoCookieGenerator(
			CookieRetrievingCookieGenerator uinfoCookieGenerator) {
		this.uinfoCookieGenerator = uinfoCookieGenerator;
	}

	@Override
	protected Event doExecute(final RequestContext context) throws Exception {
		//移除用户信息
		uinfoCookieGenerator.removeCookie(WebUtils.getHttpServletResponse(context));
		return success();
	}

}
