package com.easou.usercenter.service;

import java.util.List;
import java.util.Map;

import com.easou.usercenter.entity.EucUser;

public interface EucUserService {
	
	/**
	 * 根据用户名或Id或手机号其中一个及密码获取用户信息
	 * @param idValue	用户名或Id或手机号其中一个的值
	 * @param md5Pwd	md5密码值
	 * @return
	 */
	public EucUser queryUserByIds(String idValue, String md5Pwd);
	
	/**
	 * 通过id查找用户信息
	 * @param userId
	 * @return
	 */
	public EucUser queryUserInfoById(String userId);
	/**
	 * 通过登录名查找用户(登录名唯一)
	 * @param userName
	 * @return
	 */
	public EucUser queryUserInfoByName(String userName);
	/**
	 * 通过昵称查找用户信息(唯一)
	 * @param nickName
	 * @return
	 */
	public EucUser queryUserInfoByNickName(String nickName);
	/**
	 * 通过手机号查找用户信息(唯一)
	 * @param mobile
	 * @return
	 */
	public EucUser queryUserInfoByMobile(String mobile);
	/**
	 * 通过email地址查询用户信息(email地址唯一)
	 * @param email
	 * @return
	 */
	public EucUser queryUserInfoByEmail(String email);
	/**
	 * 更新用户
	 * @param userId
	 * @param user	用户实体，不为空的对象进行更新
	 * @return
	 */
	public boolean updateUserById(String userId, EucUser user);
	/**
	 * 更新密码
	 * @param userId
	 * @param newPass
	 * @return
	 */
	public boolean updatePasswd(String userId, String newPass);
	/**
	 * 获得职业信息列表
	 * @return
	 */
	public List<Map> getOccupation();
	/**
	 * 通过手机号密码插入数据
	 * @param mobile	11位手机号
	 * @param passwd	加密密码
	 * @return	用户实体 @EucUser
	 */
	public EucUser insertUserForMobileRegist(String mobile, String passwd, String qn);
	
	/**
	 * 通过用户名密码插入数据
	 * @param username	18位以内用户名
	 * @param passwd	加密密码
	 * @return	用户实体 @EucUser
	 */
	public EucUser insertUserForUnameRegist(String username, String passwd, String qn);
	
	/**
	 * 自动生成默认值插入数据
	 * @return	用户实体 @EucUser
	 */
	public EucUser insertUserByDefault(String passwd, String nickName);
	
	/**
	 * 自动生成默认值插入数据
	 * @return	用户实体 @EucUser
	 */
	public EucUser insertUserByDefault(String passwd, String nickName, String qn);
	/**
	 * 通过email地址和密码注册
	 * @param email
	 * @param passwd
	 * @return
	 */
	public EucUser insertUserByEmailRegist(String email, String passwd, String qn);
	/**
	 * 批量更新数据
	 */
	public void batchUpdateUser(List<EucUser> userList,boolean updateCache);
	/**
	 * 更新用户手机号
	 * @param userId
	 * @param mobile	如果为空，则设置为空
	 * @return
	 */
	public boolean updateMobileById(String userId, String mobile);
	/**
	 * 更新用户邮箱
	 * @param userId
	 * @param email
	 * @return
	 */
	public boolean updateEmailById(String userId, String email);
}
