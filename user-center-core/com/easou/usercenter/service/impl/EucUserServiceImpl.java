package com.easou.usercenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.util.MD5Util;
import com.easou.usercenter.dao.EucUserDao;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.SessionUtil;

public class EucUserServiceImpl implements EucUserService {
	
	private static String DEFAULT_NAME =  "ft.";
	
	private Log log = LogFactory.getLog(EucUserServiceImpl.class);

	private EucUserDao eucUserDao;

	@Override
	public List<Map> getOccupation() {
		return eucUserDao.getOccupation();
	}
	
	@Override
	public EucUser queryUserByIds(String idValue, String md5Pwd) {
		return eucUserDao.queryUserByIds(idValue, md5Pwd);
	}

	@Override
	public EucUser queryUserInfoById(String userId) {
		if(userId == null  || userId.length() == 0){
			return null;
		}
		return eucUserDao.queryUserInfoById(userId);
	}

	@Override
	public EucUser queryUserInfoByMobile(String mobile) {
		return eucUserDao.queryUserInfoByMobile(mobile);
	}

	@Override
	public EucUser queryUserInfoByName(String userName) {
		return eucUserDao.queryUserInfoByName(userName);
	}

	@Override
	public EucUser queryUserInfoByNickName(String nickName) {
		return eucUserDao.queryUserInfoByNickName(nickName);
	}

