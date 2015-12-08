package com.easou.usercenter.service;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easou.usercenter.entity.EucUser;

public class EucUserServiceTest extends TestCase {
	private ClassPathXmlApplicationContext context = null;
	private EucUserService service;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-jdbc.xml",
				"applicationContext-dao.xml",
				"applicationContext-memcached.xml",
				"applicationContext-default-*.xml" });
		service = context.getBean(EucUserService.class);
	}

	protected void tearDown() throws Exception {
		context.close();
	}

	public void testGetUserById() {
		EucUser user=service.queryUserInfoById("18656094");
		System.out.println(user.getName());
	}
	
//	@Test
//	public void testInsertUserByName() {
//		service.insertUserForUnameRegist("jay143", "123456");
//	}
	
//	public void testUpdatePassword() {
//		service.updatePasswd("18636714", "123");
//	}
	
//	public void testUpdateUserById() {
//		EucUser user=new EucUser();
//		user.setMobile("13424319753");
//		service.updateUserById("18636714", user);
//		System.out.println(user.getName());
//	}
}
