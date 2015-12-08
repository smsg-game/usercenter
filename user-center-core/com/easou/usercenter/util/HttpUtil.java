package com.easou.usercenter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class HttpUtil {
	
	private static Logger log = Logger.getLogger(HttpUtil.class);

	/**
	 * 通过request获取请求json串
	 * 
	 * @return
	 * @throws IOException
	 * @throws IOException
	 */
	public static String requestToJson(HttpServletRequest request) throws IOException {

		// 为兼容以前客户端包
		request.setCharacterEncoding("UTF8");
		InputStream is = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			is = request.getInputStream();
			if (null == is) {
				return "";
			}
			reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
				if (null != line) {
					sb.append("\n");
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (null != is)
				is.close();
			if (null != reader)
				reader.close();
		}
		// 通过post传递上来的JSON参数
		if (sb.indexOf("json=") == 0) {
			String str = URLDecoder.decode(sb.toString(), "UTF-8");
			log.info(str);
			return str.substring(5);
		}
		log.info(sb.toString());
		return sb.toString();
	}
}
