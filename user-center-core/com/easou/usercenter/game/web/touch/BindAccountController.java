package com.easou.usercenter.game.web.touch;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.common.constant.OUserConstant;
import com.easou.common.util.MobileUtil;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.OUserService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.user.ArrestValidation;

/**
 * 绑定帐号
 * 
 * @author damon
 * @since 2013.02.17
 * @version 1.0
 */
@Controller
public class BindAccountController extends GameBaseController {

	@Resource(name = "oUserService")
	private OUserService oUserService;

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping("/game/touch/bindAccount")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		if (!av.isLogin()) {
			// model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl", getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作");
			return "result";
		} else {
			EucUser user = eucUserService.queryUserInfoById(av.getUserId());
			model.addAttribute("back", av.getBackUrl());
			model.addAttribute("mobile",
					MobileUtil.secertString(user.getMobile()));
		}
		// EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		// if (null == user) {
		// this.removeLoginAssert(request);
		// model.addAttribute("redirUrl", av.getBackUrl());
		// model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
		// return "result";
		// }
		List<OUser> list = oUserService.queryOUserByEaId(av.getUserId());
		if (list != null && list.size() > 0) {
			for (OUser oUser : list) {
				if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.RENREN_TYPE).intValue()) {
					model.addAttribute("renren", oUser);
				} else if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.QQ_TYPE).intValue()) {
					model.addAttribute("qq", oUser);
				} else if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.TQQ_TYPE).intValue()) {
					model.addAttribute("tqq", oUser);
				} else if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.SINA_TYPE).intValue()) {
					model.addAttribute("sina", oUser);
				}
			}
		}
		// model.addAttribute("ouList", list);
		return "game/touch/ui/bindAccount";
	}

	@RequestMapping("/game/touch/cancelBind")
	public String cancelBind(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		if (!av.isLogin()) {
			// model.addAttribute("redirUrl", "/user/userCenter");
			model.addAttribute("redirUrl", getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作");
			return "result";
		}else {
			EucUser user = eucUserService.queryUserInfoById(av.getUserId());
			model.addAttribute("back", av.getBackUrl());
			model.addAttribute("mobile",
					MobileUtil.secertString(user.getMobile()));
		}
		String netType = request.getParameter("type");

		if (null == netType) {
			// model.addAttribute("redirUrl", "/user/userCenter");
			model.addAttribute("redirUrl", getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作");
			return "result";
		} else {// 移除参数
			model.remove("type");
			// model.remove("page");
		}
		oUserService.deleteUserByEaid(av.getUserId(), netType);
		List<OUser> list = oUserService.queryOUserByEaId(av.getUserId());
		if (list != null && list.size() > 0) {
			for (OUser oUser : list) {
				if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.RENREN_TYPE).intValue()) {
					model.addAttribute("renren", oUser);
				} else if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.QQ_TYPE).intValue()) {
					model.addAttribute("qq", oUser);
				} else if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.TQQ_TYPE).intValue()) {
					model.addAttribute("tqq", oUser);
				} else if (oUser.getNet_id().intValue() == Integer.valueOf(
						OUserConstant.SINA_TYPE).intValue()) {
					model.addAttribute("sina", oUser);
				}
			}
		}
		// model.addAttribute("ouList", list);
		return "game/touch/ui/bindAccount";
	}

}
