package com.easou.usercenter.web.usermodule;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import com.easou.cas.validation.EAssertion;
import com.easou.common.constant.CasConstant;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.user.ArrestValidation;

public class MUserBaseController extends BaseController {

	private static HashMap<String, String> channels = new HashMap<String, String>();

	/** 回调参数名称 */
	protected final static String BACK_URL = "back";

	/** 回调频道编号 */
	protected final static String BACK_CHANNEL = "chn";
	/** 返回结果标识 */
	protected final static String EUC_RESULT = "eucResult";
	/** 成功返回值 */
	protected final static int SUCCESS_RESULT = 100;

	/** 个人中心uri，当没有回调参数时跳转至个人中心 */
	private final static String DEFAULT_BACK = "usersetting.html";
	
	private static String RESULT_PARA = EUC_RESULT + "=" + SUCCESS_RESULT;

	static {
		channels.put("2", "梵町图片");
		channels.put("3", "梵町小说");
		channels.put("7", "梵町新闻");
		channels.put("10", "梵町音乐");
		channels.put("25", "梵町悦听");
		channels.put("31", "梵町天气");
		channels.put("33", "梵町游戏");
		channels.put("34", "梵町游戏论坛");
	}

	protected static String getChannelName(String chnId) {
		return channels.get(chnId);
	}

	/**
	 * 从会话的assertion中取出用户id
	 * 
	 * @param request
	 * @return
	 */
	protected EAssertion getLoginArrest(final HttpServletRequest request) {
		return (EAssertion) request.getSession().getAttribute(
				CasConstant.CONST_CAS_ASSERTION);
	}

	/**
	 * 会话中无用户id，删除属性
	 * 
	 * @param request
	 */
	protected void removeLoginAssert(final HttpServletRequest request) {
		request.getSession().removeAttribute(CasConstant.CONST_CAS_ASSERTION);
	}

	protected String getDefaultBackUrl(final HttpServletRequest request) {
		return DEFAULT_BACK + getQueryString(request);
	}

	protected ArrestValidation checkLogin(final HttpServletRequest request, ModelMap model) {
		EAssertion ass = getLoginArrest(request);
		if (log.isDebugEnabled()) {
			log.debug("Assertion = " + ass);
		}
		if (null == ass) {
			return new ArrestValidation(false);
		} else {
			if (null == ass.getUserId()) {
				removeLoginAssert(request);
				return new ArrestValidation(false);
			} else {
				// 回调地址
				String backUrl = request.getParameter(BACK_URL);
				if (null == backUrl || "".equals(backUrl)) {
					backUrl = getDefaultBackUrl(request);
				} else {
					if(backUrl.indexOf("?")==-1) {
						backUrl+="?" + RESULT_PARA;
					} else {
						backUrl+="&" + RESULT_PARA;
					}
				}
				model.addAttribute("redirUrl", backUrl);
				String backChn = request.getParameter(BACK_CHANNEL);
				if (null != backChn) {
					String channelName = getChannelName(backChn);
					if (null != channelName) {
						model.addAttribute("backName", channelName);
					}
				}
				ArrestValidation av = new ArrestValidation(true);
				av.setUserId(ass.getUserId() + "");
				av.setBackChn(backChn);
				av.setBackUrl(backUrl);
				return av;
			}
		}
	}
}
