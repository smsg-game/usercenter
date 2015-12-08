package com.easou.usercenter.web.apibeans;

import com.easou.common.api.JBean;

public class JSonBeanFactory {

	public static JBean getDefaultBean() {
		return new DefaultBean();
	}

	public static JBean getDefaultNotAuthBean() {
		return new DefaultNotAuthBean();
	}

	public static JBean getDefaultErrorBean() {
		return new DefaultErrorBean();
	}
}