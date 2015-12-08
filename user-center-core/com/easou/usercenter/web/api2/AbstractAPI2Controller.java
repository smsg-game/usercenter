package com.easou.usercenter.web.api2;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JDesc;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.validation.FormUserValidator;

abstract class AbstractAPI2Controller extends BaseController {
	
	/**
	 * 返回业务错误
	 */
	protected JBean busiErrorBean(JBean jbean, String code, String msg) {
		jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
		if(null!=jbean.getDesc()) {
			jbean.getDesc().addReason(code, msg);
		} else {
			JDesc jdesc=new JDesc();
			jdesc.addReason(code, msg);
			jbean.setDesc(jdesc);
		}
		return jbean;
	}

	protected boolean checkBirthday(Integer birthday, EucUser eucUser, JDesc jdesc) {
		if (null == birthday || birthday.equals(eucUser.getBirthday())) {
			return false;
		}
		String s = FormUserValidator.checkBirthDay(birthday);
		if (!"".equals(s)) {
			jdesc.addReason("6", s);
			return false;
		}
		return true;
	}

	protected boolean checkSex(Integer sex, EucUser eucUser, JDesc jdesc) {
		if (null == sex || sex.equals(eucUser.getSex())) {
			return false;
		}
		if (!FormUserValidator.checkSex(sex)) {
			jdesc.addReason("7", "性别输入错误");
			return false;
		}
		return true;
	}

	protected boolean checkCity(String city, EucUser eucUser, JDesc jdesc) {
		if (null == city || city.equals(eucUser.getCity())) {
			return false;
		}
		if (!FormUserValidator.checkCity(city)) {
			jdesc.addReason("8", "城市输入错误");
			return false;
		}
		return true;
	}

	protected boolean checkOccuId(Integer occuId, EucUser eucUser, JDesc jdesc) {
		if (null == occuId || occuId.equals(eucUser.getOccuId())) {
			return false;
		}
		if (!FormUserValidator.checkOccupation(occuId)) {
			jdesc.addReason("10", "职业代号错误");
			return false;
		}
		return true;
	}

}
