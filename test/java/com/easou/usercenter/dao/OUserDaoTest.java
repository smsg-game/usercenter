package com.easou.usercenter.dao;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easou.usercenter.entity.OUser;

public class OUserDaoTest extends TestCase {
	private ClassPathXmlApplicationContext context = null;
	private OuserDao dao;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] {
						"file:webapp/WEB-INF/spring-configuration/*.xml",
						"file:webapp/WEB-INF/unused-spring-configuration/applicationContext-default-*.xml" });
		dao = context.getBean(OuserDao.class);
	}

	protected void tearDown() throws Exception {
		context.close();
	}

	@Test
	public void testOpenId() {
		OUser ouser = dao.queryOUserByExtIdAndOType("testOpenId", "2");
		System.out.println(ouser.getId() + " " + ouser.getEa_id());
		Assert.assertTrue(null != ouser);
	}
	
	@Test
	public void testQueryEaId() {
		OUser ouser = dao.queryOUserByEaIdAndOType("22000042", "2");
		System.out.println(ouser.getId() + " " + ouser.getEa_id());
		Assert.assertTrue(null != ouser);
	}
	
//	@Test
//	public void testDeleteBinding() {
//		dao.deleteOuserInfo("22000041", "2");
//	}
	
	@Test
	public void testQueryOuserByEaid() {
		List<OUser> list=dao.queryOUserByEaId("18656094");
		System.out.println(list);
		if(list.size()>0) {
			for (OUser oUser : list) {
				System.out.println(oUser.getThird_id() + " " + oUser.getNet_id());
			}
		}
	}
}
