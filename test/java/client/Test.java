package client;

import com.alibaba.fastjson.JSON;
import com.easou.usercenter.entity.EucUser;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*String t="json=%7B%22body%22%3A%7B%22id%22%3A%2210031052%22%7D%2C%22head%22%3A%7B%22flowCode%22%3A%221350385834%22%2C%22from%22%3A%22test%22%2C%22veriCode%22%3A%225186b664a9774daf7ea987ceb00996f5%22%2C%22version%22%3A%221.0%22%7D%7D";
		if(t.indexOf("json=")!=-1){
			System.out.println(t.indexOf("json="));
		}
		Actions a=new Actions();
		//Login l = new Login();
		try {
			a.action();
			//l.login();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*SendThread t1 = new SendThread("t1",10);
		SendThread t2 = new SendThread("t2",10);
		SendThread t3 = new SendThread("t3",10);
		SendThread t4 = new SendThread("t4",10);
		t1.start();
		t2.start();
		t3.start();
		t4.start();*/
		/*LoginThread l1 = new LoginThread("l1",1);
		LoginThread l2 = new LoginThread("l2",1);
		LoginThread l3 = new LoginThread("l3",1);
		LoginThread l4 = new LoginThread("l4",1);
		LoginThread l5 = new LoginThread("l5",1);
		LoginThread l6 = new LoginThread("l6",1);
		l1.start();
		l2.start();
		l3.start();
		l4.start();
		l5.start();
		l6.start();*/
		
		EucUser user = new EucUser();
		user.setId(Long.valueOf(1));
		user.setAnswer(null);
		user.setName("");
		System.out.println(JSON.toJSONString(user));
		
	}
}
