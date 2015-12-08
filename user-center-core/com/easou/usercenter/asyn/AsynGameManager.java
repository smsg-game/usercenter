package com.easou.usercenter.asyn;

/**
 * 异步请求游戏服务器
 * 
 * @author damon
 *
 */
public interface AsynGameManager {
	
	public void savePlayHistory(String gid,long userId);
	
    public void addEb(String userId,String mobile);

}
