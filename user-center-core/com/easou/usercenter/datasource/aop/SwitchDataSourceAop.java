package com.easou.usercenter.datasource.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.easou.common.constant.Server;
import com.easou.usercenter.datasource.ContextHolder;
import com.easou.usercenter.datasource.DataSourceCluster;

/**
 * 实现在不同数据源之间的切换操作
 * 
 * @author damon
 * @since 2012.05.10
 * @version 1.0
 * 
 */
public class SwitchDataSourceAop {

	private static Log LOG = LogFactory.getLog(SwitchDataSourceAop.class);

	// 服务器类型
	public String serverType;

	/**
	 * 执行持久化操作前
	 * 
	 * @param joinPoint
	 */
	public void doBefore(JoinPoint joinPoint) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("method[" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "]before visit dataBase[" + ContextHolder.getContext() + "],and server type[" + serverType + "]");
		}
		// 如果为默认服务器，则不进行数据源切换
		if (Server.DEFAULT == Server.getServerByType(serverType)) {
			return;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("before visit dataBase,switch the dataBase from [" + ContextHolder.getContext() + "] to [" + getDataSourceByServer(serverType) + "]");
		}
		ContextHolder.setContext(getDataSourceByServer(serverType));

	}

	/**
	 * 执行持久化操作后
	 * 
	 * @param joinPoint
	 */
	public void doAfter(JoinPoint joinPoint) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("method[" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "]after visit dataBase[" + ContextHolder.getContext() + "],and server type[" + serverType + "]");
		}
		// 如果为默认服务器，则不进行数据源切换
		if (Server.DEFAULT == Server.getServerByType(serverType)) {
			return;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("after visit dataBase,switch the dataBase from [" + ContextHolder.getContext() + "] to [" + DataSourceCluster.DEFAULT_DATA_SOURCE + "]");
		}
		// 返回默认的数据源
		ContextHolder.setContext(DataSourceCluster.DEFAULT_DATA_SOURCE);
	}

	public void doThrowing(JoinPoint joinPoint, Throwable ex) {
		LOG.error("method[" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "] throw exception");
		if (LOG.isDebugEnabled()) {
			LOG.debug("throwing exception,switch the dataBase from [" + ContextHolder.getContext() + "] to [" + DataSourceCluster.DEFAULT_DATA_SOURCE + "]");
		}
		LOG.error(ex.getMessage());
		// 如果为默认服务器，则不进行数据源切换
		if (Server.DEFAULT == Server.getServerByType(serverType)) {
			return;
		}
		ContextHolder.setContext(DataSourceCluster.DEFAULT_DATA_SOURCE);
	}

	/**
	 * 根据服务器类型获取数据源
	 * 
	 * @param serverType
	 * @return
	 */
	private DataSourceCluster getDataSourceByServer(String serverType) {
		// 电信服务器
		if (Server.DIANXIN == Server.getServerByType(serverType)) {
			return DataSourceCluster.WRITE_DATA_SOURCE;
		}
		return DataSourceCluster.DEFAULT_DATA_SOURCE;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

}
