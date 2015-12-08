package com.easou.cas.authenticateion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

 
import com.easou.usercenter.entity.EucUser;

public class EasouUserEqualUtil {
	static Log  log = LogFactory.getLog(EasouUserEqualUtil.class);
	/**
	 * 判定当前用户是否是当前查询的用户信息
	 * 
	 * @param userName
	 *            登陆用户名
	 * @param pwd
	 *            登陆用户密码
	 * @param user
	 *            当前查询出来的用户信息
	 * @return
	 */
	public static boolean equalUserInfo(String userName, String pwd,
			EucUser user) {
		// 比较密码是否相同,这种比种主要考虑是用户修改密码,用户名及手机后老索引值还未失效
		if (log.isDebugEnabled()){
			log.debug("user.getPasswd():"+user.getPasswd());
			log.debug("user.getName():"+user.getName());
			log.debug("user.getMobile():"+user.getMobile());
			log.debug("user.getId():"+user.getId());
			log.debug("pwd:"+pwd);
			log.debug("userName:"+userName);
		}
		if (user.getName()==null)
			user.setName("");
		if (pwd.equals(user.getPasswd())
				&& (userName.toLowerCase().equals(user.getName().toLowerCase())
						|| userName.equals(user.getMobile()) || userName
						.equals(String.valueOf(user.getId())) || userName.equals(user.getEmail()))) {
			return true;
		}
		return false;
	}
}
