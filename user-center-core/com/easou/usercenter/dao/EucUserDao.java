package com.easou.usercenter.dao;

import java.util.List;
import java.util.Map;

import com.easou.usercenter.entity.EucUser;

public interface EucUserDao {
	/**
	 * 根据用户名或Id或手机号其中一个及密码获取用户信息
	 * 
	 * @param idValue
	 *            用户名或Id或手机号其中一个的值
	 * @param md5Pwd
	 *            md5密码值
	 * @return
	 */
	public EucUser queryUserByIds(String idValue, String md5Pwd);
	public EucUser queryUserInfoByName(String userName);
	public EucUser queryUserInfoByNickName(String nickName);
	public EucUser queryUserInfoByMobile(String mobile);
	public EucUser queryUserInfoById(String userId);
	public void updateUserById(String userId, Map paramter);
	public boolean insertUser(EucUser eucUser);
	public List<Map> getOccupation();
	public EucUser queryUserInfoByEmail(String email);
	
	/**
	 * 批量更新
	 * 
	 * @param userList
	 *     用户数据
	 * @param updateCache
	 *     是否更新缓存
	 */
	public void batchUpdateUser(List<EucUser> userList,boolean updateCache);
	public void updateNullById(String userId, Map paramter);
}
