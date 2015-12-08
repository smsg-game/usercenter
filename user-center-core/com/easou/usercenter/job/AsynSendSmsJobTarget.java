package com.easou.usercenter.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.asyn.AsynSendSmsManager;

public class AsynSendSmsJobTarget {
	
	private static Log LOG = LogFactory.getLog(AsynSendSmsJobTarget.class);
	
	private AsynSendSmsManager asynSendSmsManager;
	
	public void sendSms(){
		
		asynSendSmsManager.synSendSms();
		
	}

	public AsynSendSmsManager getAsynSendSmsManager() {
		return asynSendSmsManager;
	}

	public void setAsynSendSmsManager(AsynSendSmsManager asynSendSmsManager) {
		this.asynSendSmsManager = asynSendSmsManager;
	}
	
	

}