	@Override
	public boolean updateUserById(String userId, EucUser user) {
		if(log.isDebugEnabled()) {
			log.debug("update user by id: " + userId);
		}
		if (user != null) {
			HashMap<String, Object> paramter = new HashMap<String, Object>();
			// BeanProperty.copyToMap(user, paramter);
			// 为保证效率，不采用通用方式获取属性
//			if (null != user.getName())
//				paramter.put("name", user.getName());
			if (null != user.getPasswd())
				paramter.put("passwd", user.getPasswd());
			if (null != user.getNickName())
				paramter.put("nickName", user.getNickName());
			if (null != user.getQuestion())
				paramter.put("question", user.getQuestion());
			if (null != user.getAnswer())
				paramter.put("answer", user.getAnswer());
			if (null != user.getMobile())
				paramter.put("mobile", user.getMobile());
			if (null != user.getBirthday())
				paramter.put("birthday", user.getBirthday());
			if (null != user.getCity())
				paramter.put("city", user.getCity());
			if (null != user.getOccuId())
				paramter.put("occuId", user.getOccuId());
			if (null != user.getStatus())
				paramter.put("status", user.getStatus());
			if (null != user.getSex())
				paramter.put("sex", user.getSex());
			if (!StringUtils.isEmpty(user.getEmail()))
				paramter.put("email", user.getEmail());
			eucUserDao.updateUserById(userId, paramter);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean updatePasswd(String userId, String newPass) {
		EucUser user = new EucUser();
		user.setPasswd(MD5Util.md5(newPass));
		return updateUserById(userId, user);
	}

	@Override
	public EucUser insertUserForMobileRegist(String mobile, String passwd, String qn) {
		if(log.isInfoEnabled()) {
			log.info("手机注册添加用户：" + mobile + " 密码：" + passwd);
		}
		EucUser eucUser = new EucUser();
		eucUser.setName("ftauto");
		eucUser.setMobile(mobile);
		eucUser.setPasswd(passwd);
		eucUser.setStatus(1);
		eucUser.setSex(0);
		eucUser.setRandomPasswd(SessionUtil.getNewRandomPwd(""));
		eucUser.setNickName("ft");
		eucUser.setQn(qn);
		if (eucUserDao.insertUser(eucUser)) {
			if (eucUser.getId() != null) {
				// 为了写入缓存，所以查询一次
				EucUser eu = eucUserDao.queryUserInfoById(eucUser.getId()+"");
				Map<String, Object> param = new HashMap<String, Object>();
				String newName = DEFAULT_NAME + eu.getId();
				String newNick = "ft" + eu.getId();
				param.put("name", newName);
				param.put("nickName", newNick);
				eucUserDao.updateUserById(eu.getId().toString(), param);
				eu.setName(newName);
				eu.setNickName(newNick);
				return eu;
			}
		}
		return null;
	}

	@Override
	public EucUser insertUserForUnameRegist(String username, String passwd, String qn) {
		if(log.isInfoEnabled()) {
			log.info("登录名注册添加用户：" + username + " 密码：" + passwd);
		}
		EucUser eucUser = new EucUser();
		eucUser.setName(username);
		eucUser.setPasswd(passwd);
		eucUser.setStatus(1);
		eucUser.setSex(0);
		eucUser.setRandomPasswd(SessionUtil.getNewRandomPwd(""));
		eucUser.setNickName("ft");
		eucUser.setQn(qn);
		if (eucUserDao.insertUser(eucUser)) {
			// 插入正确
			if (eucUser.getId() != null) {
				// 为了写入缓存，所以查询一次
				EucUser eu = eucUserDao.queryUserInfoById(eucUser.getId()+"");
				Map<String, Object> param = new HashMap<String, Object>();
				String newNick = "ft" + eu.getId();
				param.put("nickName", newNick);
				eucUserDao.updateUserById(eu.getId().toString(), param);
				eu.setNickName(newNick);
				return eu;
			}
		}
		return null;
	}
	
	@Override
	public EucUser insertUserByDefault(String passwd, String nickName, String qn) {
		EucUser eucUser = new EucUser();
		eucUser.setName("ftauto");
		//String md5Pass = MD5Util.md5(RandomKeyGenerator.genNumber(6));
		String md5Pass = null;
		if(passwd!=null&&!"".equals(passwd)){
			md5Pass = MD5Util.md5(passwd);
		}
		eucUser.setPasswd(md5Pass);
		eucUser.setStatus(1);
		eucUser.setSex(0);
		eucUser.setRandomPasswd(SessionUtil.getNewRandomPwd(""));
		eucUser.setQn(qn);
		if(null!=nickName && !"".equals(nickName.trim())) {
			eucUser.setNickName(nickName);
		} else {
			eucUser.setNickName("ft.");
		}
		if(log.isInfoEnabled()) {
			log.info("默认注册添加用户：" + eucUser.getName() + " 密码：" + eucUser.getPasswd());
		}
		if (eucUserDao.insertUser(eucUser)) {
			if (eucUser.getId() != null) {
				// 为了写入缓存，所以查询一次
				EucUser eu = eucUserDao.queryUserInfoById(eucUser.getId()+"");
				Map<String, Object> param = new HashMap<String, Object>();
				String newName = DEFAULT_NAME + eu.getId();
				String newNick = eucUser.getNickName();
				if(null==nickName || "".equals(nickName.trim())) {
					newNick = "ft." + eu.getId();
				}
				param.put("name", newName);
				param.put("nickName", newNick);
				eucUserDao.updateUserById(eu.getId().toString(), param);
				eu.setName(newName);
				eu.setNickName(newNick);
				return eu;
			}
		}
		return null;
	}
	
	@Override
	public EucUser insertUserByDefault(String passwd, String nickName) {
		return insertUserByDefault(passwd, nickName, null);
	}

	public EucUserDao getEucUserDao() {
		return eucUserDao;
	}

	public void setEucUserDao(EucUserDao eucUserDao) {
		this.eucUserDao = eucUserDao;
	}

	@Override
	public EucUser queryUserInfoByEmail(String email) {
		return eucUserDao.queryUserInfoByEmail(email);
	}

	@Override
	public EucUser insertUserByEmailRegist(String email, String passwd, String qn) {
		if(log.isInfoEnabled()) {
			log.info("email注册添加用户：" + email + " 密码：" + passwd);
		}
		EucUser eucUser = new EucUser();
		eucUser.setName("ftauto");
		eucUser.setEmail(email.toLowerCase());
		eucUser.setPasswd(MD5Util.md5(passwd));
		eucUser.setStatus(1);
		eucUser.setSex(0);
		eucUser.setRandomPasswd(SessionUtil.getNewRandomPwd(""));
		eucUser.setNickName("ft");
		eucUser.setQn(qn);
		if (eucUserDao.insertUser(eucUser)) {
			// 插入正确
			if (eucUser.getId() != null) {
				// 为了写入缓存，所以查询一次
				EucUser eu = eucUserDao.queryUserInfoById(eucUser.getId()+"");
				Map<String, Object> param = new HashMap<String, Object>();
				String newName = DEFAULT_NAME + eu.getId();
				String newNick = "ft" + eu.getId();
				param.put("name", newName);
				param.put("nickName", newNick);
				eucUserDao.updateUserById(eu.getId().toString(), param);
				eu.setName(newName);
				eu.setNickName(newNick);
				return eu;
			}
		}
		return null;
	}

	@Override
	public void batchUpdateUser(List<EucUser> userList, boolean updateCache) {
		eucUserDao.batchUpdateUser(userList, updateCache);
	}

	@Override
	public boolean updateMobileById(String userId, String mobile) {
		Map paramter = new HashMap();
		if(null==mobile) {
			paramter.put("mobile", "");
			eucUserDao.updateNullById(userId, paramter);
		} else {
			paramter.put("mobile", mobile);
			eucUserDao.updateUserById(userId, paramter);
		}
		return true;
	}

	@Override
	public boolean updateEmailById(String userId, String email) {
		Map paramter = new HashMap();
		if(null==email) {
			paramter.put("email", "");
			eucUserDao.updateNullById(userId, paramter);
		} else {
			paramter.put("email", email);
			eucUserDao.updateUserById(userId, paramter);
		}
		return true;
	}
}