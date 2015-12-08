package com.easou.usercenter.game.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easou.cas.validation.EAssertion;
import com.easou.common.constant.CasConstant;
import com.easou.common.util.StringUtil;
import com.easou.game.GameConfig;
import com.easou.game.entity.Game;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.user.ArrestValidation;

public class GameBaseController extends BaseController {

	//private static HashMap<String, String> channels = new HashMap<String, String>();

	/** 回调参数名称 */
	protected final static String BACK_URL = "back";

	/** 回调频道编号 */
	protected final static String BACK_CHANNEL = "chn";
	/** 返回结果标识 */
	protected final static String EUC_RESULT = "eucResult";
	/** 成功返回值 */
	private static int SUCCESS_RESULT = 100;

	/** 个人中心uri，当没有回调参数时跳转至个人中心 */
	private final static String DEFAULT_BACK = "/game/touch/userCenter";

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
	
	protected String getBackUrl(final HttpServletRequest request,String backUrl) {
		return backUrl + getQueryString(request);
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
				}
				model.addAttribute("redirUrl", backUrl);
				String backChn = request.getParameter(BACK_CHANNEL);
				ArrestValidation av = new ArrestValidation(true);
				av.setUserId(ass.getUserId() + "");
				av.setBackChn(backChn);
//				av.setBackUrl(backUrl);
				return av;
			}
		}
	}
	
	/**
	 * 例子：http://localhost:8080/plat/start.e?gameId=1273&esid=55daHozOw3q&wver=c&qn=33&fr=7&l=7
	 * 
	 * @param game
	 * @param wver
	 * @param esid
	 * @return
	 */
	protected String buildGameUrl(Game game,String wver,String esid,String v){
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("game.url"));
		//urlBuffer.append(GameConfig.getProperty("game.hisgams.url"));
		urlBuffer.append("/start.e");
		if(StringUtil.isEmpty(wver)){//默认触版
			wver="t";
		}
		urlBuffer.append("?wver=").append(wver);
		urlBuffer.append("&version=").append(v);
		if(!StringUtil.isEmpty(esid)){
			urlBuffer.append("&esid=").append(esid);
		}
		urlBuffer.append("&gameId=").append(game.getId());
		urlBuffer.append("&onlineCount=").append(game.getOnlienCount());
		urlBuffer.append("&qn=33&fr=7&l=7");
		return urlBuffer.toString();
	}
}