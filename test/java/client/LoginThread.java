package client;

import java.util.Random;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;

public class LoginThread extends Thread {
	
	private int count;
	
	private String name;
	
	public LoginThread(String name,int count){
		this.name = name;
		this.count = count;
	}

	@Override
	public void run() {
        System.out.println("start login thread" +name);
		RequestInfo info = new RequestInfo();
		info.setEsid("damontest");
		info.setSource("0");
		info.setUid("damontest");
		String names[] = { "damon0000", "damon60", "damon80", "damon120",
				"damon130" };
		long total = 0;
		try {
			for (int i = 0; i < count * 1; i++) {
				Long start = System.currentTimeMillis();
				double l = 0;
				Random random = new Random();
				EucApiResult<EucAuthResult> result = EucApiAuthCall.login(
						names[i % 5], "528969", "app", info);
				l = (System.currentTimeMillis() - start);
				System.out.println("long time:" + l);
				total += l;
				if (CodeConstant.OK.equals(result.getResultCode())) {
					System.out.println("user:"
							+ result.getResult().getUser().getName());
					System.out.println("------------------");
				}
			}
		} catch (EucParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end login thread" +name);

	}

}
