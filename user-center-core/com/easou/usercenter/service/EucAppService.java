package com.easou.usercenter.service;

import com.easou.usercenter.entity.EucPartner;

/**
 * app服务处理类
 * @author jay
 *
 */
public interface EucAppService {
	public EucPartner queryById(String id);
	public EucPartner queryByAppId(String appId);
	public EucPartner queryByPartnerId(String partnerId);
}
