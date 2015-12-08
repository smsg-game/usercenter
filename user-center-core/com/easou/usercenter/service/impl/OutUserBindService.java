package com.easou.usercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.jasig.cas.authentication.handler.PasswordEncoder;

import com.easou.cas.authenticateion.EasouUserEqualUtil;
import com.easou.usercenter.dao.EucUserDao;
import com.easou.usercenter.dao.OuserDao;
import com.easou.usercenter.datasource.ContextHolder;
import com.easou.usercenter.datasource.DataSourceCluster;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.web.exception.ValidateException;

public class OutUserBindService {

	private OuserDao ouserDao;

	private PasswordEncoder encode;
	@Resource(name = "eucUserDao")
	private EucUserDao userDao;

	// 现只考虑了新浪用户，如果有腾讯等其他用户需要改进此方法
//	public OUser queryBindinInfo(String sid) {
//		return ouserDao.queryOUserByOuid(sid);
//	}
	
	public OUser queryOUserByExtIdAndOType(String extId, String type) {
		return ouserDao.queryOUserByExtIdAndOType(extId, type);
	}

	/**
	 * 绑定用户数据信息
	 * 
	 * @param binder
	 *            数据对象
	 * @param sid
	 *            外部用户ID
	 * @return
	 * @throws Exception
	 */
	public EucUser bindingUser(final String username, final String password, String sid,
			String type,String nickName) throws ValidateException {
		String encodePwd = encode.encode(password);
		String uname = username.toLowerCase().trim();
		// 查询用户是否存在
		EucUser user = userDao.queryUserByIds(uname, encodePwd);
		// 验证用户信息
		if (user == null
				|| !EasouUserEqualUtil.equalUserInfo(uname, encodePwd, user)) {
			throw new ValidateException("用户不存在或者密码不正确");
		}
		if (!"1".equals(String.valueOf(user.getStatus()))) {
			throw new ValidateException("该用户已被锁定");
		}
		// 判定当前easou用户在当前外部用户平台是否已绑定过了帐号
		//查询可写数据库
		ContextHolder.setContext(DataSourceCluster.WRITE_DATA_SOURCE);
		OUser oUser = ouserDao.queryOUserByEaIdAndOType(
				String.valueOf(user.getId()), type);
		if (oUser != null)
			throw new ValidateException("当前用户已绑定过当前外部网站的帐号");
		bindingUser(user.getId(), type, sid,nickName);
		return user;
	}

	/**
	 * 绑定外部用户
	 * 
	 * @param easouId
	 *            easou内部用户信息
	 * @param type
	 *            类型
	 * @param oid
	 *            外部ID
	 * @throws Exception
	 */
	public void bindingUser(long easouId, String type, String oid,String nickName) {
		
		OUser ouser = new OUser();
		ouser.setEa_id(easouId);
		ouser.setThird_id(oid);
		ouser.setNet_id(new Integer(type));
		ouser.setNick_name(nickName);
		// 添加用户信息
		ouserDao.insertOuserInfo(ouser);
	}
	
	public List<OUser> queryOUserByEaId(String eaid) {
		return ouserDao.queryOUserByEaId(eaid);
	}
	
	public void deleteUserByEaid(String eaid, String type) {
		ouserDao.deleteOuserInfo(eaid, type);
	}

	public void setOuserDao(OuserDao ouserDao) {
		this.ouserDao = ouserDao;
	}

	public void setEncode(PasswordEncoder encode) {
		this.encode = encode;
	}

	public void setUserDao(EucUserDao userDao) {
		this.userDao = userDao;
	}

}
