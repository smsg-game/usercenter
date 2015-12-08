package com.easou.usercenter.datasource;
/**
 * 数据源集群
 * 
 * @author damon
 * @since 2011.05.10
 * @version 1.0
 *
 */
public enum DataSourceCluster {
	
	/* 默认数据源；
	 * 如果用户中心部署在移动服务器，该数据源为可读写数据源;
	 * 如果是部署电信服务器，该数据源为只读数据源。
	     */
	DEFAULT_DATA_SOURCE,
	/*
	 * 可写数据源；
	 * 此数据源只有在用户中心部署到电信服务器上时存在，为可写数据源
	 */
	WRITE_DATA_SOURCE;
}
