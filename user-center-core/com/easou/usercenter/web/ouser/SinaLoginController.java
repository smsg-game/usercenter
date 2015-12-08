package com.easou.usercenter.web.ouser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.easou.common.constant.OUserConstant;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.service.impl.UrlTransService;
/**
 * 新浪登录控制器
 * 
 * @author damon
 * @since 2012.06.07
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/")
public class SinaLoginController extends BaseOuserController {
	
//	private static Logger log = Logger.getLogger(SinaLoginController.class);
	
	private Weibo weibo = new Weibo();
	private Users um=new Users();
	
	@Autowired
	private UrlTransService urlTransService;
	
	/**
	 * 新浪登录返回接受器
	 * 接受从新浪登录后返回的信息
	 * 对enService进行解密，解密后的参数名为service，并进行URLEnCoder转码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("ologin")
	public String  execute(ModelMap model,final HttpServletRequest request,
			final HttpServletResponse response) {
		Map paramMap = getRequestMap(request);
		String v = paramMap.get("uv")!=null?paramMap.get("uv").toString():null;
		if(!authorized(request, response, paramMap)){//非法第三方登录请求
	    	//model.addAttribute("redirUrl", "/login");
			//model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			//return "result";
			return getRedirectLoginUrl(v);
	    }
		
		String code = (String)paramMap.get("code");
		//String state = (String)paramMap.get("state");
		//删除对应参数
		if (null == code || "".equals(code)) {
			return getRedirectLoginUrl(v);
		}else{//删除对应参数
			paramMap.remove("code");
			paramMap.remove("state");
		}
	
		String enKey = request.getParameter(OUserConstant.ENCRYPT_SERVICE_TAG);
		if(!StringUtil.isEmpty(enKey)){
			paramMap.remove(OUserConstant.ENCRYPT_SERVICE_TAG);
		}
		Oauth oauth = new Oauth();
		// 获取accCode
		AccessToken accToken;
		try {
			accToken = oauth.getAccessTokenByCode(code);
			if (accToken == null) {
				throw new WeiboException("code验证失败: " + code);
			}
			String paramStr = urlTransService.getUrl(enKey);
			Map<String,Object> oParaMap = new HashMap<String,Object>();
			oParaMap.put("token", accToken);
			return createdBindUrl(accToken.getUid(), OUserConstant.SINA_TYPE, paramStr, request, response,oParaMap);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return getRedirectLoginUrl(v);
	}

	@Override
	protected String getNickName(Map<String,Object> oParaMap) {
		AccessToken accessToken = (AccessToken)oParaMap.get("token");
		weibo.setToken(accessToken.getAccessToken());
		try {
			User ur = um.showUserById(accessToken.getUid());
			return ur.getScreenName();
		} catch (WeiboException e) {
			log.error("获取新浪昵称失败", e);
		}
		return null;
	}

	@Override
	protected String getAccessToken(Object token) {
		return ((AccessToken)token).getAccessToken();
	}
		
}