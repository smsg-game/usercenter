package com.easou.usercenter.socket;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.easou.usercenter.config.SSOConfig;

public class MClient {
	private static Logger logger = Logger.getLogger(MClient.class);

	private static String HOST;	// 服务主机地址

	private static int PORT;	// 服务主机端口
	
	private String command = "";
	
	private String result;
	
	public MClient(String command) {
		this.command=command;
	}
	
	public static void main(String[] args) {
		MClient client = new MClient("conf");
		client.execute();
		System.out.println(client.getResult());
	}

	public void execute() {
		if(null!=SSOConfig.getProperty("monitor.server")) {
			HOST = SSOConfig.getProperty("monitor.server");
		}
		if (null != SSOConfig.getProperty("monitor.port")) {
			PORT = Integer.parseInt(SSOConfig.getProperty("monitor.port"));
		}
		if(null == HOST || 0 == PORT) {
			// 没设定监控主机，不进行监控
			return;
		}
		// 创建一个非阻塞的客户端程序
		IoConnector connector = new NioSocketConnector();
		// 设置链接超时时间
		connector.setConnectTimeout(10000);
		// 设置过滤器
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		MClientHandler handler = new MClientHandler();
		
		// 添加业务逻辑处理器类
		connector.setHandler(handler);
		IoSession session = null;
		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(
					HOST, PORT));// 创建连接
			future.awaitUninterruptibly();// 等待连接创建完成
			session = future.getSession();// 获得session
			
			session.write(command);// 发送给移动服务端
		} catch (Exception e) {
			logger.error("客户端链接异常...");
		}
		if(null!=session) {
			session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
		}
		connector.dispose();	// 关闭连接
		result = handler.getResult();
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getResult() {
		return result;
	}
}
