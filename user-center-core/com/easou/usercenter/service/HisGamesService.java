package com.easou.usercenter.service;

import java.util.List;

import com.easou.game.entity.Game;

/**
 * 历史游戏
 * 
 * @author damon
 * @since 2013.02.18
 * @version 1.0
 */
public interface HisGamesService {
	public List<Game> getGameList(String userId,String wver,String esid,String v, int count);	
}
