package com.easou.usercenter.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EucPrivilegeServiceTest extends TestCase {
	private ClassPathXmlApplicationContext context = null;
    private EucPrivilegeService service;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] {
						"file:webapp/WEB-INF/spring-configuration/*.xml",
						"file:webapp/WEB-INF/unused-spring-configuration/applicationContext-default-*.xml" });
		service = context.getBean(EucPrivilegeService.class);
	}

    protected void tearDown() throws Exception {
        context.close();
    }

    @Test
	public void testCheckPrivilege() {
		System.out.println(service.isPrivilege("127.0.0.1"));
	}
}
