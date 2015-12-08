package com.easou.common.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanProperty {

	private static final Logger log = LoggerFactory
			.getLogger(BeanProperty.class);

	/**
	 * 将bean的内容转移到map
	 * @param bean
	 * @param map
	 */
	public static void copyToMap(Object bean, Map<String, Object> map) {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < fieldName.length(); j++) {
				char c = fieldName.charAt(j);
				if(j==0) {
					// 如果第一个是字母，让它大写
					sb.append((c>='a' && c<='z')?(char)(c-32) : c);
				} else {
					sb.append(c);
				}
			}
			try {
				Object obj = bean.getClass().getMethod("get" + sb.toString())
						.invoke(bean);
				if (null != obj) {
					map.put(fieldName, obj);
				}
			} catch (Exception e) {
				log.error(fieldName + " copy error! " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Character.isLowerCase('C'));
	}
}
