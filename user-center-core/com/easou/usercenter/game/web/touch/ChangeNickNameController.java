package com.easou.usercenter.game.web.touch;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.usercenter.EucResult;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.manager.EucUserManager;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.form.FormUserInfo;
import com.easou.usercenter.web.user.ArrestValidation;

@Controller
@RequestMapping("/game/touch/changeNName")
public class ChangeNickNameController extends GameBaseController {

	protected Logger log = Logger.getLogger(getClass());

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	@Resource(name = "eucUserManager")
	private EucUserManager eucUserManager;
	
	@Autowired
	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) {
		// String d = request.getParameter("d");
		ArrestValidation av = checkLogin(request, model);
		if (!av.isLogin()) {
			//model.addAttribute("redirUrl", av.getBackUrl());
			//model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			//return "result";
			return renderJSONFailure(response, "非法操作");
		}
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		// if(eucUser==null){//用户不存在
		// return "error";
		// }
		//FormUserInfo formUser = new FormUserInfo();
		//formUser.setNickName(eucUser.getNickName());
		// formUser.setBirthday(eucUser.getBirthday());
		//formUser.setSex(eucUser.getSex());
		//formUser.setCity(eucUser.getCity());
		model.addAttribute("nickName", eucUser.getNickName());
		//model.addAttribute("formUser", formUser);
		return "game/touch/ui/changeNickName";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formUser") FormUserInfo formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		validator.validate(formUser, result);
		String nickName = formUser.getNickName();
        EucUser updateUser = new EucUser();
        updateUser.setNickName(nickName);
        //更新结果
		EucResult<EucUser> updateResult = 
				eucUserManager.updateUserInfo(av.getUserId(), updateUser, true);
		//判断是否更新成功
		if(updateResult.getResultCode()==EucResult.SUCCESS_CODE){
			//model.addAttribute("redirUrl",getDefaultBackUrl(request));
			//model.addAttribute("retMessage", "修改成功，系统将在3 秒后返回");
			//return "result";
			return renderJSONSuccess(response, "修改成功");
		}else{//更新失败
			//result.reject("FormUser[nickName]", updateResult.getDescList().get(0).getDesc());
			return renderJSONFailure(response, updateResult.getDescList().get(0).getDesc());
			//return "game/touch/ui/changeNickName";
		}
	}

}
