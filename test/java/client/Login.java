package client;

import java.util.Random;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;

public class Login {
	
	public void login() throws EucParserException{
		System.out.println("start test...");
		RequestInfo info = new RequestInfo();
		info.setEsid("damontest");
		info.setSource("0");
		info.setUid("damontest");
		//String names[] = {"damon580","damon590","damon660","damon670","damon680",
		//		"damon510","damon530","damon540","damon550","damon560"};
		//String names[] = {"damon00","damon40","damon50","damon30","damon130",
		//		"damon360","damon100","damon110","damon0000","damon60",
		//		"damon70","damon80","damon90","damon110","damon120",
		//		"damon130","damon140","damon150","damon160","damon170"};
		//EucApiResult<EucAuthResult> result = EucApiAuthCall.login("damon00",
		// "123456", "app", info);
		//int ids[]={10031050,10031051,10031052,10031068,10031093,
		//		10031096,20000096,10031048,10031049,10031250};
		String names[] = {"damon0000","damon60","damon80","damon120","damon130"};
		long total = 0;
		int count = 19;
		for (int i = 0; i < count*1; i++) {
			Long start = System.currentTimeMillis();
			double l = 0;
			Random random = new Random();
			//String name = names[random.nextInt(10)-1];
			//String name = names[i];
			EucApiResult<EucAuthResult> result = EucApiAuthCall.login(names[i%5],
					 "528969", "app", info);
			//EucApiResult<JUser> result = EucApiCall.getUserbyId(ids[i], info);
			l = (System.currentTimeMillis() - start);
			System.out.println("long time:"
					+ l);
			total += l;
			if (CodeConstant.OK.equals(result.getResultCode())) {
				// System.out.println("token:"+result.getResult().getToken().getToken());
				System.out.println("user:" +result.getResult().getUser().getName());
				// System.out.println("user:"+result.getResult().getUser().getId());
				System.out.println("------------------");
			}
		}
		for(int i=0;i<1000;i++){
			Random random = new Random();
			int m = random.nextInt(15);
			if(m>16||m<0){
			    System.out.println(m);
			}else if(m==0){
				System.out.print(m);
			}
		}
		//System.out.println("评均："+(total/count)*0.001);
	}

}
