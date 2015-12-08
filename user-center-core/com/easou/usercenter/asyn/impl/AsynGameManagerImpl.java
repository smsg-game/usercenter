package com.easou.usercenter.asyn.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;

import com.easou.common.api.EucHttpClient;
import com.easou.common.util.StringUtil;
import com.easou.game.GameConfig;
import com.easou.pay.auth.AuthPayHelper;
import com.easou.usercenter.asyn.AsynGameManager;

public class AsynGameManagerImpl implements AsynGameManager{
	
	private static Log LOG = LogFactory.getLog(AsynGameManagerImpl.class);

	@Override
	@Async
	public void savePlayHistory(String gid, long userId) {
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("game.url"));
		urlBuffer.append("/plat/savePlayHistory.e");
		Map paraMap = new HashMap();
		paraMap.put("gid", gid);
		paraMap.put("pid", userId);
		String json = EucHttpClient.httpGet(urlBuffer.toString(), paraMap);
		LOG.info("save Play History result:"+json);
		
	}
	

	@Override
	@Async
	public void addEb(String userId, String mobile) {
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("payment.url"));
		urlBuffer.append("/json/addEb.e");
		TreeMap<String, String> paraMap = new TreeMap<String, String>();
		paraMap.put("userId", userId);
		paraMap.put("mobile", mobile);
		paraMap.put("sign", AuthPayHelper.getIntance().authSign(paraMap));
		String json = EucHttpClient.httpGet(urlBuffer.toString(), paraMap);
		LOG.info("add Eb result:"+json);
		
	}

	
	private String buildEbUrl(String userId,String mobile){
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("payment.url"));
		//urlBuffer.append(GameConfig.getProperty("game.hisgams.url"));
		urlBuffer.append("/json/addEb.e");
		urlBuffer.append("?userId=").append(userId);
		if(!StringUtil.isEmpty(mobile)){
			urlBuffer.append("&mobile=").append(mobile);
		}
		LOG.info("request addEb:"+urlBuffer.toString());
		return urlBuffer.toString();
	}
	/**
	 * 例子：http://localhost:8080/plat/gd.e?gameId=1273&esid=55daHozOw3q&wver=c&qn=33&fr=7&l=7
	 * 
	 * @param game
	 * @param wver
	 * @param esid
	 * @return
	 */
	private String buildGameUrl(String appId,String userId){
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("game.url"));
		//urlBuffer.append(GameConfig.getProperty("game.hisgams.url"));
		urlBuffer.append("/plat/savePlayHistory.e");
		if(StringUtil.isEmpty(appId)){//默认触版
			appId="0";
		}
		urlBuffer.append("?gid=").append(appId);
		if(!StringUtil.isEmpty(userId)){
			urlBuffer.append("&pid=").append(userId);
		}
		return urlBuffer.toString();
	}
}
