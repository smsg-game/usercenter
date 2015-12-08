package client;

import com.easou.cas.client.EucApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.usercenter.entity.Sms;

public class SendThread extends Thread{
	
	int count = 10;
	
	String name;
	
	public SendThread(String name,int count){
		this.name = name;
		this.count = count;
	}

	@Override
	public void run() {
		 
		    int sucCount=0;
		    for(int i=0;i<count;i++){
		    	Sms sms = new Sms();
		    	sms.setContent("您好："+i);
		    	sms.setMobile("15002080824");    	
			    try {
			    	long start = System.currentTimeMillis();
					EucApiResult<String> result = EucApiCall.requestResetPass("15002080824", true, null);
					System.out.println("thread["+name+"] response time:"+(System.currentTimeMillis()-start));
					if(CodeConstant.OK.equals(result.getResultCode())){
						sucCount++;
					}
					System.out.println("success send sms:"+sucCount);
				} catch (EucParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }

			System.out.println(sucCount);
	}
	

}
