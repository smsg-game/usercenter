package com.easou.usercenter.service.impl;

import java.util.Date;

import com.easou.usercenter.dao.EucSignatureDao;
import com.easou.usercenter.entity.EucSignature;
import com.easou.usercenter.service.EucSignatureService;

public class EucSignatureServiceImpl implements EucSignatureService {
	
	private EucSignatureDao eucSignatureDao;

	@Override
	public boolean insertSignature(Long userId, String key, Date expire) {
		EucSignature signature = new EucSignature();
		signature.setId(userId);
		signature.setKey(key);
		signature.setExpire(expire);
		return eucSignatureDao.insertSignature(signature);
	}

	@Override
	public EucSignature queryById(Long id) {
		return eucSignatureDao.queryById(id);
	}

	@Override
	public EucSignature queryByKey(String key) {
		return eucSignatureDao.queryByKey(key);
	}

	@Override
	public boolean updateSignature(Long userId, String key, Date expire) {
		EucSignature signature = new EucSignature();
		signature.setId(userId);
		signature.setKey(key);
		signature.setExpire(expire);
		return eucSignatureDao.updateSignature(signature);
	}

	public EucSignatureDao getEucSignatureDao() {
		return eucSignatureDao;
	}

	public void setEucSignatureDao(EucSignatureDao eucSignatureDao) {
		this.eucSignatureDao = eucSignatureDao;
	}

}
