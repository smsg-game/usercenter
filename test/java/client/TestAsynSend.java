package client;

import com.easou.usercenter.asyn.AsynSendSmsManager;
import com.easou.usercenter.asyn.impl.AsynSendSmsManagerImpl;
import com.easou.usercenter.entity.Sms;

public class TestAsynSend {

	public static void main(String[] mrgs) {
		OfferThread o1= new OfferThread("o1",1);
		OfferThread o2= new OfferThread("o2",1);
		OfferThread o3= new OfferThread("o3",1);
		OfferThread o4= new OfferThread("o4",1);
		PollThread p1 = new PollThread("p1");
		o1.start();
		o2.start();
		o3.start();
		o4.start();
		p1.start();
		
	}

}

/**
 * 入栈
 * 
 * @author damon
 *
 */
class OfferThread extends Thread {
	
	private AsynSendSmsManager asynSendSmsManager = new AsynSendSmsManagerImpl();
	
	private int count;
	
	private String name;
	
	protected OfferThread(String name,int count){
		this.name = name;
		this.count = count;
	}

	public void run() {
		 System.out.println("start thread:"+name);
         Sms sms = new Sms();
         sms.setMobile("15002080824");
         for(int i=0;i<count;i++){
             asynSendSmsManager.asynSendSms(sms);
         }
         System.out.println("end thread:"+name);
	}
}

/**
 * 出栈
 * 
 * @author damon
 *
 */
class PollThread extends Thread {
	
	private AsynSendSmsManager asynSendSmsManager = new AsynSendSmsManagerImpl();
	
	private String name;
	
	protected PollThread(String name){
		this.name = name;
	}
	
	public void run() {
		try {
			sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = System.currentTimeMillis();
		System.out.println("start thread:"+name);
		asynSendSmsManager.synSendSms();
		System.out.println("end thread:"+name+"");
	}
}
