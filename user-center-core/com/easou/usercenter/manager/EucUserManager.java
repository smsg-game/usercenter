package com.easou.usercenter.manager;

import com.easou.usercenter.EucResult;
import com.easou.usercenter.entity.EucUser;

/**
 * 用户管理
 * 
 * @author damon
 * @since 2013.02.16
 * @version 1.0
 */
public interface EucUserManager {
	
	/**
	 * 更新用户信息
	 * 
	 * @param userId
	 *     用户ID
	 * @param updateUser
	 *     需要更新的用户信息
	 */
	public EucResult<EucUser> updateUserInfo(String userId,EucUser updateUser,boolean isVeri);

}
