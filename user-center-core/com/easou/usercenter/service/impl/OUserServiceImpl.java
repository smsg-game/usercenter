package com.easou.usercenter.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.cas.authenticateion.ThirdPartUserInfo;
import com.easou.usercenter.config.SSOConfig;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.OUserService;

public class OUserServiceImpl implements OUserService {
	
	Log log = LogFactory.getLog(OUserServiceImpl.class);

	private EucUserService eucUserService;
    
	private OutUserBindService outUserBindService;

	public EucUserService getEucUserService() {
		return eucUserService;
	}

	public void setEucUserService(EucUserService eucUserService) {
		this.eucUserService = eucUserService;
	}

	public OutUserBindService getOutUserBindService() {
		return outUserBindService;
	}

	public void setOutUserBindService(OutUserBindService outUserBindService) {
		this.outUserBindService = outUserBindService;
	}

	@Override
	public EucUser insertUserAndBindRegist(String username, String passwd,
			ThirdPartUserInfo tinfo) throws Exception {
			EucUser eucUser = eucUserService.insertUserForUnameRegist(username, passwd, null);
			if (null == eucUser)
				return null;
			
			log.debug("用户插入成功");
			outUserBindService.bindingUser(eucUser.getId(), tinfo.getType(),
					tinfo.getThirdId(),tinfo.getNickName());
			return eucUser;
	}
	
	@Override
	public EucUser insertDefaultUserAndBindRegist(ThirdPartUserInfo tinfo)
			throws Exception {
		// 为第三方登录用户插入一个新的默认用户，密码为默认值
		String pwd=SSOConfig.getProperty("passwd.init");
		EucUser eucUser = eucUserService.insertUserByDefault(pwd, tinfo.getNickName());
		if (null == eucUser)
			return null;

		log.debug("用户插入成功");
		outUserBindService.bindingUser(eucUser.getId(), tinfo.getType(), tinfo
				.getThirdId(),tinfo.getNickName());
		return eucUser;
	}

	@Override
	public OUser queryBindinInfo(String thirdId, String netType) {
		return outUserBindService.queryOUserByExtIdAndOType(thirdId, netType);
	}

//	@Override
//	public EucUser queryUserInfoByName(String userName) {
//		return eucUserService.queryUserInfoByName(userName);
//	}

	@Override
	public List<OUser> queryOUserByEaId(String eaid) {
		return outUserBindService.queryOUserByEaId(eaid);
	}

	@Override
	public void deleteUserByEaid(String eaid, String netType) {
		outUserBindService.deleteUserByEaid(eaid, netType);
	}
}