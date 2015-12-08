package com.easou.usercenter.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.easou.common.api.EucParserException;
import com.easou.usercenter.config.SSOConfig;

/**
 * 日志对象
 * 
 * @author damon
 * @since 2012.07.31
 * @version 1.0
 */
public class LogBuffer {

	/**
	 * 日志字段分格符
	 */
	private final static String LOG_SPLIT = "{]";

	private static DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//	/** 日志版本号 */
//	private static String version = null;

	/**
	 * 日志内容
	 */
	StringBuffer buffer;
	/**
	 * 日志内容大侠
	 */
	LogSize size;
	/**
	 * 实际日志大小
	 */
	int length;
//
//	/**
//	 * 获取版本号
//	 * 
//	 * @return
//	 */
//	private static String getVersion() {
//		if (version == null) {
//			if (SSOConfig.getProperty("log.version") != null) {
//				version = SSOConfig.getProperty("log.version");
//			} else {
//				version = "1.0.0";
//			}
//		}
//		return version;
//	}

	@SuppressWarnings("unused")
	private LogBuffer() {

	}

	protected StringBuffer getBuffer() {
		return buffer;
	}

	protected void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}

	protected LogSize getSize() {
		return size;
	}

	protected void setSize(LogSize size) {
		this.size = size;
	}

	protected int getLength() {
		return length;
	}

	protected void setLength(int length) {
		this.length = length;
	}

//	protected static void setVersion(String version) {
//		LogBuffer.version = version;
//	}

	/**
	 * 构造函数
	 * 
	 * @param length
	 */
	protected LogBuffer(LogSize size) {
		this.size = size;
		this.length = 0;
		this.buffer = new StringBuffer();
		try {
			// 增加日志日期
			this.append(" "+getCurrentLogDate());
			// 增加日志版本
//			this.append(getVersion());
		} catch (EucParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加日志
	 * 
	 * @param str
	 * @return
	 */
	public LogBuffer append(String str) throws EucParserException {
		int count = ++this.length;
		if (count < size.length) {// 判断日志长度是否超出
			this.buffer.append(str).append(LOG_SPLIT);
			return this;
		} else if (count == size.length) {
			this.buffer.append(str);
			return this;
		}
		throw new EucParserException("log'length is greater than log'size["
				+ size.length + "]");
	}

	/**
	 * 追加日志
	 * 
	 * @param i
	 * @return
	 */
	public LogBuffer append(int i) throws EucParserException {
		return this.append(String.valueOf(i));
	}

	/**
	 * 格式化日志
	 * 
	 * @return
	 */
	public String formatLog() {
		int differ = size.length - length;
		try {
			while (differ-- > 1) {
				this.append("");
			}
		} catch (EucParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.buffer.toString();
	}

	/**
	 * 获取当前日志记录时间字符串
	 * 
	 * @return
	 */
	private static String getCurrentLogDate() {

		return format.format(new Date());
	}

	/**
	 * test
	 * 
	 * @param mrgs
	 */
	public static void main(String[] mrgs) {
		LogBuffer logBuffer = new LogBuffer(LogSize.M_SIZE);
		for (int i = 1; i <= 5; i++) {
			try {
				logBuffer.append(i);
			} catch (EucParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(LogBuffer.getCurrentLogDate());
		System.out.println(logBuffer.formatLog());
	}

}
