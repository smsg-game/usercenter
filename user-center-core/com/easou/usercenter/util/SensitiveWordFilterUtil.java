package com.easou.usercenter.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.util.key.HashKey;

/**
 * 敏感词过滤工具类
 * 
 * @author PC55
 * 
 */
public class SensitiveWordFilterUtil {
	private static Log log = LogFactory.getLog(SensitiveWordFilterUtil.class);

	private SensitiveWordFilterUtil() {
	}

	static {
		init();
	}

	public static void init() {

		HashKey hashKey = getHashKey();

		BufferedInputStream in = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(SensitiveWordFilterUtil.class.getResource(
					"/forbid.txt").getPath());
			in = new BufferedInputStream(fileIn);
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			String start = "";
			start = read.readLine();
			do {
				start = read.readLine();
				if (start != null && !"".equals(start)) {
					String[] wordsInfo = start.split(";|,");
					if (wordsInfo.length == 2) {
						hashKey.addKey(wordsInfo[0], "", wordsInfo[1]
								.toCharArray()[0]);
					} else {
						hashKey.addKey(start, "", 65);
					}
				}
			} while (start != null);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("load forbid.txt Exception: " + ex);
		} finally {
			if (fileIn != null)
				try {
					fileIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		log.info("load forbid.txt success.");
	}

	public static HashKey getHashKey() {
		return HashKey.getInstance();
	}
	
	public static void main(String[] args) {
		System.out.println(SensitiveWordFilterUtil.getHashKey().isExistKeyByContent("gggg","A"));
	}

}
//
//class ReloadSensitiveWord implements Runnable {
//	private static final Log LOG = LogFactory.getLog(ReloadSensitiveWord.class);
//
//	public void run() {
//		while (true) {
//			try {
//				Thread.sleep(36000000);// 10小时reload一次
//			} catch (InterruptedException e) {
//				LOG.error("###:" + e);
//			}
//			try {
//				SensitiveWordFilterUtil.init();
//			} catch (Exception e) {
//				LOG.error("###:" + e);
//			}
//		}
//	}
//}