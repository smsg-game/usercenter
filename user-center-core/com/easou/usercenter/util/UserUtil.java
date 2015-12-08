package com.easou.usercenter.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class UserUtil {
	
	private static final Logger log = Logger.getLogger(UserUtil.class);
	
	/**
	 * 城市列表
	 */
	private static HashSet<String> citys;


	public static boolean checkCity(String city) {

		if (null != city && !("".equals(city.trim()))) {
			if (null == citys) {
				initCitys();
			}
			if (citys.contains(city)) {
				return true;
			}
			if (citys.contains(city + "省")) {
				return true;
			}
			if (citys.contains(city + "市")) {
				return true;
			}
			if (citys.contains(city + "县")) {
				return true;
			}
		}else{
			return true;
		}

		return false;
	}

	private static void initCitys() {
		citys = new HashSet<String>();
		BufferedInputStream in = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(UserUtil.class.getResource(
					"/citys.txt").getPath());
			in = new BufferedInputStream(fileIn);
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			String start = "";
			start = read.readLine();
			do {
				start = read.readLine();
				if (start != null && !"".equals(start.trim())) {
					citys.add(start);
				}
			} while (start != null);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("load citys.txt Exception: " + ex);
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
		log.info("load citys.txt success.");
	}

	public static void main(String[] args) {
		System.out.println(UserUtil.checkCity("深圳"));
		System.out.println(UserUtil.checkCity("深圳省"));
		System.out.println(UserUtil.checkCity("深圳市"));
		System.out.println(UserUtil.checkCity("深圳县"));
	}
}
