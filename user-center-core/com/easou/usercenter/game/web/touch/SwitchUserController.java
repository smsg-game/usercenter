package com.easou.usercenter.game.web.touch;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.cas.auth.EucAuth;
import com.easou.cas.auth.EucAuthManager;
import com.easou.common.api.EucParserException;
import com.easou.common.util.StringUtil;
import com.easou.game.GameUtil;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.user.ArrestValidation;

@Controller
public class SwitchUserController extends GameBaseController {
	
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping("/game/touch/switchUser")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		if (null == user) {
			// 该id用户不存在
			removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		EucAuthManager eucAuth = new EucAuthManager();
		EucAuth auth = new EucAuth();
		auth.setRenew(true);
		String service = request.getRequestURL().toString();
		service = service.replaceFirst(request.getRequestURI(), "/game/touch/userCenter");
		auth.setService(service + getQueryString(request));
		String authUrl = GameUtil.getAuthURL(request)+getQueryString(request);
		try {
			//重定向
			eucAuth.auth(request, response, auth, authUrl, null);
			return null;
		} catch (EucParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
