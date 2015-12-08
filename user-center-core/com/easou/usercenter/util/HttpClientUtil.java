package com.easou.usercenter.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.easou.usercenter.config.SSOConfig;

public class HttpClientUtil {
    
	public static String HTTP_CONNECT_TIMEOUT_TAG = "http.connect.timeout";
	//默认超时
	public static int HTTP_CONNECT_TIMEOUT = 30000;
    public static String executeUrl(String strUrl, String json) {
        URL url = null;
        HttpURLConnection httpurlconnection = null;
        try {
            url = new URL(strUrl);
            httpurlconnection = (HttpURLConnection) url.openConnection();
            if(SSOConfig.getProperty(HTTP_CONNECT_TIMEOUT_TAG)!=null){
            	httpurlconnection.setConnectTimeout(
            			Integer.valueOf(SSOConfig.getProperty(HTTP_CONNECT_TIMEOUT_TAG)));
            }else{
                httpurlconnection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
            }
            httpurlconnection.setDoOutput(true);
            if(null == json) {
            	httpurlconnection.setRequestMethod("GET");
            } else {
            	httpurlconnection.setRequestMethod("POST");
            	httpurlconnection.getOutputStream().write(json.getBytes("UTF8"));
            }
            httpurlconnection.getOutputStream().flush();
            httpurlconnection.getOutputStream().close();
            
            int code = httpurlconnection.getResponseCode();
            
            if (200 == code) {
                InputStream is = httpurlconnection.getInputStream();
                byte[] result = new byte[httpurlconnection.getContentLength()];
                is.read(result);
                String t = new String(result, "UTF8");
                return t;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpurlconnection != null)
                httpurlconnection.disconnect();
        }
    }
}