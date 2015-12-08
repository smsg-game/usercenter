package com.easou.usercenter.asyn.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;

import com.easou.usercenter.asyn.AsynCache;
import com.easou.usercenter.asyn.AsynCacheName;
import com.easou.usercenter.asyn.AsynSendSmsManager;
import com.easou.usercenter.entity.Sms;
import com.easou.usercenter.util.SendMessageUtil;

public class AsynSendSmsManagerImpl implements AsynSendSmsManager {

	private static Log LOG = LogFactory.getLog(AsynSendSmsManagerImpl.class);

	private static AsynCache<Sms> cache = new AsynCache<Sms>(
			AsynCacheName.SMS_CACHE);

	@Override
	@Async
	public void asynSendSms(Sms sms) {
		/*if (LOG.isDebugEnabled()) {
			 LOG.debug("asyn send sms["+sms.getContent()+
			 "] to mobile["+sms.getMobile()+"],sms create time["+sms.getCreateDate()+"]");
		}*/
		//cache.offer(sms);
		boolean success = SendMessageUtil.send(sms.getUid(),
				sms.getMobile(), sms.getContent(),sms.getContentLog(), sms.getChannel(),
				sms.getEsid(), sms.getType());
		
	}

	@Override
	public void synSendSms() {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("start asyn send sms...");
//		}
		// 计数
		int count = 0;
		// 成功
		int sucCount = 0;
		// 失败
		int failCount = 0;
		// 最大响应时间
		long maxDelayTime = 0;
		// 最小响应时间
		long minDelayTime = 0;
		// 总延迟时间
		long totalDelayTime = 0;

		while (!cache.isEmpty()) {
			Sms sms = cache.poll();
			boolean success = SendMessageUtil.send(sms.getUid(),
					sms.getMobile(), sms.getContent(),sms.getContentLog(), sms.getChannel(),
					sms.getEsid(), sms.getType());
			if (LOG.isDebugEnabled()) {
				long time = System.currentTimeMillis()
						- sms.getCreateDate().getTime();
				// 总延迟时间
				totalDelayTime = totalDelayTime + time;
				if (maxDelayTime < time) {// 最大延迟
					maxDelayTime = time;
				}
				if (minDelayTime > time||minDelayTime==0) {// 最小延迟
					minDelayTime = time;
				}
			}
			if (success) {
				sucCount++;
			} else {
				failCount++;
			}
			count++;
		}
//		if (LOG.isDebugEnabled()) {
//			long avgDelayTime = 0;
//			if (count > 0) {
//				avgDelayTime = totalDelayTime / count;
//			}
//			LOG.debug("send sms count[" + count + "],aveDelayTime["
//					+ avgDelayTime + "],maxDelayTime[" + maxDelayTime
//					+ "],minDelayTime[" + minDelayTime + "]");
//		}
//		if (count > 0) {
//			LOG.info("asyn end, send sms totalCount[" + count + "],sucCount["
//					+ sucCount + "],failCount[" + failCount + "]");
//		}
	}

}
