package client;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.cas.client.EucApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.RequestInfo;
import com.easou.usercenter.util.HttpClientUtil;

public class TestApi {
	
	/*public static EucApiResult<JUser> directlyResetMobile(long id,String mobile,RequestInfo info) throws Exception {
		EucApiResult<JUser> apiResult = EucApiCall.directlyBindMobileString(id, mobile,info);
		return apiResult;
	}*/
	
	public static EucApiResult<EucAuthResult> regByRealMobile(String mobile,
			String password, RequestInfo info)
			throws EucParserException {
		return EucApiAuthCall.regByRealMobile(mobile, password, info);
	}
	
	public static void main(String[] args) throws Exception {
		String result = HttpClientUtil.executeUrl("http://testsso.fantingame.com/api2/validate.json","{\"head\":{\"partnerId\":\"1000100010001014\",\"appId\":\"1377\",\"sign\":\"ddc7d7ab42b75700d7d70a6664a48eca\"},\"body\":{\"ticket\":\"ST-19-zv5EmMGPcWEdm6d9kiZj-sso\",\"service\":\"http://html5.boyaa.com/texas/easou/game.html\"}}");
		//String result = HttpClientUtil.executeUrl("http://testsso.fantingame.com/api2/validate.json","{\"body\":{\"EASOUTGC\":\"TGT-61-FiNxjkR11MYsdaMUZxYaIThNJSoiZaPWYxzp96O2pg1wEyQKbq-sso\",\"U\":\"9585929bc3ba56694dd7ac7dbcc1d828f6bcb0e3f4572f5b0416afd762851398260734c38e5851bfa659c1d226c51ae9\"},\"head\":{\"appId\":\"1328\",\"flowCode\":\"1361257331\",\"partnerId\":\"1281\",\"sign\":\"006903244081cb98992089072c9d9918\",\"version\":\"1.0\"}}");
		//System.out.println(result);
	}
}
