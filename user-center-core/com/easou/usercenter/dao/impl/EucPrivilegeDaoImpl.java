package com.easou.usercenter.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.easou.usercenter.dao.EucPrivilegeDao;
import com.easou.usercenter.entity.EucPrivilege;

@Repository
public class EucPrivilegeDaoImpl extends BeanCacheDaoWrapper implements EucPrivilegeDao {
	// 索引列名称
	private static final String[] indexColumnNames = new String[0];
	/**
	 * 失效时间(一天失效)
	 */
	private static final int effictTime = 24 * 60 * 60;

	@Override
	protected String[] getIndexColumnNames() {
		return indexColumnNames;
	}

	@Override
	public List<EucPrivilege> queryIpAddress() {
		try {
			return queryForListWithCache("EucPrivilegeDao.queryIpAddress",
					"euc_privilege", "ipaddress", effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}
}
