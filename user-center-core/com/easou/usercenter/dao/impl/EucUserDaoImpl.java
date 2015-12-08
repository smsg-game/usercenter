package com.easou.usercenter.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import com.easou.common.util.StringUtil;
import com.easou.usercenter.cache.Cache;
import com.easou.usercenter.cache.ValueEffect;
import com.easou.usercenter.dao.EucUserDao;
import com.easou.usercenter.entity.EucUser;

@Repository
public class EucUserDaoImpl extends BeanCacheDaoWrapper implements EucUserDao {
	// 索引列名称
	private static final String[] indexColumnNames = new String[] { "name",
			"nickName", "mobile", "email" };
	/**
	 * 失效时间(一个星期失效)
	 */
	private static final int effictTime = 7 * 24 * 60 * 60;

	@Override
	protected String[] getIndexColumnNames() {
		return indexColumnNames;
	}

	/**
	 * 根据用户名称 获取用户信息
	 * 
	 * @param userName
	 *            用户名称
	 */
	public EucUser queryUserInfoByName(String userName) {
		try {
			return queryUserInfo("EucUserDao.queryUserInfoByName", "name",
					userName, null, null);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 根据用户昵称获取用户信息
	 * 
	 * @param nickName
	 *            用户昵称
	 * @return
	 */
	public EucUser queryUserInfoByNickName(String nickName) {
		try {
			return queryUserInfo("EucUserDao.queryUserInfoByNickName",
					"nickName", nickName, null, null);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 根据用户手机号查询用户信
	 * 
	 * @param mobile
	 *            用户手机号码
	 * @return
	 */
	public EucUser queryUserInfoByMobile(String mobile) {
		try {
			return queryUserInfo("EucUserDao.queryUserInfoByMobile", "mobile",
					mobile, null, null);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 根据用户名或Id或手机号其中一个及密码获取用户信息
	 * 
	 * @param idValue
	 *            用户名或Id或手机号其中一个的值
	 * @param md5Pwd
	 *            md5密码值
	 * @return
	 */
	public EucUser queryUserByIds(String username, String md5Pwd) {
		boolean is = Pattern.matches("\\d+", username);
		EucUser user = null;
		if (is && null!=md5Pwd) {
			user = queryUserInfoByMobile(username);
		} else {
			if(StringUtil.isEmail(username)) {
				user = queryUserInfoByEmail(username);
			}
			// 如果按email字段查找不到用户，通过用户名字段查找，因为早期部分用户以email作为用户名
			if(null==user)
				user = queryUserInfoByName(username);
		}
		if(null==user) {
			return user;
		}
		if(!md5Pwd.equals(user.getPasswd())) {
			return null;
		}
		return user;
	}

	/**
	 * 根据用户名及密码获取用户信息
	 * 
	 * @param userName
	 *            用户名称
	 * @param md5Pwd
	 *            md5密码值
	 * @return
	 */
	protected EucUser queryUserInfoByNameAndPwd(String userName, String md5Pwd) {
		Map paramter = new HashMap();
		paramter.put("name", userName);
		paramter.put("pwd", md5Pwd);
		try {
			return queryUserInfo("EucUserDao.queryUserInfoByNameAndPwd",
					"name$pwd", paramter, ValueEffect.INDEXEFFECT, effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 根据用户名或Id或手机号其中一个及密码获取用户信息
	 * 
	 * @param value
	 *            用户名或Id或手机号其中一个的值
	 * @param md5Pwd
	 *            md5密码值
	 * @return
	 */
	protected EucUser queryUserInfoByNMIandPwd(String value, String md5Pwd) {
		Map paramter = new HashMap();
		paramter.put("va", value);
		paramter.put("pwd", md5Pwd);
		try {
			return queryUserInfo("EucUserDao.queryUserInfoByNMIandPwd",
					"name$pwd$id$mobile", paramter, ValueEffect.INDEXEFFECT,
					effictTime);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public EucUser queryUserInfoById(String userId) {
		try {
			return queryUserInfoById("EucUserDao.queryUserById", userId, null);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 更新指定用户ID下的用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param paramter
	 *            参数信息
	 */
	public void updateUserById(String userId, Map paramter) {
		if(null==userId || "".equals(userId.trim())) {
			throw new RuntimeException("id不正确");
		}
		try {
			updateFiledValueWitchId("euc_user", userId, "EucUserDao.upadteUserByid", paramter,
					EucUser.class, false);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	/**
	 * 根据指定字段查询数据信息(指定字段必须是唯一的)
	 * 
	 * @param stateName
	 *            执行sql id
	 * @param indexName
	 *            索引字段名称
	 * @param indexValue
	 *            索引字段值
	 * @param effectTime
	 *            失效时间
	 * @return
	 * @throws Exception
	 */
	protected EucUser queryUserInfo(final String stateName, String indexName,
			Object indexValue, ValueEffect ef, Integer effectTime)
			throws Exception {
		return queryForObjectWithCache(stateName, "euc_user", "id", indexName,
				indexValue, EucUser.class, ef, effectTime);
	}

	/**
	 * 根据主键查询数据对象信息
	 * 
	 * @param stateName
	 *            执行sql id
	 * @param idValue
	 *            主健值
	 * @param effectTime
	 *            失效时间
	 * @return
	 */
	protected EucUser queryUserInfoById(final String stateName, String idValue,
			Integer effectTime) throws Exception {
		return queryForObjectWithCacheById(stateName, "euc_user", "id",
				idValue, EucUser.class, null);
	}

//	/**
//	 * 更新数据信息
//	 * 
//	 * @param statementName
//	 *            执行sql id
//	 * @param id
//	 *            主键ID
//	 * @param paramter
//	 *            更新字段信息
//	 */
//	protected void updateUserInfoById(String statementName, String id,
//			Map paramter) {
//		try {
//			updateFiledValueWitchId("euc_user", id, statementName, paramter,
//					EucUser.class);
//		} catch (Exception e) {
//			throw new RuntimeException("数据访问出错",e);
//		}
//	}

	/**
	 * 查询职业类别
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getOccupation() {
		try {
			return queryForListWithCache("EucUserDao.getOccupation",
					"euc_occupation", "occupation", Cache.ONE_HOURS);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错", e);
		}
	}

	@Override
	public boolean insertUser(EucUser eucUser) {
		int result=getSqlSession().insert("insertUser", eucUser);
		if(result > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public EucUser queryUserInfoByEmail(String email) {
		try {
			return queryUserInfo("EucUserDao.queryUserInfoByEmail", "email",
					email, null, null);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}

	@Override
	public void batchUpdateUser(List<EucUser> userList, boolean updateCache) {
		if(userList!=null&&userList.size()>0){
			getSqlSession().update("EucUserDao.batchUpdateUser", userList);
		}
		 /*if(userList!=null){
			for(EucUser user:userList){
			    Map<String,Object> paraMap = new HashMap<String,Object>();
			    paraMap.put("id", user.getId());
			    if(user.getLastLoginTime()!=null){
			        paraMap.put("lastLoginTime", user.getLastLoginTime());
			    }
				getSqlSession().update("EucUserDao.upadteUserByid", paraMap);
			}
		}*/
		//更新缓存
		if(updateCache){
			//TODO 有需求的时候增加
		}
	}

	@Override
	public void updateNullById(String userId, Map paramter) {
		if(null==userId || "".equals(userId.trim())) {
			throw new RuntimeException("id不正确");
		}
		try {
			updateFiledValueWitchId("euc_user", userId, "EucUserDao.upadteNullByid", paramter,
					EucUser.class, true);
		} catch (Exception e) {
			throw new RuntimeException("数据访问出错",e);
		}
	}	
}
