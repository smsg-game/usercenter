package com.easou.usercenter.web;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easou.common.util.RandomKeyGenerator;
import com.easou.usercenter.entity.EucSignature;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucSignatureService;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.SendMailUtil;
import com.easou.usercenter.util.SignatureUtil;
import com.easou.usercenter.web.form.FormEmail;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/pass/findbyem")
public class ForgetEmailController extends BaseController {

	private final static int EXPIRE_DATE = 2;

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Autowired
	private EucSignatureService eucSignatureSerivce;

	@RequestMapping(method = RequestMethod.GET)
	public String show(ModelMap model) {
		model.addAttribute("formEmail", new FormEmail());
		return "default/ui/forgetEmail";
	}

	/**
	 * 邮箱找回密码
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String execute(@ModelAttribute("formEmail") FormEmail formEmail,
			BindingResult result, ModelMap model,
			final HttpServletRequest request, final HttpServletResponse response) {
		String email = formEmail.getEmail();
//		String channel = ConditionUtil.getChannel(request);
//		String esid = ConditionUtil.getEsid(request);
//		String uid = ConditionUtil.getUid(request);
		if (!FormUserValidator.checkEmail(email)) {
			log.info("email地址不正确");
			result.reject("notVaildEmail", "邮箱地址不正确");
			return "default/ui/forgetEmail";
		}
		EucUser eucUser = eucUserService.queryUserInfoByEmail(email);
		if (null == eucUser) {
			log.info("该邮箱地址还未注册");
			result.reject("noRegEmail", "该邮箱地址还未注册");
			return "default/ui/forgetEmail";
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, EXPIRE_DATE);
		Date expire = calendar.getTime();
		EucSignature signature = eucSignatureSerivce.queryById(eucUser.getId());
		String secert = "";
		if (null == signature) {
			// 不存在数字签名
			String key = RandomKeyGenerator.generate(10);
			eucSignatureSerivce.insertSignature(eucUser.getId(), key, expire);
			secert = SignatureUtil.encodeSecert(eucUser.getId(), key);
		} else {
			if (null != signature.getExpire() && new Date().before(signature.getExpire())) {
				// 未过期
				log.debug("用户的数字签名未过期");
				secert = SignatureUtil.encodeSecert(signature.getId(),
						signature.getKey());
			} else {
				// 已过期
				log.debug("用户的数字签名已过期，重新生成密文");
				String key = RandomKeyGenerator.generate(10);
				eucSignatureSerivce.updateSignature(eucUser.getId(), key, expire);
				secert = SignatureUtil.encodeSecert(eucUser.getId(), key);
			}
		}
		// 发送邮件
		Map paramMap = new HashMap();
		paramMap.put("s", secert);
		String wver = request.getParameter("wver");
		if(wver!=null&&!wver.equals("")){
			paramMap.put("wver", "t");
		}
		String url = this.getPath(request, "/pass/emresetpass", paramMap);
		String content = "<p>点击链接完成密码重设: <a href='" + url + "'>" + url
				+ "</a></p><p>此邮件不用回复，如果您意外收到该邮件，请忽略之。</p>";
		log.debug(content);
		SendMailUtil.send(email, "梵町找回密码", content);
		model.addAttribute("email", email);
		return "default/ui/forgetEmail2";
	}
}
