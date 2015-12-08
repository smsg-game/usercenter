package com.easou.usercenter.cache.jedis.config;

import org.apache.commons.digester.Digester;

import com.easou.usercenter.cache.jedis.config.moudle.Clus;
import com.easou.usercenter.cache.jedis.config.moudle.Service;

public class XmlConfig {
	private final static String xmlFileName = "cache-config.xml";

	public Clus parser() throws Exception {
		Digester digester = new Digester();
		digester.addObjectCreate("clus", Clus.class);
		digester.addCallMethod("clus/service", "addService", 1,
				new Class[] { Service.class });
		digester.addObjectCreate("clus/service", Service.class);
		digester.addSetProperties("clus/service", "cluName", "cluName");
		digester.addSetProperties("clus/service", "type", "type");
		digester.addBeanPropertySetter("clus/service/addr", "addr");
		digester.addBeanPropertySetter("clus/service/weith", "weith");
		digester.addBeanPropertySetter("clus/service/maxActive",
				"maxActive");
		digester.addBeanPropertySetter("clus/service/maxIdle", "maxIdle");
		digester.addBeanPropertySetter("clus/service/minIdle", "minIdle");
		digester.addBeanPropertySetter("clus/service/maxWait", "maxWait");
		digester.addBeanPropertySetter("clus/service/passwd", "passwd");
		digester.addCallParam("clus/service", 0, true);
		return (Clus) digester.parse(this.getClass().getClassLoader().getResourceAsStream(
				xmlFileName));
	}

	public static void main(String[] str) {
		XmlConfig config = new XmlConfig();
		try {
			Clus clu = (Clus) config.parser();
			System.out.println(clu.getService().size());
			
			 Service service=clu.getService().get("default");
			 System.out.println(service.getCluName());
			 System.out.println(service.getAddr());
			 System.out.println(service.getMaxActive());
			 System.out.println(service.getMaxIdle());
			 System.out.println(service.getMaxIdle());
			 System.out.println(service.getMinIdle());
			 System.out.println(service.getType());
			// System.out.println(service.getMod());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
