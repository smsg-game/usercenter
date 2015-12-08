package com.easou.usercenter.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easou.usercenter.entity.EucUser;

public class EucUserDaoTest extends TestCase {
    private ClassPathXmlApplicationContext context = null;
    private EucUserDao dao;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] {
						"file:webapp/WEB-INF/spring-configuration/*.xml",
						"file:webapp/WEB-INF/unused-spring-configuration/applicationContext-default-*.xml" });
		dao = context.getBean(EucUserDao.class);
	}

    protected void tearDown() throws Exception {
        context.close();
    }

    @Test
    public void testFindUser() {
    	EucUser user=dao.queryUserInfoById("18656094");
    	System.out.println(user.getId() + " " + user.getName() + " " + user.getNickName() + " " + user.getPasswd());
    	Assert.assertTrue(null!=user);
    }
    
//    @Test
//    public void testGetOccupation() {
//    	List<Map> list=dao.getOccupation();
//    	for (Map map : list) {
//			System.out.println(map.get("ID") + " " + map.get("NAME"));
//		}
//    	Assert.assertTrue(list.size()>0);
//    }
//    
//    @Test
//    public void testFindUserByEmail() {
//    	EucUser user=dao.queryUserInfoByEmail("momo_liang@163.com");
//    	System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail());
//    	Assert.assertTrue(null!=user);
//    }
    
//    @Test
//    public void testInsertEucUser() {
//    	EucUser eucUser=new EucUser();
//    	eucUser.setName("jay_momo201231");
//    	eucUser.setPasswd(MD5Util.md5("111111").toLowerCase());
//    	eucUser.setNickName("梁志鹏31");
//    	eucUser.setStatus(1);
//    	eucUser.setSex(0);
//    	eucUser.setEmail("momo_liang@163.com");
//    	dao.insertUser(eucUser);
//    	System.out.println(eucUser.getId());
//    }
    
//    @Test
//    public void testUpdateEucUser() {
//    	HashMap paramter = new HashMap();
//    	paramter.put("passwd", MD5Util.md5("123").toLowerCase());
//    	dao.updateUserById("18656094", paramter);
//    }
    
//    @Test
//    public void testUpdateNullUser() {
//    	HashMap paramter = new HashMap();
//    	paramter.put("mobile", "");
//    	dao.updateNullById("18656094", paramter);
//    }
}
