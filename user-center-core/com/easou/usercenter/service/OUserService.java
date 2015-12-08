package com.easou.usercenter.service;

import java.util.List;

import com.easou.cas.authenticateion.ThirdPartUserInfo;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;

public interface OUserService {
	public EucUser insertUserAndBindRegist(String username, String passwd,
			ThirdPartUserInfo uinfo) throws Exception;
	
	public EucUser insertDefaultUserAndBindRegist(ThirdPartUserInfo tinfo) throws Exception;
	
	public OUser queryBindinInfo(String thirdId, String netType);
	
//	public EucUser queryUserInfoByName(String userName);
	
	/**
	 * 根据easou用户ID查找关联账号
	 * @param eaid
	 */
	public List<OUser> queryOUserByEaId(String eaid);
	
	public void deleteUserByEaid(String eaid, String netType);
}
