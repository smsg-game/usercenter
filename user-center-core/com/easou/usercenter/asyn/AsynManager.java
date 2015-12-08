package com.easou.usercenter.asyn;

/**
 * 异步处理类
 * 
 * @author damon
 * @since 2012.11.15
 * @version 1.0
 * @param <T>
 */
public interface AsynManager<T> {
	
	final static int EVERY_SIZE = 50;
	
	/**
	 * 异步更新数据
	 * 
	 * @param object
	 */
	public void asynUpdate(T object);
	
	/**
	 * 同步数据
	 */
	public void synData();
}
