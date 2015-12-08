package com.easou.usercenter.web.apibeans;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;

public class DefaultBean extends JBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5288398056679386512L;

	public DefaultBean() {
		JHead head = new JHead();
		// head.setVersion("1.0");
		head.setRet(CodeConstant.OK);
		JDesc desc = new JDesc();
		this.setHead(head);
		this.setDesc(desc);
	}
}
