package com.easou.usercenter.cache;

import java.util.Map;

public interface Cache {
	/**
	 * 添加一个对象对缓存,如果值存在则替换,如果不存在则添加(永不失效)
	 * 
	 * @param key
	 *            缓存主键
	 * @param value
	 *            缓存值
	 * @return
	 */
	public boolean set(String key, Object value);

	/**
	 * 添加一个对象对缓存,如果值存在则替换,如果不存在则添加,在expire所指定的时间内失效
	 * 
	 * @param key
	 *            缓存主键
	 * @param value
	 *            缓存值
	 * @param expire
	 *            失效时间(单位: 秒)
	 * @return
	 */
	public boolean set(String key, Object value, int expire);

	/**
	 * 将多个值放入集合中
	 * 
	 * @param el
	 *            需要放入缓存的数据信息
	 * @return
	 */
	public boolean mulSet(Map<String, Object> el);

	/**
	 * 从缓存中获取指定对像
	 * 
	 * @param key
	 *            缓存主键
	 * @param objCla
	 *            数据对象类型
	 * @return
	 */
	public <T extends Object> T get(String key, Class<T> objCla);

	/**
	 * 批量获取数据
	 * 
	 * @param key
	 *            缓存key值
	 * @param cla
	 *            对象数据类型
	 * @return
	 */
	public Map mget(String[] key, Map<String, Class> cla);

	/**
	 * 判定指定key值的缓存数据是否存在
	 * 
	 * @param key
	 *            缓存key
	 * @return
	 */
	public boolean isExist(String key);

	/**
	 * 从缓存中删除指定key值下的数据
	 * 
	 * @param key
	 *            缓存key值
	 * @return
	 */
	public boolean del(String key);

	/**
	 * 同时从缓存中删除多个数据
	 * 
	 * @param key
	 *            缓存key值
	 * @return
	 */
	public boolean mdel(String... key);

	// public Object get(String key);

	public final static int ONE_DAY = 60 * 60 * 24;
	
	public final static int ONE_HOURS = 60 * 60;
	
	public final static int TEN_MINUTE = 60 * 10;
	
	public final static int FIVE_MINUTE = 60 * 5;
	
	public final static int THREE_MINUTE = 60 * 3;
}
