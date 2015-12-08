package com.easou.cas.authenticateion;

import java.io.Serializable;
import java.util.Date;

public class ThirdPartUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7399381795342685237L;
	// access token
	private String accToken;
	// 第三方用户id
	private String thirdId;
	// easou用户ID
	private String easouId;
	
	private String type = "1";
	
	private String nickName;
	
	private Date registTime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccToken() {
		return accToken;
	}

	public void setAccToken(String accToken) {
		this.accToken = accToken;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public String getEasouId() {
		return easouId;
	}

	public void setEasouId(String easouId) {
		this.easouId = easouId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
}
