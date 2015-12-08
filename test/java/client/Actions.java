package client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.easou.cas.client.EucApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;

public class Actions {

	public int init() throws Throwable {
		return 0;
	}// end of init

	public int action() throws Throwable {
		System.out.println("start test...");
		RequestInfo info = new RequestInfo();
		info.setEsid("damontest");
		info.setSource("0");
		info.setUid("damontest");
		Map paraMap = new HashMap();
		paraMap.put("time0", System.currentTimeMillis());
//		info.setParaMap(paraMap);
		//EucApiResult<EucAuthResult> result = EucApiAuthCall.login("damon00",
		// "123456", "app", info);
		int ids[]={10031050,10031051,10031052,10031068,10031093,
				10031096,20000096,10031048,10031049,10031250};
		Random random = new Random();
		int id = ids[random.nextInt(10)];
		long total = 0;
		int count = 10;
			Long start = System.currentTimeMillis();
			double l = 0;
			EucApiResult<JUser> result = EucApiCall.getUserbyId(id, info);
			l = (System.currentTimeMillis() - start);
			System.out.println("long time:"
					+ l);
			total += l;
			if (CodeConstant.OK.equals(result.getResultCode())) {
				// System.out.println("token:"+result.getResult().getToken().getToken());
				System.out.println("user:" + result.getResult().getName());
				// System.out.println("user:"+result.getResult().getUser().getId());
			}
		System.out.println("评均："+(total/count)*0.001);
		
		return 0;
	}// end of action

	public int end() throws Throwable {
		return 0;
	}// end of end

}
