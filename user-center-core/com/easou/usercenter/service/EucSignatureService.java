package com.easou.usercenter.service;

import java.util.Date;

import com.easou.usercenter.entity.EucSignature;

public interface EucSignatureService {
	public EucSignature queryById(Long id);
	
	public EucSignature queryByKey(String key);
	
	public boolean insertSignature(Long userId, String key, Date expire);
	
	public boolean updateSignature(Long userId, String key, Date expire);
}
