package com.easou.springframework.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoader;
/**
 * spring 加载配置监听类
 * 
 * @author damon
 * @since 2012.05.13
 *
 */
public class EContextLoaderListener implements ServletContextListener {

    private ContextLoader contextLoader;


    /**
     * Initialize the root web application context.
     */
    public void contextInitialized(ServletContextEvent event) {
        this.contextLoader = createContextLoader();
        this.contextLoader.initWebApplicationContext(event.getServletContext());
    }

    /**
     * Create the ContextLoader to use. Can be overridden in subclasses.
     * @return the new ContextLoader
     */
    protected ContextLoader createContextLoader() {
        return new EContextLoader();
    }

    /**
     * Return the ContextLoader used by this listener.
     * @return the current ContextLoader
     */
    public ContextLoader getContextLoader() {
        return this.contextLoader;
    }


    /**
     * Close the root web application context.
     */
    public void contextDestroyed(ServletContextEvent event) {
        if (this.contextLoader != null) {
            this.contextLoader.closeWebApplicationContext(event.getServletContext());
        }
    }

}
