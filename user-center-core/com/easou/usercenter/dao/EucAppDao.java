package com.easou.usercenter.dao;

import com.easou.usercenter.entity.EucPartner;

public interface EucAppDao {
	
	public EucPartner queryById(String id);
	public EucPartner queryByAppId(String appId);
	public EucPartner queryByPartnerId(String partnerId);

}