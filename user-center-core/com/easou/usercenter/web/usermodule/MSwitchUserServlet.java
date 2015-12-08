package com.easou.usercenter.web.usermodule;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.auth.EucToken;
import com.easou.cas.validation.EAssertion;
import com.easou.common.util.CookieUtil;
import com.easou.usercenter.config.SSOConfig;

public class MSwitchUserServlet extends HttpServlet {

	private static final long serialVersionUID = -8408051841542755982L;

	public static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
	private static final String URL_USERMODULE_INDEX = "/user/usersetting.html";
	
	private static Log log = LogFactory.getLog(MSwitchUserServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/view/jsp/usermodule/user-switch-cfm.jsp").forward(request, response);
	}
	
	
}
