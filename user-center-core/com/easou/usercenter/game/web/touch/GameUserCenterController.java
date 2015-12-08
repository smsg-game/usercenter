package com.easou.usercenter.game.web.touch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.user.ArrestValidation;

/**
 * 游戏个人中心
 * 
 * @author damon
 * @since 2013.02.17
 * @version 1.0
 */
@Controller
public class GameUserCenterController extends GameBaseController {
	
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping("/game/touch/userCenter")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		if (null == user) {
			// 该id用户不存在
			removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作!");
			return "result";
		}
		
		model.addAttribute("eaId",user.getId());
		model.addAttribute("mobile",secertString(user.getMobile()));
		model.addAttribute("email",secertString(user.getEmail()));
		model.addAttribute("nickName", user.getNickName());
		try {
			model.addAttribute("backUrl",
					URLEncoder.encode(SSOConfig.getProperty("domain.readonly")+"/game/touch/userCenter"+getQueryString(request),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "game/touch/ui/userCenter";
	}
	
	private String secertString(String mobile) {
		try {
		if(null!=mobile && mobile.trim().length()>2) {
			StringBuffer sb=new StringBuffer();
			String _mobile = mobile.trim();
			int length = _mobile.length();
			sb.append(_mobile.substring(0, 2)).append("******").append(_mobile.substring(length-2));
			return sb.toString();
		} else {
			return mobile;
		}
		} catch(Exception e) {
			log.error(e,e);
			return null;
		}
	}

}
