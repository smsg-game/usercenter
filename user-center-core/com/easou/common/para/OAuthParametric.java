package com.easou.common.para;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.api.EucParserException;
import com.easou.common.api.EucService;
import com.easou.common.api.JBody;
import com.easou.common.api.JHead;
import com.easou.common.api.Md5SignUtil;
import com.easou.common.api.RequestInfo;

/**
 * 外部加密鉴权接口
 * 
 * @author damon
 * @since 2013.01.22
 * @version 1.0
 */
public class OAuthParametric implements AuthParametric<RequestInfo> {

	private static Log LOG = LogFactory.getLog(OAuthParametric.class);

	public JHead getVeriHeader(JBody jBody, EucService service, RequestInfo info) {
		// TODO Auto-generated method stub
		JHead head = service.buildDefaultRequestHeader();
		try {
			String sign = getSign(jBody,service.getKey());
			head.setSign(sign);
			head.setFlowCode(System.currentTimeMillis() + "");	// 流水号
			if(null!=info) {
				if(info.getQn()!=null)
					head.setQn(info.getQn());	//渠道
				if(info.getAppId()!=null)
					head.setAppId(info.getAppId());	//应用/游戏id
				if(info.getSource()!=null)
					head.setSource(info.getSource());		//请求来源
				if(info.getAgent()!=null)
					head.setAgent(info.getAgent());		//客户端agent
			}
		} catch (EucParserException e) {
			LOG.error("produce vericode is error...", e);
		}
		return head;
	}
	
	public String getSign(JBody jBody,String key)throws EucParserException{
		return Md5SignUtil.sign((Map)jBody, key);
	}
}
