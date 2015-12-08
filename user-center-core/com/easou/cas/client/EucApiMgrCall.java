package com.easou.cas.client;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.RequestInfo;
import com.easou.common.para.AuthParametric;
import com.easou.common.para.InAuthParametric;

/**
 * 接口调用
 * 
 * @author jay
 * @since 2012.09.18
 */
public class EucApiMgrCall {

	protected static EucService eucService = EucService.getInstance();
	
	private static AuthParametric<RequestInfo> authPara = new InAuthParametric();

	/**
	 * 重置密码接口，新密码由服务端控制
	 * 
	 * @param id
	 * @return
	 * @throws EucParserException
	 */
	public static EucApiResult<Boolean> resetPass(Long id, RequestInfo info)
			throws EucParserException {
		JBody jbody = new JBody();
		jbody.putContent("id", id);
		JBean jbean = eucService.getResult("/api/resetPasswd", jbody,authPara, info);
		EucApiResult<Boolean> result = new EucApiResult<Boolean>(jbean);
		if(result.getResultCode().equals(CodeConstant.OK)) {
			result.setResult(true);
		} else {
			result.setResult(false);
		}
		return result;
	}
}
