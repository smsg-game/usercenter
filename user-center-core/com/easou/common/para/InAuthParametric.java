package com.easou.common.para;

import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.JBody;
import com.easou.common.api.JHead;
import com.easou.common.api.RequestInfo;

/**
 * 内部接口鉴权
 * 
 * @author damon
 * @since 2013.01.22
 * @version 1.0
 */
public class InAuthParametric implements AuthParametric<RequestInfo>{

	@Override
	public JHead getVeriHeader(JBody jBody, EucService service,RequestInfo info) {
		JHead head = service.buildDefaultRequestHeader();
		if(null!=info) {
			if(info.getQn()!=null)
				head.setQn(info.getQn());
			if(info.getAppId()!=null)
				head.setAppId(info.getAppId());
		}
		head.setFlowCode(System.currentTimeMillis() + "");
		return head;
	}
	
	/**
	 * 生成签名密文
	 * 
	 * @param paraMap
	 * @return
	 */
	public String getSign(JBody jBody,String key)throws EucParserException{
		throw new EucParserException("no execute this method...");
	}

}
