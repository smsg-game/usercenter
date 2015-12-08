package com.easou.usercenter.web.usermodule;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.cas.auth.EucAuthHelper;
import com.easou.cas.auth.EucSimpleAuthHelper;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.service.EucUserService;

@Controller
public class MSwitchUserController extends MUserBaseController {

//	@Resource(name = "eucUserServiceImpl")
//	private EucUserService eucUserService;
	
	@RequestMapping(value = "/user/switch", method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		return "usermodule/user-switch";
	}
	
	
	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	public String switchUser(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
//		EucAuthHelper authHelper = EucSimpleAuthHelper.getInstance();
		try {
			String loginUrl = SSOConfig.getProperty("domain.readonly") + "/mswitch";
			
//			authHelper.logout(response, loginUrl, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "usermodule/user-switch-cfm";
	}
	
//	@RequestMapping(value = "/mswitch", method = RequestMethod.GET)
//	public String login(ModelMap model, final HttpServletRequest request,
//			final HttpServletResponse response) {
//		return "usermodule/user-switch-cfm";
//	}
}
