package com.easou.usercenter.service.impl;

import com.easou.usercenter.dao.EucAppDao;
import com.easou.usercenter.entity.EucPartner;
import com.easou.usercenter.service.EucAppService;

public class EucAppServiceImpl implements EucAppService {
	
	private EucAppDao eucAppDao;
	
	public EucAppDao getEucAppDao() {
		return eucAppDao;
	}

	public void setEucAppDao(EucAppDao eucAppDao) {
		this.eucAppDao = eucAppDao;
	}

	@Override
	public EucPartner queryById(String id) {
		return eucAppDao.queryById(id);
	}

	@Override
	public EucPartner queryByAppId(String appId) {
		return eucAppDao.queryByAppId(appId);
	}

	@Override
	public EucPartner queryByPartnerId(String partnerId) {
		return eucAppDao.queryByPartnerId(partnerId);
	}
}
