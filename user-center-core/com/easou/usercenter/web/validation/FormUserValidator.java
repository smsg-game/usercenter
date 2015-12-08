package com.easou.usercenter.web.validation;

import com.easou.common.util.DateUtil;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.util.EucSensitiveWordFilterUtil;
import com.easou.usercenter.util.SensitiveWordFilterUtil;
import com.easou.usercenter.util.UserUtil;

public class FormUserValidator {

	/**
	 * 检测登录名是否合法
	 * 
	 * @param userName
	 * @return
	 */
	public static String checkUserName(String username) {
		String result = "";
		if (StringUtil.isEmpty(username)) {
			result = "登录名不能为空";
		} else if (!StringUtil.isValidate(username)
				|| StringUtil.isNumber(username)) {
			result = "登录名只支持英文、数字或者“_“组合";
		} else if (username.length() < 4) {
			result = "登录名最短4位";
		} else if (username.length() > 18) {
			result = "登录名最长18位";
		} else if (EucSensitiveWordFilterUtil.isExistKeyByContent(username.toLowerCase())) {
			result = "登录名包含敏感词";
		}
		return result;
	}

	/**
	 * 检测登录名是否包含敏感词
	 * 
	 * @param username
	 * @return
	 */
	private static boolean isExistKeyByContent(String username) {
		return SensitiveWordFilterUtil.getHashKey().isExistKeyByContent(
				username, "AB");
	}

	public static String checkPassword(String str) {
		String result = "";
		if (null == str || "".equals(str.trim())) {
			result = "密码不能为空";
		} else if (!StringUtil.isHalfChar(str)) {
			result = "密码必须为半角字符";
		} else if (str.length() > 18) {
			result = "密码最长18位";
		} else if (str.length() < 6) {
			result = "密码最短6位";
		}
		return result;
	}

	/**
	 * 检查确认密码
	 * 
	 * @param password
	 * @param cfmpwd
	 * @return
	 */
	public static String checkCfmPassword(String password, String cfmpwd) {
		String result = "";
		if (StringUtil.isEmpty(cfmpwd))
			result = "确认密码不能为空";
		if (!StringUtil.isEmpty(password) && !StringUtil.isEmpty(cfmpwd)
				&& !password.equals(cfmpwd))
			result = "两次密码不一致，请重新输入！";
		return result;
	}

	/**
	 * 验证用户昵称是否合法。
	 * 
	 * @param nickName
	 *            {@link String} 昵称
	 * @return {@link String} 返回不合法原因，为 ""则合法.
	 */
	public static String checkNickName(String nickName) {
		String result = "";
		if (null == nickName || "".equals(nickName.trim())) {
			result = "昵称不能为空";
		} else {
			int len = StringUtil.uniLength(nickName);
			if (len > 16) {
				result = "昵称最长16位(中文2位)";
			} else if (len < 2) {
				result = "昵称最短2位";
			} else if (EucSensitiveWordFilterUtil.isExistKeyByContent(nickName.toLowerCase())) {
				result = "昵称包含敏感词";
			}
		}
		return result;
	}

	/**
	 * 判定手机是否合法
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobile(String mobile) {
		if (null==mobile || "".equals(mobile)
                || !mobile.matches("1[3458]\\d{9}"))
            return false;
        return true;
	}

	public static String checkBirthDay(Integer birthDay) {
		String result = "";
		if(null == birthDay) {
			return "出生年份错误";
		} else {
			String strDay = birthDay + "";
			if (!strDay.matches("(19|20)\\d{2}")) {
				result = "出生年份不合法";
			} else if (birthDay.intValue() > getCurrentYear()) {
				result = "出生年份不能大于当前年份";
			}
		}
		return result;
	}

	private static int getCurrentYear() {
		return Integer.parseInt(DateUtil.getCurrentYear());
	}

	public static String checkIntor(String intor) {
		String result = "";
		if (null != intor) {
			if (intor.length() > 140) {
				result = "个人简介不能超过140个字符";
			} else if (SensitiveWordFilterUtil.getHashKey()
					.isExistKeyByContent(intor, "A")) {
				result = "个人简介包含敏感词";
			}
		}
		return result;
	}

	 public static boolean checkCity(String city) {
		return UserUtil.checkCity(city);
	 }

	/**
	 * 性别信息验证
	 * 
	 * @param sex
	 *            性别
	 * @return
	 */
	public static boolean checkSex(Integer sex) {
		if(null!=sex && sex.intValue()<2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查职业
	 * 
	 * @param occupation
	 *            职业信息
	 * @return
	 */
	public static boolean checkOccupation(Integer occuId) {
		if (null!=occuId && occuId.intValue()<20){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkEmail(String email) {
		if(null==email || "".equals(email) || !StringUtil.isEmail(email)) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println("[" + checkNickName("最长八个中文字，超") + "]");
		System.out.println(checkMobile("18241434118"));
	}
}
