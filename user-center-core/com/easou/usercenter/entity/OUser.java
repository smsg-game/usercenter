package com.easou.usercenter.entity;

import com.easou.common.constant.OUserConstant;

/**
 * 外部访问用户信息
 * 
 * @author river
 * 
 * 增加昵称
 * 
 * @author damon 
 * @since 2013.02.19
 * @version 1.0.1
 * 
 */
public class OUser {
	private long id;
	// 梵町ID
	private long ea_id;
	// 状态
	private String status;
	// 网络类别
	private Integer net_id;
	//
	private String nick_name;

	public Integer getNet_id() {
		return net_id;
	}

	public void setNet_id(Integer netId) {
		net_id = netId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getEa_id() {
		return ea_id;
	}

	public void setEa_id(long eaId) {
		ea_id = eaId;
	}

	// 第三方用户Id
	private String third_id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getThird_id() {
		return third_id;
	}

	public void setThird_id(String thirdId) {
		third_id = thirdId;
	}
	
	public String getThirdName() {
		return OUserConstant.getThirdName(this.net_id + "");
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nickName) {
		this.nick_name = nickName;
	}
}
