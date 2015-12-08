package com.easou.usercenter.manager.impl;

import javax.annotation.Resource;

import com.easou.usercenter.EucErrorConstant;
import com.easou.usercenter.EucResult;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.manager.EucUserManager;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.validation.FormUserValidator;

public class EucUserManagerImpl implements EucUserManager {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	@Override
	public EucResult<EucUser> updateUserInfo(String userId, EucUser updateUser,
			boolean isVeri) {
		// EucApiResult<EucUser> result = new EucApiResult<EucUser>();
		// TODO 判断userId是否存在
		EucUser curUser = eucUserService.queryUserInfoById(userId);
		if (curUser == null) {// 无用户存在
			EucResult<EucUser> result = EucResult.buildBusErrorResult(
					EucErrorConstant.USER_NOT_EXIST_CODE,
					EucErrorConstant.USER_NOT_EXIST_DESC);
			return result;
		}
		// TODO 验证更新数据是否合法
		if (isVeri) {// 是否做校验
			// 验证昵称
			// String desc = checkNickName(updateUser.getNickName(), curUser);
			EucResult<EucUser> result = checkNickName(updateUser.getNickName(),
					curUser);
			if (result != null
					&& EucResult.BUS_ERROR_CODE == result.getResultCode()) {
				return result;
			}
			// 验证城市
			if (null != updateUser.getCity()
					&& !"".equals(updateUser.getCity())) {
				// 城市可填空
				if (!FormUserValidator.checkCity(updateUser.getCity())) {
					return EucResult.buildBusErrorResult(
							EucErrorConstant.CITY_NOT_EXIST_CODE,
							EucErrorConstant.CITY_NOT_EXIST_DESC);
				}
			}
			// 验证性别
			if (updateUser.getSex() != null) {
				if (!FormUserValidator.checkSex(updateUser.getSex())) {// 验证性别
					return EucResult.buildBusErrorResult(
							EucErrorConstant.SEX_ERROR_CODE,
							EucErrorConstant.SEX_ERROR_DESC);
				}
			}
			// TODO 其他属性验证

		}
		// TODO 更新数据
		// 更新数据库
		boolean isSucc = eucUserService.updateUserById(userId, updateUser);
		// TODO 返回成功结果
		if (isSucc) {// 更新成功
			curUser = eucUserService.queryUserInfoById(userId);
			return EucResult.buildSuccResult(curUser);
		} else {// 更新失败
			return EucResult.buildBusErrorResult(
					EucErrorConstant.UPDATE_ERROR_CODE,
					EucErrorConstant.UPDATE_ERROR_DESC);
		}
	}

	/**
	 * 检测匿称
	 * 
	 * @param nickName
	 * @param userId
	 * @return
	 */
	private <T> EucResult<T> checkNickName(String nickName, EucUser eucUser) {
		if (nickName != null && nickName.equals(eucUser.getNickName())) {// 昵称没做修改
			return EucResult.buildBusErrorResult(
					EucErrorConstant.NICK_NAME_NOT_CHANGE_CODE,
					EucErrorConstant.NICK_NAME_NOT_CHANGE_DESC);
		}
		String s = FormUserValidator.checkNickName(nickName);
		if (!"".equals(s)) {
			return EucResult.buildBusErrorResult(
					EucErrorConstant.NICK_NAME_ERROR_CODE, s);
		} else {
			return null;
		}
	}

}
