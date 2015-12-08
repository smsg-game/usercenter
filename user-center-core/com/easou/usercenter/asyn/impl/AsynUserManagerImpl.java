package com.easou.usercenter.asyn.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.asyn.AsynCache;
import com.easou.usercenter.asyn.AsynCacheName;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;

public class AsynUserManagerImpl implements AsynManager<EucUser>{
	
	private static Log LOG = LogFactory.getLog(AsynUserManagerImpl.class);
	/**批量大小*/
	private static int BATCH_SIZE=50;
	
	private static AsynCache<EucUser> cache = new AsynCache(AsynCacheName.USER_CACHE);
	
	//private static int totalCount = 0;
	
    /**
     * 如果部署在只读服务器上，必须跨机房写入
     */
	private EucUserService eucUserService;
	
	@Override
	public void asynUpdate(EucUser object) {
		if(LOG.isDebugEnabled()){
			LOG.debug("asyn update user["+object.getId()+
					"],update login time["+object.getLastLoginTime()+"]");
		}
		cache.offer(object);
	}

	@Override
	public void synData() {
		int count = 0;
		//缓存不为空
		List<EucUser> tempList = new ArrayList();
		while(!cache.isEmpty()){
			//TODO 可做批量更新
			//List<EucUser> tempList = cache.poll(EVERY_SIZE);
			EucUser user = cache.poll();
			tempList.add(user);
			count++;
			//分批提交
			if((count>0&&count%BATCH_SIZE==0)||cache.isEmpty()){
				eucUserService.batchUpdateUser(tempList, false);
				tempList = new ArrayList();
			}
			if(LOG.isDebugEnabled()){
				LOG.debug("syn update user["+user.getId()+"]");
			}
		}
		if (count > 0) {
		   LOG.info("update user size["+count+"]...");
		}
		//totalCount = totalCount+count;
		//LOG.error("update user totalCount["+totalCount+"]...");
	}

	public EucUserService getEucUserService() {
		return eucUserService;
	}

	public void setEucUserService(EucUserService eucUserService) {
		this.eucUserService = eucUserService;
	}
}
