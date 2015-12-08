package com.easou.usercenter.web.apibeans;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;

public class DefaultNotAuthBean extends JBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6931182695364346561L;

	public DefaultNotAuthBean() {
		JHead head = new JHead();
		head.setRet(CodeConstant.NOT_AUTH);
//		head.setVersion("1.0");
		this.setHead(head);
		JDesc jdesc = new JDesc();
		jdesc.add("授权未通过");
		this.setDesc(jdesc);
	}
}