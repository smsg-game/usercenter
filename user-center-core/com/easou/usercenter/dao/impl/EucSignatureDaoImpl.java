package com.easou.usercenter.dao.impl;

import org.springframework.stereotype.Repository;

import com.easou.usercenter.dao.EucSignatureDao;
import com.easou.usercenter.entity.EucSignature;

@Repository
public class EucSignatureDaoImpl extends DynamicSqlSessionDaoSupport implements EucSignatureDao {

	@Override
	public boolean insertSignature(EucSignature signature) {
		if(getSqlSession().insert("EucSignatureDao.insertSignature", signature)>0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public EucSignature queryById(Long id) {
		return (EucSignature)getSqlSession().selectOne("EucSignatureDao.queryById", id);
	}

	@Override
	public EucSignature queryByKey(String key) {
		return (EucSignature)getSqlSession().selectOne("EucSignatureDao.queryByKey", key);
	}

	@Override
	public boolean updateSignature(EucSignature signature) {
		if(getSqlSession().update("EucSignatureDao.updateSignature", signature)>0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteSignature(Long id) {
		if(getSqlSession().delete("EucSignatureDao.deleteSignature", id)>0) {
			return true;
		} else {
			return false;
		}
	}
}
