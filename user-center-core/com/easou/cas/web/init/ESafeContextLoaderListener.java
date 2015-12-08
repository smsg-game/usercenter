package com.easou.cas.web.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.easou.springframework.web.context.EContextLoaderListener;
/**
 * spring 加载配置监听类
 * 
 * @author damon
 * @since 2012.05.13
 *
 */
public class ESafeContextLoaderListener implements ServletContextListener {

    /** Instance of Commons Logging. */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * The name of the ServletContext attribute whereat we will place a List of
     * Throwables that we caught from our delegate context listeners.
     */
    public static final String CAUGHT_THROWABLE_KEY = "exceptionCaughtByListener";

    /** The actual ContextLoaderListener to which we will delegate to. */
    private final EContextLoaderListener delegate = new EContextLoaderListener();

    public void contextInitialized(final ServletContextEvent sce) {
        try {
            this.delegate.contextInitialized(sce);
        } catch (Throwable t) {
            /*
             * no matter what went wrong, our role is to capture this error and
             * prevent it from blocking initialization of the context. logging
             * overkill so that our deployer will find a record of this problem
             * even if unfamiliar with Commons Logging and properly configuring
             * it.
             */

            final String message = "SafeContextLoaderListener: \n"
                + "The Spring ContextLoaderListener we wrap threw on contextInitialized.\n"
                + "But for our having caught this error, the web application context would not have initialized.";

            // log it via Commons Logging
            log.error(message, t);

            // log it to System.err
            System.err.println(message);
            t.printStackTrace();

            // log it to the ServletContext
            ServletContext context = sce.getServletContext();
            context.log(message, t);

            /*
             * record the error so that the application has access to later
             * display a proper error message based on the exception.
             */
            context.setAttribute(CAUGHT_THROWABLE_KEY, t);
        }
    }

    public void contextDestroyed(final ServletContextEvent sce) {
        this.delegate.contextDestroyed(sce);
    }

}