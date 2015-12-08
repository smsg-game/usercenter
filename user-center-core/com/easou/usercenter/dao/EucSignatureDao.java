package com.easou.usercenter.dao;

import com.easou.usercenter.entity.EucSignature;

public interface EucSignatureDao {
	
	public EucSignature queryById(Long id);
	
	public EucSignature queryByKey(String key);
	
	public boolean insertSignature(EucSignature signature);
	
	public boolean updateSignature(EucSignature signature);
	
	public boolean deleteSignature(Long id);

}