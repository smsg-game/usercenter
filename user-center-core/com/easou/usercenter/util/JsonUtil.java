package com.easou.usercenter.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
	
	private final static ObjectMapper MAPPER = new ObjectMapper();
	
	public static String parserObjToJsonStr(Object obj) {
		try {
			String jsonStr = MAPPER.writeValueAsString(obj);
			return jsonStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
