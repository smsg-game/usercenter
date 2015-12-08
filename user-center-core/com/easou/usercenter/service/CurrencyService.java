package com.easou.usercenter.service;

/**
 * 获取E币
 * 
 * @author damon
 * @since 2013.02.18
 * @since 1.0
 */
public interface CurrencyService {
	
	/**
	 * 获取E币
	 * 
	 * @param userId
	 * @return
	 */
	public String getJSONCurrency(String userId);

}
