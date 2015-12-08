package com.easou.usercenter.config;

import com.easou.common.config.Config;
import com.easou.common.config.impl.PropertiesConfig;

public class SSOConfig {

	static {
		init();
	}

	// private static Logger log = Logger.getLogger(KeyConstant.class);

	private static Config config;

	private synchronized static void init() {
		config = new PropertiesConfig(SSOConfig.class
				.getResourceAsStream("/ssoconfig.properties"));
		// log.info("Secert key init finished!");
	}

	public static String getSecertKey() {
		return config.getStr("secert_key");
	}

	public static String getSulServer() {
		return config.getStr("surl_server");
	}
	
	public static String getProperty(String name) {
		return config.getStr(name);
	}
	
	public static String getProperty(String name, String defaultValue) {
		String value=config.getStr(name);
		if(null==value)
			return defaultValue;
		else
			return value;
	}

	public static void main(String[] args) {
		System.out.println(getSecertKey());
	}
}