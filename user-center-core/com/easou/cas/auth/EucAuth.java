package com.easou.cas.auth;

import com.easou.common.api.RequestInfo;

/**
 * 鉴权信息
 * 
 * @author damon
 * @since 2012.06.28
 * @version 1.0
 * 
 */
public class EucAuth extends RequestInfo implements java.io.Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1331950782487831610L;

	/**
     * 鉴权后重定向的APP地址
     */
	private String service;
	
    /**
     * 鉴权时，发现没有登录时是否跳转到登陆页
     */
	private boolean gateWay;
	
    /**
     * 强制登录；鉴权时，不管用户是否有登录都进行重新登录页
     */
	private boolean renew;
	
	/**
	 *  模板类型
	 */
	private String wver;
	
    /**
     * 鉴权后重定向的APP地址
     */
	public String getService() {
		return service;
	}
	
    /**
     * 鉴权后重定向的APP地址
     */
	public void setService(String service) {
		this.service = service;
	}

    /**
     * 鉴权时，发现没有登录时是否跳转到登陆页
     * 【true】为不返回登录页；【false】返回登录页
     * 
     * @return
     */
	public boolean isGateWay() {
		return gateWay;
	}

    /**
     * 鉴权时，发现没有登录时是否跳转到登陆页
     * 
     * @param gateWay
     *     【true】为不返回登录页；【false】返回登录页
     */
	public void setGateWay(boolean gateWay) {
		this.gateWay = gateWay;
	}

    /**
     * 强制登录；鉴权时，不管用户是否有登录都进行重新登录页
     * 【true】为返回登录页；【false】不进行强制登录，即如果发现用已经登录过，直接返回，如果发现未登录才返回登录页面
     * 
     * @return
     */
	public boolean isRenew() {
		return renew;
	}

    /**
     * 强制登录；鉴权时，不管用户是否有登录都进行重新登录页
     * @param renew
     * 【true】为返回登录页；【false】不进行强制登录，即如果发现用已经登录过，直接返回，如果发现未登录才返回登录页面
     */
	public void setRenew(boolean renew) {
		this.renew = renew;
	}

	/**
	 * 模板参数
	 * 
	 * @return
	 */
	public String getWver() {
		return wver;
	}

	/**
	 * 模板参数
	 * @param wver
	 *      【c】彩版、【t】触版、【s】简版；默认为彩 版
	 * 
	 * @return
	 */
	public void setWver(String wver) {
		this.wver = wver;
	}

}
