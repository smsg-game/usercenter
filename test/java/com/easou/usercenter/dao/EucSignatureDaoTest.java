package com.easou.usercenter.dao;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easou.usercenter.entity.EucSignature;

public class EucSignatureDaoTest extends TestCase {
    private ClassPathXmlApplicationContext context = null;
    private EucSignatureDao dao;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] {
						"file:webapp/WEB-INF/spring-configuration/*.xml",
						"file:webapp/WEB-INF/unused-spring-configuration/applicationContext-default-*.xml" });
		dao = context.getBean(EucSignatureDao.class);
	}

    protected void tearDown() throws Exception {
        context.close();
    }
    
    @Test
    public void testQueryById() {
    	EucSignature signature = dao.queryById(1234l);
    	System.out.println("============>" + signature.getKey());
    	Assert.assertNotNull(signature);
    }
    
    @Test
    public void testQueryByKey() {
    	EucSignature signature = dao.queryByKey("abcd");
    	System.out.println("============>" + signature.getId());
    	Assert.assertNotNull(signature);
    }
    
//    @Test
//    public void testInsert() {
//    	EucSignature signature = new EucSignature();
//    	signature.setId(1235l);
//    	signature.setKey("abce");
//    	signature.setExpire(new Date());
//    	boolean result = dao.insertSignature(signature);
//    	Assert.assertTrue(result);
//    }
//    
//    @Test
//    public void testUpdate() {
//    	EucSignature signature = new EucSignature();
//    	signature.setId(1235l);
//    	signature.setKey("abce1");
//    	signature.setExpire(new Date());
//    	boolean result = dao.updateSignature(signature);
//    	Assert.assertTrue(result);
//    }
    
//    @Test
//    public void testDelete() {
//    	boolean result = dao.deleteSignature(1235l);
//    	Assert.assertTrue(result);
//    }
}
