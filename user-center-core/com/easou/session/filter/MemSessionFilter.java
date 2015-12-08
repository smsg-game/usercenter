/**
 * MemSessionFilter.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.easou.session.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easou.session.WapServletRequestWrapper;
import com.easou.session.WapSession;

/**
 * Session
 * Revision History
 * 2007-11-6,norbys,created it
 */
@SuppressWarnings("serial")
public class MemSessionFilter extends HttpServlet implements Filter {
//    private static final Log   LOG                     = LogFactory.getLog(MemSessionFilter.class);

    public static final String SESSION_ID_NAME         = "esid";

    public static final String CID_NAME                = "cid";

    public static final String CID_ALIAS_NAME          = "did";

//    public static final String SESSION_PROPERTIES_FILE = "/sessionservice.properties";

//    public static final String SESSION_TIME_OUT_NAME   = "sessionTimeout";

//    public static int          sessionTimeout          = 1800000;

    public void init(FilterConfig filterConfig) throws ServletException {
    	super.init();
//        InputStream in = MemSessionFilter.class.getResourceAsStream(SESSION_PROPERTIES_FILE);
//        Properties props = new Properties();
//        try {
//            props.load(in);
//            sessionTimeout = PropertiesUtil.getIntProperty(props, SESSION_TIME_OUT_NAME, sessionTimeout);
//        } catch (Exception ex) {
//            LOG.error("load property file fail:" + ex.getMessage());
//        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        WapServletRequestWrapper wapRequest = new WapServletRequestWrapper(request);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        WapSession session = (WapSession)wapRequest.getSession();
        // start write the uid in cookie
        Cookie cookies[] = request.getCookies();
        boolean addCooke = true;
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                if ("uid".equals(cookies[i].getName()) && null != cookies[i].getValue()
                        && cookies[i].getValue().length() > 10) {
                    addCooke = false;
                    break;
                }
            }
        }
        if (addCooke) {
            Cookie cookie = new Cookie("uid", (String) wapRequest.getAttribute(SESSION_ID_NAME));
            cookie.setMaxAge(86400 * 365 * 10);
            cookie.setPath("/");
            cookie.setDomain(".easou.com");
            response.addCookie(cookie);
        }
        // end uid

        // get the wap version c(cai ban) or s(simple ban)
        // String wapver = extractWapVersion(wapRequest, response);

        response.setHeader(SESSION_ID_NAME, (String) wapRequest.getAttribute(SESSION_ID_NAME)); // write head
        response.setHeader("mobile", "" + session.getAttribute("mobile"));
        response.setHeader("ua", "" + session.getAttribute("ua"));
        response.setHeader("cid", "" + session.getAttribute(CID_NAME));
        response.setHeader("did", "" + session.getAttribute(CID_ALIAS_NAME));
//        response.setHeader("mid", "" + session.getAttribute("mid"));
//        response.setHeader("wapVer", wapver);
        chain.doFilter(wapRequest, response);
        session.saveSession();
    }

