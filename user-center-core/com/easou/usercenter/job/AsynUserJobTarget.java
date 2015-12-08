package com.easou.usercenter.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.asyn.AsynManager;

/**
 * 更新用户信息任务执行类
 * 
 * @author damon
 * @since 2012.11.15
 * @version 1.0
 *
 */
public class AsynUserJobTarget {
	
	private Log LOG = LogFactory.getLog(getClass());
	
	private AsynManager asynUserManager;
	
	/**
	 * 更新数据
	 */
	public void updateUserTarget(){
		LOG.debug("start to asyn user data...");
		//同步数据
		asynUserManager.synData();
		
		LOG.debug("end to asyn user data...");
	
	}

	public AsynManager getAsynUserManager() {
		return asynUserManager;
	}

	public void setAsynUserManager(AsynManager asynUserManager) {
		this.asynUserManager = asynUserManager;
	}
}
