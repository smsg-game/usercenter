package com.easou.usercenter.web.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.common.constant.OUserConstant;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.service.OUserService;

@Controller
@RequestMapping("/user/")
public class BindUserController extends UserBaseController {

	@Resource(name = "oUserService")
	private OUserService oUserService;

	@RequestMapping("sbindUser")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		if(!av.isLogin()) {
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}else{
			model.addAttribute("back", av.getBackUrl());
		}
//		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
//		if (null == user) {
//			this.removeLoginAssert(request);
//			model.addAttribute("redirUrl", av.getBackUrl());
//			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
//			return "result";
//		}
		List<OUser> list = oUserService.queryOUserByEaId(av.getUserId());
		for(OUser oUser:list){
			if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.RENREN_TYPE).intValue()){
				model.addAttribute("renren", OUserConstant.RENREN_TYPE);
			}else if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.QQ_TYPE).intValue()){
				model.addAttribute("qq", OUserConstant.QQ_TYPE);
			}else if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.TQQ_TYPE).intValue()){
				model.addAttribute("tqq", OUserConstant.TQQ_TYPE);
			}else if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.SINA_TYPE).intValue()){
				model.addAttribute("sina", OUserConstant.SINA_TYPE);
			}
		}
		//model.addAttribute("ouList", list);
		return "default/ui/bindTrdUser";
	}
	
	@RequestMapping("cancelBind")
	public String cancelBind(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		if(!av.isLogin()) {
			//model.addAttribute("redirUrl", "/user/userCenter");
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		String netType = request.getParameter("type");
		
		if(null==netType) {
			//model.addAttribute("redirUrl", "/user/userCenter");
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "非法操作，系统将在3秒后返回...");
			return "result";
		}else{//移除参数
			model.remove("type");
			//model.remove("page");
		}
		oUserService.deleteUserByEaid(av.getUserId(), netType);
		List<OUser> list = oUserService.queryOUserByEaId(av.getUserId());
		for(OUser oUser:list){
			if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.RENREN_TYPE).intValue()){
				model.addAttribute("renren", OUserConstant.RENREN_TYPE);
			}else if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.QQ_TYPE).intValue()){
				model.addAttribute("qq", OUserConstant.QQ_TYPE);
			}else if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.TQQ_TYPE).intValue()){
				model.addAttribute("tqq", OUserConstant.TQQ_TYPE);
			}else if(oUser.getNet_id().intValue()==Integer.valueOf(OUserConstant.SINA_TYPE).intValue()){
				model.addAttribute("sina", OUserConstant.SINA_TYPE);
			}
		}
		//model.addAttribute("ouList", list);
		return "default/ui/bindTrdUser";
	}
}
