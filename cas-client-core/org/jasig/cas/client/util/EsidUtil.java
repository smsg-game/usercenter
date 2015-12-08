package org.jasig.cas.client.util;


public class EsidUtil {
//	/**
//	 * 从http请求中获取esid值
//	 * 
//	 * @param request
//	 *            http请求对象
//	 * @return
//	 */
//	public static String getEsidFromRequest(HttpServletRequest request) {
//		String esid = request.getParameter(MemSessionFilter.SESSION_ID_NAME);
//		// 如果url中无esid则从attribute中获取esid
//		if (esid == null) {
//			esid = (String) request
//					.getAttribute(MemSessionFilter.SESSION_ID_NAME);
//		}
//		return esid;
//	}
//
//	/**
//	 * 从http请求属性中获取esid
//	 * 
//	 * @param request
//	 *            http请求对象
//	 * @return
//	 */
//	public static String getEsidFromReqAttr(HttpServletRequest request) {
//		return (String) request.getAttribute(MemSessionFilter.SESSION_ID_NAME);
//	}
//
//	/**
//	 * 在url后边添加esid参数信息
//	 * 
//	 * @param url
//	 *            原请求url
//	 * @param esid
//	 *            esid
//	 * @return
//	 */
//	public static String appendEsidToUrl(String url, String esid) {
//		if (url == null)
//			return "";
//		if (url.indexOf("?") > 0) {
//			if (url.indexOf("?esid=") > 0||url.indexOf("&esid=") > 0) {
//				url = url.replaceAll("esid=[^&]+", "esid=" + esid);
//			} else {
//				url = url + "&esid=" + esid;
//			}
//		} else {
//			url = url + "?esid=" + esid;
//
//		}
//		return url;
//
//	}
//	
//	public static void main(String[] str){
//		System.out.println(appendEsidToUrl("/index.e?t6esid=O_OuvWIRUavzuvWIRUa5BOO","123456"));
//	}
}
