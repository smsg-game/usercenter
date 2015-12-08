package com.easou.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLTools {

    private static Pattern pattern = Pattern.compile("http://([^/:]+)(:(\\d+))?(/.*)?", Pattern.CASE_INSENSITIVE);

    public static String getHostFromURL(String urlStr) {
        Matcher m = pattern.matcher(urlStr);
        if (m.matches()) {
            return m.group(1);
        } else {
            return "";
        }
    }

    public static String getPortFromURL(String urlStr) {
        Matcher m = pattern.matcher(urlStr);
        if (m.matches()) {
            String port = m.group(3);
            if (port != null) {
                return port;
            }
        }
        return "80";
    }

    public static String urlEncode(String s) {
        try {
            String s1 = URLEncoder.encode(s, "UTF-8");
            return s1;
        } catch (Exception ex) {
            return s;
        }
    }
    public static String urlDecode(String s){
        return urlDecode(s,"UTF-8");
    }

    public static String urlDecode(String s, String enc) {
        String tmpenc = null != enc ? enc : "UTF-8";
        if (s != null && !"".equals(s.trim())) {
            try {
                String s1 = URLDecoder.decode(s, tmpenc);
                return s1;
            } catch (Exception ex) {
            }
        }
        return "";
    }

    public static String escapeURL(String s) {
        try {
            String s1 = s.replaceAll("&", "&amp;");
            return s1;
        } catch (Exception ex) {
            return s;
        }
    }

    public static String unEscapeURL(String s) {
        try {
            String s1 = s.replaceAll("&amp;", "&");
            return s1;
        } catch (Exception ex) {
            return s;
        }
    }
}
