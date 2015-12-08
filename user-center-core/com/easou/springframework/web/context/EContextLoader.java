package com.easou.springframework.web.context;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.easou.usercenter.config.SSOConfig;
/**
 * spring 加载配置类
 * 
 * @author damon
 * @since 2012.05.13
 *
 */
public class EContextLoader extends ContextLoader {
	
	private static String SERVER_TYPE = "server.type";

    protected WebApplicationContext createWebApplicationContext(
            ServletContext servletContext, ApplicationContext parent)
            throws BeansException {
       
//        return super.createWebApplicationContext(servletContext, parent);
        Class contextClass = determineContextClass(servletContext);
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
            throw new ApplicationContextException("Custom context class [" + contextClass.getName() +
                    "] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
        }

        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        wac.setParent(parent);
        wac.setServletContext(servletContext);
        String configLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        String serverType=SSOConfig.getProperty(SERVER_TYPE);
       
        if (configLocation != null) {
            String[] xml=StringUtils.tokenizeToStringArray(configLocation,ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
            if(serverType!=null){
            	String[] new_xml=StringUtils.addStringToArray(xml, 
            			"/WEB-INF/spring-configuration-source/applicationContext-" +
            			""+serverType+"-dynamic.xml");
                wac.setConfigLocations(new_xml);
            }
            /*if(serverType!=null 
            		&& Server.getServerByType(serverType)==Server.DIANXIN){//判断是否是电信服务器
                String[] new_xml=StringUtils.addStringToArray(xml, "/WEB-INF/spring-configuration-source/applicationContext-dx-dynamic.xml");
                wac.setConfigLocations(new_xml);
            }else{
            	String[] new_xml=StringUtils.addStringToArray(xml, "/WEB-INF/spring-configuration-source/applicationContext-default-dynamic.xml");
                wac.setConfigLocations(xml);
            }*/
           
        }

        wac.refresh();
        return wac;
    }

}