package com.easou.usercenter.cache.jedis.config.moudle;

import java.util.HashMap;
import java.util.Map;

public class Clus {
	private Map<String, Service> service = new HashMap<String, Service>();

	public Map<String, Service> getService() {
		return service;
	}

	public Service getServiceByName(String serviceName) {
		return service.get(serviceName);
	}

	public void addService(Service ser) {
 
		if (!"1".equals(ser.getType().trim())
				&& !"2".equals(ser.getType().trim())) {
			throw new RuntimeException("service 结点属性type值只能为1或2");
		}
		service.put(ser.getCluName(), ser);
	}

}
