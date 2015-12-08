package com.easou.usercenter.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.dao.EucPrivilegeDao;
import com.easou.usercenter.entity.EucPrivilege;
import com.easou.usercenter.service.EucPrivilegeService;

public class EucPrivilegeServiceImpl implements EucPrivilegeService {
	
	private Log log = LogFactory.getLog(EucPrivilegeServiceImpl.class);
	
	private EucPrivilegeDao eucPrivilegeDao;

	@Override
	public boolean isPrivilege(String ipAddress) {
		if(log.isDebugEnabled()) {
			log.debug("检查权限: ip地址: " + ipAddress);
		}
		if(null==ipAddress) {
			return false;
		}
		List<EucPrivilege> list = eucPrivilegeDao.queryIpAddress();
		for (EucPrivilege eucPrivilege : list) {
			if(ipAddress.equals(eucPrivilege.getIpAddress())) {
				return true;
			}
		}
		return false;
	}

	public EucPrivilegeDao getEucPrivilegeDao() {
		return eucPrivilegeDao;
	}

	public void setEucPrivilegeDao(EucPrivilegeDao eucPrivilegeDao) {
		this.eucPrivilegeDao = eucPrivilegeDao;
	}
}