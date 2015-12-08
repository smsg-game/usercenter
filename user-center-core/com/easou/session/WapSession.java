/**
 * WapSession.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.easou.session;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WapSession implements HttpSession {
	private static final Log LOG = LogFactory.getLog(WapSession.class);

    private String esid;
    private Map valueMap;
    private boolean changed = false;

    @SuppressWarnings("unchecked")
    public WapSession(String esid) throws Exception {
        super();
        this.esid = esid;
        valueMap = new HashMap();
        Map map = SessionService.getInstance().getSession(esid);
        if (map != null) {
            valueMap.putAll(map);
        }
    }
    
    public Object getAttribute(String key) {
        return valueMap.get(key);
    }

    /**
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void setAttribute(String key, Object value) {
        if ("isCache".equalsIgnoreCase(key) && null != value) {// fileter
        	saveSession();
        } else {
            changed = true;
            valueMap.put(key, value);
        }
    }

    public void saveSession() {
        if (changed) {
            boolean status = SessionService.getInstance().saveSession(esid,
                    valueMap);
            changed = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug("session save status:" + status);
            }
        }
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(valueMap.keySet());      
    }

    public long getCreationTime() {
        return 0;
    }

    public String getId() {
        return esid;
    }

    public long getLastAccessedTime() {
        return 0;
    }

    public int getMaxInactiveInterval() {
        return 0;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getValue(String arg0) {
        return valueMap.get(arg0);
    }

    public String[] getValueNames() { 
         Object[] objs = valueMap.keySet().toArray();
         String[] keys = new String[objs.length];
         for(int i=0; i<objs.length; i++){
             keys[i] = objs[i].toString();
         }
         return keys;
    }

    public void invalidate() {
        valueMap.clear();
        changed = true; 
    }

    public boolean isNew() {
        return false;
    }

    public void putValue(String arg0, Object arg1) {
        setAttribute(arg0,arg1);        
    }

    public void removeAttribute(String arg0) {
        valueMap.remove(arg0);      
        changed = true;
    }

    public void removeValue(String arg0) {
        removeAttribute(arg0);      
    }

    public void setMaxInactiveInterval(int arg0) {
    }
}
