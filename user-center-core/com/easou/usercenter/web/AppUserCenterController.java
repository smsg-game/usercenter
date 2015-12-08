package com.easou.usercenter.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;

/**
 * 本控制器是提供给大厅客户端进行入个人中心的中转页，以让webview取得http header里的App-Agent
 * @author jay
 *
 */
@Controller
public class AppUserCenterController extends BaseController {

	@RequestMapping("/appUserCenter")
	public String show(ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) throws UnsupportedEncodingException {
		String qryString = getQueryString(request);
		String qn = ConditionUtil.getQn(request);
		String source = ConditionUtil.getChannel(request);
		String appId = null==request.getParameter(Constant.APPID_TAG)?"":request.getParameter(Constant.APPID_TAG);
		try {
			RequestInfo info = fetchRequestInfo(request);
			info.setQn(qn);
			info.setAppId(appId);
			EucApiResult<EucAuthResult> result = EucApiAuthCall.checkLogin(request, response, info);
			if(CodeConstant.OK.equals(result.getResultCode())) {
				return "redirect:/game/touch/userCenter" + qryString;
			} else {
				return "redirect:/quickLogin?service=" + URLEncoder.encode(SSOConfig.getProperty("domain.readonly") + "/game/touch/userCenter" + qryString, "utf-8") + "&qn=" + qn + "&source=" + source;
			}
		} catch (EucParserException e1) {
			return "redirect:/quickLogin?service=" + URLEncoder.encode(SSOConfig.getProperty("domain.readonly") + "/game/touch/UserCenter" + qryString, "utf-8") + "&qn=" + qn + "&source=" + source;
		}
	}
}