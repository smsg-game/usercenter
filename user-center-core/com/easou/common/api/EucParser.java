package com.easou.common.api;

public class EucParser {

//	private static final Logger log = LoggerFactory
//			.getLogger(EucParser.class);
//	
//	private String secretKey="";
//	
//	private String version;
//	
//	private String from;
//	
//	private String appId=ClientConfig.getProperty("appId", "");
//	
//	public EucParser() {
//		
//	}
//
//	public EucParser(String secretKey, String version, String from) {
//		this.secretKey=secretKey;
//		this.version=version;
//		this.from=from;
//	}
//	
//	public String parserJsonString(JBody body, String flowCode) throws EucParserException {
//		JHead head = new JHead();
//		if (null == version) {
//			// 没有流水号，没有版本号
//			throw new EucParserException("flowCode or version is null!");
//		}
//		if(null==body) {
//			body=new JBody();
//		}
//		head.setVeriCode(getVeriCode(body.toJSONString(), appId));
//		head.setAppId(appId);
//		head.setVersion(version);
//		if(null!=flowCode) {
//			head.setFlowCode(flowCode);
//		}
//		if(null!=from) {
//			head.setFrom(from);
//		}
//		JSONObject jo = new JSONObject();
//		jo.put("head", head);
//		jo.put("body", body);
////		return JSON.toJSONString(jo,SerializerFeature.WriteClassName);	// 写以类名，以便解释
//		return JSON.toJSONString(jo);
//	}
//	
//	public static JBean parserJsonResult(String json) {
//		if(json==null||"".equals(json.trim())) {
//			return null;
//		} else {
//			JBean result=JSON.parseObject(json, JBean.class);
//			return result;
//		}
//	}
//
//	public String getVeriCode(String content, String appId) {
//		StringBuffer sb = new StringBuffer();
//		sb.append(appId).append(content).append(secretKey);
//		if (log.isDebugEnabled()) {
//			log.debug(sb.toString());
//		}
//		return md5(sb.toString());
//	}
//
//	public void setSecretKey(String secretKey) {
//		this.secretKey = secretKey;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
//
//	public void setFrom(String from) {
//		this.from = from;
//	}
//	
//	public void setAppId(String appId) {
//		this.appId = appId;
//	}
//	
//	private String md5(String str) {
//		if (str == null) {
//			return null;
//		}
//		try {
//			return DigestUtils.md5Hex(str.getBytes("utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			log.error("md5 加密失败", e);
//		}
//		return null;
//	}
}