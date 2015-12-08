/**
 * WapServletRequestWrapper.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.easou.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.util.URLTools;
import com.easou.session.filter.MemSessionFilter;

/**
 * 2007-11-6,norbys,created it
 */
public class WapServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Log      LOG          = LogFactory.getLog(WapServletRequestWrapper.class);

    private String                esid         = "";

    private WapSession            session;

    private Map<String, String[]> parameterMap = new HashMap<String, String[]>();

    @SuppressWarnings("unchecked")
    public WapServletRequestWrapper(HttpServletRequest request) {
        super(request);
        String queryString = URLTools.urlDecode(request.getQueryString());

		if (queryString != null && !"".equals(queryString.trim())) {
			if (queryString.indexOf("&amp;") < 0
					|| queryString.indexOf("&") < 0)
				parameterMap = request.getParameterMap();
			else {// url中不带&amp;或&
				parameterMap = new HashMap<String, String[]>();
				parameterMap.putAll(request.getParameterMap());
				String[] queryArr = queryString.split("&amp;|&");
				for (int i = 0; i < queryArr.length; i++) {
					String[] valueArr = queryArr[i].split("=");
					if (valueArr.length == 2) {
						parameterMap.put(valueArr[0],
								new String[] { valueArr[1] });
					} else if (valueArr.length == 1) {
						parameterMap.put(valueArr[0], new String[] { "" });
					}
					LOG.info("parameterMap value: " + queryArr[i]);
				}
			}
		}

        this.esid = getParameter(MemSessionFilter.SESSION_ID_NAME);
        String cid = getParameter(MemSessionFilter.CID_NAME);
        if (null == cid || "".equals(cid)) {
            cid = getParameter(MemSessionFilter.CID_ALIAS_NAME);
        }

        if (esid == null && getAttribute(MemSessionFilter.SESSION_ID_NAME) != null) {
            esid = (String) super.getAttribute(MemSessionFilter.SESSION_ID_NAME);
        }

        SessionIdInfo sInfo = SessionId.explain(esid);
        if (!sInfo.isValid() || (System.currentTimeMillis() - sInfo.getCreateDate()) > SessionService.getInstance().getSessionTimeout()) {
            esid = SessionId.createNewId(cid, esid);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("esid=" + esid);
        }
        setAttribute(MemSessionFilter.SESSION_ID_NAME, esid);
        if (LOG.isInfoEnabled()) {
            LOG.info("esid:" + esid);
        }
        try {
            if (null == session) {
                session = new WapSession(esid);
            }
        } catch (Exception e) {
            LOG.error("******init wap session is error!\n", e);
            session = null;
        }
    }

    public String getParameter(String name) {
        if (parameterMap != null) {
            String values[] = (String[]) parameterMap.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }
        return null;
    }
    
//    public WapSession getWapSession() {
//        return session;
//    }
    
    public HttpSession getSession() {
        return session;
    }
}
