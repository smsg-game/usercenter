package com.easou.common.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.easou.common.config.Config;

public class PropertiesConfig implements Config {
	protected Logger logger = Logger.getLogger(this.getClass());
	Properties pro = null;

	public PropertiesConfig() {
	}

	public PropertiesConfig(String filePath) {
		init(filePath);
	}

	public PropertiesConfig(InputStream stream) {
		try {
			init(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("propertis 文件初始化失败", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void init(String filePath) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(filePath));
			init(stream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("propertis 文件初始化失败", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void init(InputStream stream) throws IOException {
		pro = new Properties();
		pro.load(new InputStreamReader(stream, "utf-8"));
	}

	@Override
	public Float getFloat(String name) {
		String valueStr = pro.getProperty(name);
		if (valueStr != null) {
			return Float.parseFloat(valueStr);
		}
		return null;
	}

	@Override
	public Integer getInt(String name) {
		// TODO Auto-generated method stub
		String valueStr = pro.getProperty(name);
		if (valueStr != null) {
			return Integer.parseInt(valueStr);
		}
		return null;
	}

	@Override
	public List<String> getList(String name) {
		// TODO Auto-generated method stub
		String valueStr = pro.getProperty(name);
		if (valueStr != null) {
			String[] strs = valueStr.split(";");
			return Arrays.asList(strs);
		}
		return null;
	}

	@Override
	public Long getLong(String name) {
		// TODO Auto-generated method stub
		String valueStr = pro.getProperty(name);
		if (valueStr != null) {
			return Long.parseLong(valueStr);
		}
		return null;
	}

	@Override
	public String getStr(String name) {
		// TODO Auto-generated method stub
		return pro.getProperty(name);
	}
	
	
	public static void main(String[] str) {
		PropertiesConfig config=new PropertiesConfig("F:\\svn\\space\\src\\etc\\conf\\classes\\smsMsg.properties");
		System.out.println(config.getStr("recdr"));
	}
}
