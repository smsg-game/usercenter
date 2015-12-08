package com.easou.common.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JDesc extends JSONArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6170163626846069961L;

	public void addReason(String code, String desc) {
		add(new JReason(code, desc));
	}

	public JReason getReason(int index) {
		JSONObject jo = getJSONObject(index);
		return new JReason(jo.getString("c"), jo.getString("d"));
	}
}
