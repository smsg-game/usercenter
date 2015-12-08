package com.easou.common.config;

import java.util.List;

public interface Config {
	/**
	 * 根据主键获取字符串配置项
	 * 
	 * @param name
	 *            配置项名称
	 * @return
	 */
	public String getStr(String name);

	/**
	 * 根据配置项名称获取整型配置项值
	 * 
	 * @param name
	 *            配置项名称
	 * @return
	 */
	public Integer getInt(String name);

	/**
	 * 根据名称获取长整型配置项值
	 * 
	 * @param name
	 *            配置项名称
	 * @return
	 */
	public Long getLong(String name);

	/**
	 * 根据配置项名称获取浮点型配置项值
	 * 
	 * @param name
	 *            配置项名称
	 * @return
	 */
	public Float getFloat(String name);

	/**
	 * 根据据配置项名称获取集合配置项信息
	 * 
	 * @param name
	 *            配置项名称
	 * @return
	 */
	public List<String> getList(String name);
}
