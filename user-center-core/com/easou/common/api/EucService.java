package com.easou.common.api;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.para.AuthParametric;



public class EucService {
	
	private String appId;
	
	private String partnerId;

	private String key;
    /*SDK版本*/
	private String version;

	private String apiServer;
	
	private String source;
	

	private static EucService instance = null;

	private EucService() {

	}

	public static EucService getInstance() {
		if (null == instance) {
			instance = new EucService();
			Properties pro = getProperties();
			instance.init(pro);
//			ApiConfig.init(pro);
		}
		return instance;
	}
	
	private static Properties getProperties() {
		InputStream is = EucService.class.getResourceAsStream("/client.properties");
		Properties pro = new Properties();
		try {
			pro.load(is);
		} catch (Exception e) {
			//log.warn("配置文件client.properties不存在");
			e.printStackTrace();
		}
		return pro;
	}

	private void init(Properties pro) {
		try {
			appId = ClientConfig.getProperty("appId", "");
			partnerId = ClientConfig.getProperty("partnerId", "");
			key = ClientConfig.getProperty("secertKey", "");
			version = ClientConfig.getProperty("version");
			source = ClientConfig.getProperty("source");
			apiServer = ClientConfig.getProperty("apiServer");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 构建请求头部
     * 
     * @return
     */
	public JHead buildDefaultRequestHeader(){
		JHead head = new JHead();	
		head.setAppId(appId);
		head.setVersion(version);
		head.setPartnerId(partnerId);
		head.setSource(source);
		return head;
	}

	/**
	 * 获得接口结果
	 * @param path
	 *            请求路径
	 * @param jHead
	 *            请求头部
	 * @param jBody
	 * @param eucParser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JBean getResult(String path, JBody jBody, AuthParametric authPara,
			RequestInfo info) {
		try {
			// 生成公共请求头
			JHead jHead = authPara.getVeriHeader(jBody, this, info);
			// 生成请求参数
			JSONObject jo = new JSONObject();
			jo.put("head", jHead);
			jo.put("body", jBody);
			// 请求参数
			String requestJSON = JSON.toJSONString(jo);
			// TODO请求接口并返回结果
			String result = executeUrl(buildURL(apiServer + path, info),
					requestJSON);
			JBean jbean = parserJsonResult(result);
			return jbean;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static JBean parserJsonResult(String json) {
		if(json==null||"".equals(json.trim())) {
			return null;
		} else {
			JBean result=JSON.parseObject(json, JBean.class);
			return result;
		}
	}
	
	/**
	 * 执行HTTP URL请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	private String executeUrl(String url,String json){
		Map paraMap = new HashMap();
		paraMap.put("json", json);
		return EucHttpClient.httpPost(url, paraMap);
	}
	
	/**
	 * 请求参数信息
	 * 
	 * @param uri
	 *     uri信息
	 * @param info
	 *     请求信息
	 * @return
	 */
	public String buildURL(String url,RequestInfo info){
		if(info==null)
			return url;
		StringBuffer buffer = new StringBuffer();
		buffer.append(url).append("?").append(info.paraToString());
		return buffer.toString();
	}

	public String getAppId() {
		return appId;
	}

	public String getVersion() {
		return version;
	}

	public String getKey() {
		return key;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getApiServer() {
		return apiServer;
	}

	public String getSource() {
		return source;
	}
	
}