package com.easou.usercenter.dao.impl;

import org.springframework.stereotype.Repository;

import com.easou.usercenter.dao.EucAppDao;
import com.easou.usercenter.entity.EucPartner;

@Repository
public class EucAppDaoImpl extends BeanCacheDaoWrapper implements EucAppDao {

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
	public EucPartner queryById(String id) {
		try {
			return queryForObjectWithNoIndexCache("EucAppDao.queryById",
					"euc_app", id, EucPartner.class, effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}

	@Override
	public EucPartner queryByAppId(String appId) {
		try {
			return queryForObjectWithNoIndexCache("EucAppDao.queryByAppId",
					"euc_app", appId, EucPartner.class, effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}

	@Override
	public EucPartner queryByPartnerId(String partnerId) {
		try {
			return queryForObjectWithNoIndexCache("EucAppDao.queryByPartnerId",
					"euc_app", partnerId, EucPartner.class, effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}
}