package com.easou.usercenter.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 实例化SPRING容器
 * 
 * @since 2012.07.04
 * @author damon
 * @version 1.0
 */
public class SpringContext implements ApplicationContextAware{
	
//	@Autowired(required=true)
	protected static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

	public static ApplicationContext getContext() {
		return applicationContext;
	}
}
