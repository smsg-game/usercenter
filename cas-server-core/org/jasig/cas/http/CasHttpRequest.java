package org.jasig.cas.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class CasHttpRequest extends HttpServletRequestWrapper {
	private HttpSession casSession;

	public CasHttpRequest(HttpServletRequest request) {
		super(request);
		this.casSession = new CasHttpSession(request);
	}

	public HttpSession getSession() {
		return casSession;
	}

	public HttpSession getSession(boolean bl) {
		return this.getSession();
	}

}
