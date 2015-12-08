package com.easou.usercenter.dao;

import java.util.List;

import com.easou.usercenter.entity.OUser;

/**
 * 外部用户数据访问类
 * 
 * @author river
 * 
 */
public interface OuserDao {

	/**
	 * 添加用户绑定信息
	 * 
	 * @param user
	 *            绑定用户信息对象
	 */
	public void insertOuserInfo(OUser user);
	
	/**
	 * 根据外部用户ID和类型获取用户绑字信息
	 * @param extId
	 * @param type
	 * @return
	 */
	public OUser queryOUserByExtIdAndOType(String extId, String type);
	
	/**
	 * 根据easou用户ID及外部帐号类型查询用户绑定信息
	 * @param euid 梵町用户ID
	 * @param type 外部帐号类型
	 * @return
	 */
	public OUser queryOUserByEaIdAndOType(String eaid,String type);
	
	/**
	 * 根据easou用户ID及外部账号类型删除用户绑定信息
	 * @param euid 梵町用户ID
	 * @param type 外部账号类型
	 */
	public void deleteOuserInfo(String eaid,String type);
	
	/**
	 * 根据easou用户ID查找关联账号
	 * @param eaid
	 */
	public List<OUser> queryOUserByEaId(String eaid);
}
