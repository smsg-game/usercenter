package com.easou.cas.sdk;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.easou.cas.auth.EucAuthResult;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.usercenter.client.api.JUnitEucApiCallAction;

public class EucRegistCallTest extends JUnitEucApiCallAction {
	
private static final Log LOG = LogFactory.getLog(EucUserCallTest.class);
	
	@Test
	public void testAutoRegist()throws EucParserException {
		EucApiResult<EucAuthResult> result = EucRegistCall.autoRegist(true);
		if(CodeConstant.OK.equals(result.getResultCode())){
			Assert.assertTrue("登录成功", true);
		}else{
			Assert.assertFalse("登录失败", true);
		}
		
	}

}
