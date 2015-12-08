package com.easou.usercenter.service.impl;

import java.util.TreeMap;

import com.easou.common.api.ClientConfig;
import com.easou.common.api.EucHttpClient;
import com.easou.game.GameConfig;
import com.easou.pay.auth.AuthPayHelper;
import com.easou.usercenter.service.CurrencyService;

public class CurrencyServiceImpl implements CurrencyService {

	@Override
	public String getJSONCurrency(String userId) {
		StringBuffer urlBuffer = new StringBuffer(
				ClientConfig.getProperty("payment.url"));
		urlBuffer.append("/json/userEb.e");
		TreeMap<String, String> paraMap = new TreeMap<String, String>();
		paraMap.put("userId", userId);
		paraMap.put("partnerId", ClientConfig.getProperty("partnerId"));
		paraMap.put("sign", AuthPayHelper.getIntance().authSign(paraMap));
		String json = EucHttpClient.httpGet(urlBuffer.toString(), paraMap);
		return json;
	}

}
