package com.easou.usercenter.game.web.touch;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.common.util.MobileUtil;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.user.ArrestValidation;

/**
 * 游戏分享
 * 
 * @author damon
 * @since 2013.02.17
 * @version 1.0
 */
@Controller
public class GameShareController extends GameBaseController {
	
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	
	@RequestMapping("/game/touch/gameShare")
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
		model.addAttribute("eaId",user.getId());
		model.addAttribute("mobile",MobileUtil.secertString(user.getMobile()));
		model.addAttribute("email",MobileUtil.secertString(user.getEmail()));
		model.addAttribute("nickName", user.getNickName());
		return "game/touch/ui/gameShare";
	}
}