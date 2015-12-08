package com.easou.game.entity;

/**
 * 游戏
 * 
 * @author damon
 * @since 2013.01.11
 * @version 1.0
 */
public class Game {
	/*图片地址*/
	private String logoPicUrl;
	/*游戏名称*/
	private String name;
	/*游戏ID*/
	private String id;
	/*游戏地址*/
	private String gameUrl;
	/*在线人数*/
	private String onlienCount;
	/*分享内容*/
	private String sharing;
	
	
	public String getLogoPicUrl() {
		return logoPicUrl;
	}
	public void setLogoPicUrl(String logoPicUrl) {
		this.logoPicUrl = logoPicUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGameUrl() {
		return gameUrl;
	}
	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}
	public String getOnlienCount() {
		return onlienCount;
	}
	public void setOnlienCount(String onlienCount) {
		this.onlienCount = onlienCount;
	}
	public String getSharing() {
		return sharing;
	}
	public void setSharing(String sharing) {
		this.sharing = sharing;
	}
	
	
	
}
