/**
 * SessionService.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.easou.session;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danga.MemCached.MemCachedClient;

/**
 * TODO
 * 
 * Revision History
 *
 * 2007-11-6,norbys,created it
 */
public class SessionService {
    private static final Log LOG = LogFactory.getLog(SessionService.class);
    
    // 保存session的memcached客户端
    private MemCachedClient client;

	public MemCachedClient getClient() {
		return client;
	}

	public void setClient(MemCachedClient client) {
		this.client = client;
	}

	private static SessionService instance = null;

    private int sessionTimeout = 1800000; //session失效时间

//    private SockIOPool pool = null;
//
//    private String poolName = "sesssock";

    public static synchronized SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    private SessionService() {
//        InputStream infile = SessionService.class
//                .getResourceAsStream("/sessionservice.properties");
//        Properties props = new Properties();
//        try {
//            props.load(infile);
//        } catch (IOException ex) {
//            LOG.error("load property file fail:" + ex.getMessage());
//        }
//        initSockIOPool(props);
//        sessionTimeout = PropertiesUtil.getIntProperty(props, "sessionTimeout",
//        		1800000);
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("sessionTimeout=" + sessionTimeout);
//        }
    }

//    public void initSockIOPool(Properties props) {
//        String serverlist = props.getProperty("serverlist", "127.0.0.1:11211");
//        poolName = props.getProperty("poolname", "sesssock");
//        String[] servers = serverlist.split(",");
//        pool = SockIOPool.getInstance(poolName);
//        pool.setServers(servers);
//        pool.setFailover(PropertiesUtil.getBooleanProperty(props, "failover",
//                true));
//        pool.setInitConn(PropertiesUtil.getIntProperty(props, "initconn", 10));
//        pool.setMinConn(PropertiesUtil.getIntProperty(props, "minconn", 5));
//        pool.setMaxConn(PropertiesUtil.getIntProperty(props, "maxconn", 250));
//        pool.setMaintSleep(PropertiesUtil.getIntProperty(props, "maintsleep",
//                30));
//        pool.setNagle(PropertiesUtil.getBooleanProperty(props, "nagle", false));
//        pool
//                .setSocketTO(PropertiesUtil.getIntProperty(props, "socketTO",
//                        3000));
//        pool.setAliveCheck(PropertiesUtil.getBooleanProperty(props,
//                "alivecheck", true));
//        pool.initialize();
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("servers=");
//            servers = pool.getServers();
//            for (int i = 0; i < servers.length; i++) {
//                LOG.debug(servers[i]);
//            }
//            LOG.debug("poolname=" + poolName);
//            LOG.debug("failover=" + pool.getFailover());
//            LOG.debug("initconn=" + pool.getInitConn());
//            LOG.debug("minconn=" + pool.getMinConn());
//            LOG.debug("maxconn=" + pool.getMaxConn());
//            LOG.debug("maintsleep=" + pool.getMaintSleep());
//            LOG.debug("nagle=" + pool.getNagle());
//            LOG.debug("socketTO=" + pool.getSocketTO());
//            LOG.debug("alivecheck=" + pool.getAliveCheck());
//        }
//    }

    public Map getSession(String id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(id);
        }
        return (Map) client.get(id);
    }

    public boolean saveSession(String id, Map session) {
        return saveSession(id, session, new Date(new Date().getTime()
                + sessionTimeout));
    }

    private boolean saveSession(String id, Map session, Date expiry) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("id=" + id);
            LOG.debug("session=" + session);
            LOG.debug("expiry=" + expiry);
        }
        return client.set(id, session, expiry);
    }

    public boolean removeSession(String id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(id);
        }
        return client.delete(id);
    }

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

//    protected void finalize() {
//        if (this.pool != null) {
//            this.pool.shutDown();
//        }
//    }
}