//    /**
//     * �������ͷ��Accept��Ϣ�ж��ն�֧�ֵ�WAPЭ��
//     * 
//     * @param request
//     * @return
//     */
//    private String extractWapVersion(WapServletRequestWrapper request, HttpServletResponse response) {
//        // ������URL��ȡЭ��汾����ver
//        String wapVersion = (String) request.getParameter(Constants.WAP_VERSION_PARAMETER);
//        String wver = (String) request.getParameter("wver");
//        if (null != wver && ("c".equals(wver) || "t".equals(wver)))
//            wapVersion = Constants.WAP_VERSION_WAP2;
//        if (null != wver && "s".equals(wver))
//            wapVersion = Constants.WAP_VERSION_WAP1;
//        WapSession session = request.getWapSession();
//        // ���URL�е�Э��汾������Ч�����Session��ȡ
//        if (!validateWapVersion(wapVersion) && session != null) {
//            wapVersion = (String) session.getAttribute(Constants.SESSION_ATTRIBUTE_WAP_VERSION);
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("WapVersion from session : " + wapVersion);
//            }
//        }
//
//        // ��COOKIE�л�ȡ�汾����
//        Cookie cookies[] = request.getCookies();
//        String cookie_wap_version = null;
//        if (!validateWapVersion(wapVersion) && cookies != null) {
//            for (int i = 0; i < cookies.length; i++) {
//                if (Constants.SESSION_ATTRIBUTE_WAP_VERSION.equals(cookies[i].getName())
//                        && StringUtils.isNotEmpty(cookies[i].getValue())) {
//                    wapVersion = cookie_wap_version = cookies[i].getValue();
//                }
//            }
//        }
//
//        String accept = request.getHeader(Constants.REQUEST_HEAD_ACCEPT);
//        boolean wap1sup = false;
//        boolean wap2sup = false;
//        if (null != accept && (accept.indexOf("html") != -1 || accept.indexOf("*/*") != -1))
//            wap2sup = true;
//        if (null != accept && (accept.indexOf("vnd.wap") != -1))
//            wap1sup = true;
//
//        // ���Session�в����ڣ���ͨ������ͷaccept��Ϣ�ж�
//        if (wapVersion == null) {
//            wapVersion = Constants.WAP_VERSION_WAP1;
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("Accept : " + accept);
//            }
//
//            String userAgent = request.getHeader(Constants.REQUEST_HEAD_USER_AGENT);
//            if (accept != null && accept.indexOf("vnd.wap") == -1) {
//                // accept�а�html,xhtml,*/*��ʾ֧��WAP2.0
//                if (accept.indexOf("html") != -1 || accept.indexOf("*/*") != -1) {
//                    wapVersion = Constants.WAP_VERSION_WAP2;
//                } else if (userAgent != null && userAgent.indexOf(Constants.USER_AGENT_SAFARI) != -1) {
//                    wapVersion = Constants.WAP_VERSION_WAP2;
//                } else if (accept.indexOf(Constants.HEAD_TYPE_EASOU_CLIENT) != -1) {
//                    wapVersion = Constants.WAP_VERSION_EASOU_CLIENT;
//                }
//            }
//        }
//        if (session != null) {
//            // ����ȡ����WAPЭ��汾����Session
//            session.setAttribute(Constants.SESSION_ATTRIBUTE_WAP_VERSION, wapVersion);
//            session.setAttribute("wap1", wap1sup);
//            session.setAttribute("wap2", wap2sup);
//        }
//        // ����ȡ����WAPЭ��汾����cookie
//        if (StringUtils.isEmpty(cookie_wap_version) || !cookie_wap_version.equals(wapVersion)) {
//            if ((wapVersion.equals(Constants.WAP_VERSION_WAP1) && wap1sup)
//                    || (wapVersion.equals(Constants.WAP_VERSION_WAP2) && wap2sup)) {
//                Cookie cookie = new Cookie(Constants.SESSION_ATTRIBUTE_WAP_VERSION, wapVersion);
//                cookie.setMaxAge(86400 * 365 * 10);
//                cookie.setPath("/");
//                cookie.setDomain("easou.com");
//                response.addCookie(cookie);
//            }
//        }
//
//        // EASOU wap2.0�淶,��wver�������cookie(�Ųʰ�ֵΪc��������ֵΪt�����ֵΪs)
//        if (null == wver || "".equals(wver)) {
//            if (wapVersion.equals(Constants.WAP_VERSION_WAP1))
//                wver = "s";
//            if (wapVersion.equals(Constants.WAP_VERSION_WAP2))
//                wver = "c";
//        }
//        Cookie cookie = new Cookie("wver", wver);
//        cookie.setMaxAge(86400 * 365 * 10);
//        cookie.setPath("/");
//        cookie.setDomain("easou.com");
//        response.addCookie(cookie);
//
//        return wapVersion;
//    }
//
//    /**
//     * ��֤WAPЭ��汾����Ч��
//     * 
//     * @param wapVersion
//     * @return
//     */
//    private boolean validateWapVersion(String wapVersion) {
//        if (Constants.WAP_VERSION_EASOU_CLIENT.equals(wapVersion) || Constants.WAP_VERSION_WAP1.equals(wapVersion)
//                || Constants.WAP_VERSION_WAP2.equals(wapVersion)
//                || Constants.WAP_VERSION_EASOU_COOPERATE.equals(wapVersion)) {
//            return true;
//        }
//        return false;
//    }
}
