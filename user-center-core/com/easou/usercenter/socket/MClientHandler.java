package com.easou.usercenter.socket;

import org.apache.log4j.Logger;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

public class MClientHandler extends IoHandlerAdapter {
	private static Logger logger = Logger.getLogger(MClientHandler.class);
	
	private String result;

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String msg = message.toString();
		if(logger.isDebugEnabled()) {
			logger.debug("客户端接收到的信息为：" + msg);
		}
		result = msg;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("客户端发生异常...", cause);
	}
	
	public String getResult() {
		return result;
	}
}
