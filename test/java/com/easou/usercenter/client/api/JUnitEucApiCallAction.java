package com.easou.usercenter.client.api;

import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.cas.client.EucApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;
import com.easou.usercenter.web.JUnitActionBase;

public class JUnitEucApiCallAction extends JUnitActionBase{
	

	private static final Log LOG = LogFactory.getLog(JUnitEucApiCallAction.class);
	/** 登录用户名 */
	protected static final String LOGIN_MOBILE = "13424319753";
	/** 密码 */
	protected static final String LOGIN_MOBILE_PASS_WD = "111111";
	/**注册手机号，如果手机号被注册过需要更改*/
	protected static final String REGISTER_MOBILE="15002080805";
	/**注册用户名，如果用户名被注册过需要更改*/
	protected static final String REGISTER_NAME="damon000000";
	

	protected RequestInfo getRequestInfo() {

		RequestInfo info = new RequestInfo();
		info.setEsid("");
		info.setUid("");
		info.setSource("test");

		return info;
	}

	/**
	 * 判断返回是否正确
	 * 
	 * @param result
	 * @return
	 */
	protected <T> boolean isReturnSuc(EucApiResult<T> result) {
		if (!CodeConstant.OK.equals(result.getResultCode())
				|| (result.getDescList() != null && result.getDescList().size() > 0)) {// 失败
			return false;
		} else {// 成功
			return true;
		}
	}

	/**
	 * 返回错误描述信息
	 * 
	 * @param result
	 * @return
	 */
	protected <T> String getErrorDesc(EucApiResult<T> result) {
		if(CodeConstant.NOT_AUTH.equals(result.getResultCode())){
			return "无调用接口的权限";
		}
		return result.getDescList() != null? result.getDescList().get(0)
				.getD() : "无异常信息";
	}
	
	/**
	 * 产生随机注册名
	 * 
	 * @return
	 */
	protected String proRandomName(){
		Random rand = new Random();
		int i = rand.nextInt(); //int范围类的随机数
		i = rand.nextInt(1000000); //生成0-100以内的随机数
		String userName = REGISTER_NAME+i;
		//TODO 验证是否注册过
		
		return userName;
	}
	
	/**
	 * 产生随机手机号
	 * 
	 * @return
	 */
    protected String proRandomMobile()throws Exception {
		//TODO 产生随机手机号
    	long rMobile = Long.valueOf(REGISTER_MOBILE);
    	boolean succ = false;
    	Random rand = new Random();
    	while(!succ){
    		int i = rand.nextInt(10000000); //生成0-100以内的随机数
    		long temp = rMobile+i;
    		EucApiResult<JUser> result = EucApiCall.getUserbyMobile(String.valueOf(temp), getRequestInfo());
    	    if(!isReturnSuc(result)){
    	    	succ = true;  
    	    	rMobile = temp;
    	    }
    	}
		//TODO 验证手机号是否已经被注册
		return String.valueOf(rMobile);
	}
	
	protected EucAuthResult login()throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		//TODO 登录
		EucApiResult<EucAuthResult> result = EucApiAuthCall.login(request,
				response, LOGIN_MOBILE, LOGIN_MOBILE_PASS_WD, true,
				getRequestInfo());
		if (CodeConstant.OK.equals(result.getResultCode())){
			Assert.assertTrue("登录验证",result.getResult()!=null);
		}else{
			Assert.assertFalse("登录验证失败【"+getErrorDesc(result)+"】",true);
		}
		return result.getResult();
	}
	
	/**
	 * 用户名|手机号/密码登录
	 * 
	 * @param userName
	 *     用户名|手机号
	 * @param passwd
	 *     密码
	 * @return
	 * @throws Exception
	 */
	protected EucAuthResult login(String userName,String passwd)throws Exception {
		EucApiResult<EucAuthResult> result = 
				EucApiAuthCall.login(userName, passwd, "app", getRequestInfo());
		if(isReturnSuc(result)){
			Assert.assertTrue("用户名|手机号/密码登录",result.getResult()!=null);
		}else{
			Assert.assertFalse("用户名|手机号/密码登录【"+getErrorDesc(result)+"】",true);
		}
		return result.getResult();
		
	}
}
