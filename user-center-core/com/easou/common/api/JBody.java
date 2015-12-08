package com.easou.common.api;

import com.alibaba.fastjson.JSONObject;

public class JBody extends JSONObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6605682619731152303L;

	public void putContent(String name,Object obj) {
		put(name, obj);
	}
}
