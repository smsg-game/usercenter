package com.easou.usercenter.dao;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easou.usercenter.entity.EucPrivilege;

public class EucPrivilegeDaoTest extends TestCase {
    private ClassPathXmlApplicationContext context = null;
    private EucPrivilegeDao dao;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] {
						"file:webapp/WEB-INF/spring-configuration/*.xml",
						"file:webapp/WEB-INF/unused-spring-configuration/applicationContext-default-*.xml" });
		dao = context.getBean(EucPrivilegeDao.class);
	}

    protected void tearDown() throws Exception {
        context.close();
    }
    
    @Test
    public void testGetIpByName() {
    	List<EucPrivilege> list=dao.queryIpAddress();
    	for (EucPrivilege p : list) {
			System.out.println(p.getUsername() + " " + p.getIpAddress());
		}
    	Assert.assertTrue(list.size()>0);
    }
}
