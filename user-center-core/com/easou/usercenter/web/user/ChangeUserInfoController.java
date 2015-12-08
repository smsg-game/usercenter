package com.easou.usercenter.web.user;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.form.FormUserInfo;
import com.easou.usercenter.web.validation.FormUserValidator;

/**
 * 用户信息
 * 
 * @author damon
 * 
 */
@Controller
@RequestMapping("/user/changeInfo")
public class ChangeUserInfoController extends UserBaseController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;
	@Autowired
	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse respons) {
		// String d = request.getParameter("d");
		ArrestValidation av = checkLogin(request, model);
		if (!av.isLogin()) {
			model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
		}
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		// if(eucUser==null){//用户不存在
		// return "error";
		// }
		FormUserInfo formUser = new FormUserInfo();
		formUser.setNickName(eucUser.getNickName());
//		formUser.setBirthday(eucUser.getBirthday());
		formUser.setSex(eucUser.getSex());
		formUser.setCity(eucUser.getCity());
		model.addAttribute("name", eucUser.getName());
		model.addAttribute("formUser", formUser);
		return "default/ui/changeInfo";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String execute(@ModelAttribute("formUser") FormUserInfo formUser,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		ArrestValidation av = checkLogin(request, model);
		EucUser eucUser = eucUserService.queryUserInfoById(av.getUserId());
		if (null == eucUser) {
			log.debug("用户不存在");
			//model.addAttribute("redirUrl", av.getBackUrl());
			model.addAttribute("redirUrl",getDefaultBackUrl(request));
			model.addAttribute("retMessage", "你要修改资料的用户不存在");
			return "result";
		}
		String nickName = formUser.getNickName();
		// Integer birthday = formUser.getBirthday();
		Integer sex = formUser.getSex();
		String city = formUser.getCity();
		model.addAttribute("name", eucUser.getName());

		validator.validate(formUser, result);
		if (result.hasErrors()) {
			log.info("验证失败");
			return "default/ui/changeInfo";
		}
		EucUser updUser = new EucUser();
		boolean error = false;
		String s = checkNickName(nickName, eucUser);
		if (!"".equals(s)) {
			result.reject("FormUser[nickName]", s);
			error = true;
		} else {
			updUser.setNickName(nickName);
		}
		if (null != city && !"".equals(city)) {
			// 城市可填空
			if (!FormUserValidator.checkCity(city)) {
				result.reject("FormUser[birthday]", "城市名称不正确");
				error = true;
			} else {
				updUser.setCity(city);
			}
		}
		if (!FormUserValidator.checkSex(sex)) {// 验证性别
			result.reject("FormUser[sex]", "性别有误");
			error = true;
		} else {
			updUser.setSex(sex);
		}
		if (error) {
			log.info("个人资料验证失败");
			return "default/ui/changeInfo";
		}

		// 更新数据库
		eucUserService.updateUserById(av.getUserId(), updUser);
		/*String backParam = request.getParameter(BACK_URL);
		String backUrl;
		if(null!=backParam && !"".equals(backParam)) {
			Map map = new HashMap();
			map.put(EUC_RESULT, 100);
			backUrl = buildURL(backParam, map);
		} else  {
			backUrl = av.getBackUrl();
		}*/
		model.addAttribute("redirUrl",getDefaultBackUrl(request));
		//model.addAttribute("redirUrl", backUrl);
		model.addAttribute("retMessage", "修改成功，系统将在3 秒后返回");
		return "result";
	}

	/**
	 * 检测匿称
	 * 
	 * @param nickName
	 * @param userId
	 * @return
	 */
	private String checkNickName(String nickName, EucUser eucUser) {
		if (nickName != null && nickName.equals(eucUser.getNickName())) {// 昵称没做修改
			return "";
		}
		String s = FormUserValidator.checkNickName(nickName);
		if (!"".equals(s)) {
			return s;
		} else {
			EucUser temp = eucUserService.queryUserInfoByNickName(nickName);
			if (null != temp && !temp.getId().equals(eucUser.getId())) {
				// 昵称相同并且不为当前用户ID的用户
				return "该昵称已被注册";
			}
			return "";
		}
	}

}
