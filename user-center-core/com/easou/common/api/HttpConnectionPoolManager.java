package com.easou.common.api;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * HTTP客户端连接池
 * 
 * @author damon
 * @since 2012.10.16
 * @version 1.0
 * 
 */
public class HttpConnectionPoolManager {

	private static Log LOG = LogFactory.getLog(HttpConnectionPoolManager.class);

	/** 配置参数 */
	private static HttpParams httpParams;
	/** 连接池 */
	private static PoolingClientConnectionManager connectionManager;
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 800;
	/**
	 * 获取连接的最大等待时间
	 */
	// public final static int WAIT_TIMEOUT = 60000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 400;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 10000;
	/**
	 * 读取超时时间
	 */
	public final static int READ_TIMEOUT = 10000;
	/** 请求管理类 */
	public final static HttpClient httpClient;

	private static SSLSocketFactory getTrustFactory() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			return new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {
			return null;
		}
	}

	static {
		// 最大连接数
		int maxTotal = MAX_TOTAL_CONNECTIONS;
		if (ClientConfig.getProperty("http.pool.maxTotal") != null) {
			maxTotal = Integer.valueOf(ClientConfig.getProperty("http.pool.maxTotal"));
		}
		// 最大路由数
		int maxPerRoute = MAX_ROUTE_CONNECTIONS;
		if (ClientConfig.getProperty("http.pool.maxPerRoute") != null) {
			maxPerRoute = Integer.valueOf(ClientConfig.getProperty("http.pool.maxPerRoute"));
		}
		// 套接字超时时间
		int socketTimeout = READ_TIMEOUT;
		if (ClientConfig.getProperty("http.socket.timeout") != null) {
			socketTimeout = Integer.valueOf(ClientConfig.getProperty("http.socket.timeout"));
		}
		// 设置连接超时时间
		int connectTimeout = CONNECT_TIMEOUT;
		if (ClientConfig.getProperty("http.connection.timeout") != null) {
			connectTimeout = Integer.valueOf(ClientConfig.getProperty("http.connection.timeout"));
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("http pool maxTotal:" + maxTotal);
			LOG.debug("http pool maxPerRoute:" + maxPerRoute);
			LOG.debug("http socket timeout:" + socketTimeout);
			LOG.debug("http.connection.timeout:" + connectTimeout);
		}
		
		httpParams = new BasicHttpParams();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, getTrustFactory()));
		connectionManager = new PoolingClientConnectionManager(schemeRegistry);
		
		// 设置最大连接数
		connectionManager.setMaxTotal(maxTotal);
		connectionManager.setDefaultMaxPerRoute(maxPerRoute);
		
		connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);

		// 设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, connectTimeout);
		// 设置读取超时时间
		HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
		
		// 初始化对象
		httpClient = new DefaultHttpClient(connectionManager, httpParams);

	}

	public static HttpClient getHttpClient() {
		return httpClient;
	}

}