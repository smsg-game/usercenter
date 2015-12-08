package com.easou.cas.validation;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ECas20ProxyReceivingTicketValidationFilter extends
		AbstractTicketValidationFilter {
	private static final Log log = LogFactory
			.getLog(ECas20ProxyReceivingTicketValidationFilter.class);

	protected void initInternal(final FilterConfig filterConfig)
			throws ServletException {
		super.initInternal(filterConfig);
	}

	protected void onSuccessfulValidation(final HttpServletRequest request,
			final HttpServletResponse response, final EAssertion assertion) {
		try {
			request.getSession().setAttribute(CONST_CAS_ASSERTION, assertion);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
