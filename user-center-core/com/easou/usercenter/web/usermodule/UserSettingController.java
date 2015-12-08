package com.easou.usercenter.web.usermodule;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.common.constant.OUserConstant;
import com.easou.common.util.MobileUtil;
import com.easou.usercenter.ErrorDesc;
import com.easou.usercenter.EucResult;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.manager.EucUserManager;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.OUserService;
import com.easou.usercenter.util.JsonUtil;
import com.easou.usercenter.web.form.FormUserInfo;
import com.easou.usercenter.web.user.ArrestValidation;

@Controller
public class UserSettingController extends MUserBaseController {
	
	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	@Resource(name = "eucUserManager")
	private EucUserManager eucUserManager;
	@Resource(name = "oUserService")
	private OUserService oUserService;
	
	@Autowired
	private Validator validator;

	@RequestMapping("/user/usersetting.html")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		if(av == null || !av.isLogin()) {
			return "usermodule/user-switch-cfm";
		}
		EucUser user = eucUserService.queryUserInfoById(av.getUserId());
		if (null == user) {
			// 该id用户不存在
			removeLoginAssert(request);
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "usermodule/user-switch-cfm";
		}
		String m = request.getParameter("m");
		if(m != null && !"".equals(m)) {
			model.addAttribute("m", m);
		}
		model.addAttribute("eaId",user.getId());
		model.addAttribute("mobile",MobileUtil.secertString(user.getMobile()));
		model.addAttribute("email",MobileUtil.secertString(user.getEmail()));
		model.addAttribute("nickName", user.getNickName());
		
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
		
		return "usermodule/index";
	}
	
	@RequestMapping("/user/changeNickName")
	@ResponseBody
	public Object changeNickName(@ModelAttribute("formUser") FormUserInfo formUser,
			BindingResult result, ModelMap model, 
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		ErrorDesc error = new ErrorDesc(0, "");
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		if (null == eucUser) {
			log.debug("用户不存在");
			error.setCode(1001);
			error.setDesc("用户不存在");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response,jsonResult);
		}
		String nickName = formUser.getNickName();
		model.addAttribute("name", eucUser.getName());

		validator.validate(formUser, result);
		if (result.hasErrors()) {
			log.info("验证失败");
			error.setCode(1002);
			error.setDesc("验证失败");
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}
		EucUser updUser = new EucUser();
		updUser.setNickName(nickName);
		//更新结果
		EucResult<EucUser> updateResult = 
				eucUserManager.updateUserInfo(av.getUserId(), updUser, true);
		//判断是否更新成功
		if(updateResult.getResultCode()==EucResult.SUCCESS_CODE){
			
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}else{//更新失败
			error.setCode(1003);
			error.setDesc(updateResult.getDescList().get(0).getDesc());
			String jsonResult = JsonUtil.parserObjToJsonStr(error);
			return renderJSONSuccess(response, jsonResult);
		}
	}
	
}
