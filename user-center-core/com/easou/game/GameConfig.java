package com.easou.game;

import com.easou.common.config.Config;
import com.easou.common.config.impl.PropertiesConfig;

public class GameConfig {
	

	static {
		init();
	}

	private static Config config;

	private synchronized static void init() {
		config = new PropertiesConfig(GameConfig.class
				.getResourceAsStream("/gameconfig.properties"));
	}
	
	public static String getProperty(String name) {
		return config.getStr(name);
	}

	public static void main(String[] args) {
		System.out.println(getProperty("game.url") );
	}

}
