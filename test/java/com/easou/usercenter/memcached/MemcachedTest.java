package com.easou.usercenter.memcached;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.danga.MemCached.MemCachedClient;

public class MemcachedTest extends TestCase {
    private ClassPathXmlApplicationContext context = null;
    private MemCachedClient client;

	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] {
						"file:webapp/WEB-INF/spring-configuration/*.xml",
						"file:webapp/WEB-INF/unused-spring-configuration/applicationContext-default-*.xml" });
		client = (MemCachedClient)context.getBean("ticketClient");
	}

    protected void tearDown() throws Exception {
        context.close();
    }
    
    @Test
    public void testMem() {
    	client.add("aa", "bb");
    	System.out.println(client.get("aa"));
    }
}
