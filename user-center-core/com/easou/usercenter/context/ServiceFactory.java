package com.easou.usercenter.context;

import org.jasig.cas.CentralAuthenticationService;

import com.easou.usercenter.asyn.AsynSendSmsManager;
import com.easou.usercenter.service.AuthVerifyService;
import com.easou.usercenter.service.EucAppService;
/**
 * 提供给不依赖SPRING容器生成的对象（如直接new），但又需要要调用spring容器里面的对象
 * 
 * @since 2012.07.05
 * @author damon
 * @version 1.0
 *
 */
public class ServiceFactory {
	
	private AuthVerifyService authVerifyService;
	
	private AsynSendSmsManager asynSendSmsManager;
	
	private EucAppService eucAppService;
	
	private CentralAuthenticationService centralAuthenticationService;
	
	private static ServiceFactory instance;
	
	public static ServiceFactory getInstance(){
		if(instance==null){
			instance = new ServiceFactory();
		}
		return instance;
	}
	
	public AuthVerifyService getAuthVerifyService(){
		if(authVerifyService==null){
			authVerifyService = (AuthVerifyService)SpringContext.getContext().getBean("authVerifyService");
		}
		return authVerifyService;
	}
	
	public AsynSendSmsManager getAsynSendSmsManager(){
		if(asynSendSmsManager==null){
			asynSendSmsManager = (AsynSendSmsManager)SpringContext.getContext().getBean("asynSendSmsManager");
		}
		return asynSendSmsManager;
	}
	
	public EucAppService getEucAppService() {
		if(eucAppService==null) {
			eucAppService = (EucAppService)SpringContext.getContext().getBean("eucAppService");
		}
		return eucAppService;
	}
	
	public CentralAuthenticationService getCentralAuthenticationService(){
		if(centralAuthenticationService==null) {
			centralAuthenticationService = (CentralAuthenticationService)SpringContext.getContext().getBean("centralAuthenticationService");
		}
		return centralAuthenticationService;
	}
	
	

}
