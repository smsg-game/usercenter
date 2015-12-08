package com.easou.usercenter.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.easou.usercenter.cache.ValueEffect;
import com.easou.usercenter.dao.OuserDao;
import com.easou.usercenter.entity.OUser;

@Repository
public class OuserDaoImpl extends BeanCacheDaoWrapper implements OuserDao {
	private static final String[] indexColumnNames = new String[] {};
	/**
	 * 失效时间(一个星期失效)
	 */
	private static final int effictTime = 7 * 24 * 60 * 60;

	@Override
	protected String[] getIndexColumnNames() {
		return indexColumnNames;
	}

	@Override
	public void insertOuserInfo(OUser ouser) {
		cache.del(getIdKey("euc_binding",ouser.getEa_id()+""));
		getSqlSession().insert("OuserDao.insertOuserInfo", ouser);
	}

	@Override
	public OUser queryOUserByEaIdAndOType(String eaid, String type) {
		try {
			Map paramter = new HashMap();
			paramter.put("easou_id", eaid);
			paramter.put("type", type);
			return this.queryForObjectWithCache(
					"OuserDao.queryOUserByeidAndOtype", "euc_binding", "id",
					"easou_id$type", paramter, OUser.class, ValueEffect.ALLEFFECT,
					effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}
	
	@Override
	public OUser queryOUserByExtIdAndOType(String extId, String type) {
		try {
			Map paramter = new HashMap();
			paramter.put("third_id", extId);
			paramter.put("type", type);
			return this.queryForObjectWithCache(
					"OuserDao.queryOUserByExtIdAndOType", "euc_binding", "id",
					"third_id$type", paramter, OUser.class, ValueEffect.ALLEFFECT,
					effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}

	@Override
	public void deleteOuserInfo(String eaid, String type) {
		try {
			cache.del(getIdKey("euc_binding",eaid));
			OUser ouser = queryOUserByEaIdAndOType(eaid, type);
			if(null!=ouser) {
				deleteWitchCache("OuserDao.deleteOuserInfo", "euc_binding", ouser.getId());
			}
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}

	@Override
	public List<OUser> queryOUserByEaId(String eaid) {
		try {
			return queryForListWithCache("OuserDao.queryOUserByEaId",
					"euc_binding", eaid, effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错!", e);
		}
	}
}