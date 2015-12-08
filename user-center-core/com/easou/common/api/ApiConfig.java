package com.easou.common.api;

import java.util.HashMap;
import java.util.Properties;

public class ApiConfig {

	private final static String SPLIT_SYM = ",";

	public final static String READ_PATH = "read";

	public final static String WRITE_PATH = "write";

	private static HashMap<String, String> paths = new HashMap<String, String>();

	public static void init(Properties pro) {
		String[] reads = new String[0];
		String[] writes = new String[0];
		if(null!=pro.getProperty("readPaths")) {
			reads = pro.getProperty("readPaths").split(SPLIT_SYM);
		}
		if(null!=pro.getProperty("writePaths")) {
			writes = pro.getProperty("writePaths").split(SPLIT_SYM);
		}

		for (int i = 0; i < reads.length; i++) {
			paths.put(reads[i], READ_PATH);
		}

		for (int i = 0; i < writes.length; i++) {
			paths.put(writes[i], WRITE_PATH);
		}
	}
	
	public static String getPathType(String path) {
		return paths.get(path);
	}
}