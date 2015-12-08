package com.easou.usercenter.web.apibeans;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;

public class DefaultErrorBean extends JBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1568986494491990706L;

	public DefaultErrorBean() {
		JHead head = new JHead();
		head.setRet(CodeConstant.ERROR);
//		head.setVersion("1.0");
		this.setHead(head);
		JDesc jdesc = new JDesc();
		jdesc.add("系统错误");
		this.setDesc(jdesc);
	}
}
