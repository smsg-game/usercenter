package com.easou.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {

	/**
	 * 将数据对像转换成json数据
	 * 
	 * @param obj
	 *            数据对象
	 * @return
	 */
	public static String parserObjToJsonStr(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
	}

	/**
	 * 将json字符串转换成java对象
	 * 
	 * @param <T>
	 * @param str
	 *            json数据字符串
	 * @param cla
	 *            对象类型
	 * @return
	 */
	public static <T extends Object> T parserStrToObject(String str,
			Class<T> cla) {
		return JSON.parseObject(str, cla);
	}
	
	public static Object parserStrToObject(String str) {
		return JSON.parseObject(str);
	}

}
