package com.easou.usercenter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.api.EucHttpClient;
import com.easou.common.util.StringUtil;
import com.easou.game.GameConfig;
import com.easou.game.entity.Game;
import com.easou.usercenter.service.HisGamesService;

public class HisGamesServiceImpl implements HisGamesService{

	@Override
	public List<Game> getGameList(String userId, String wver, String esid,String v, int count) {
		String jsonGames=getJsonGames(userId, count, v);
	    if(jsonGames==null||"".equals(jsonGames.trim())){
	    	return null;
	    }
	    if(!jsonGames.startsWith("{\"playHistory\":")) {
	    	jsonGames = "{\"playHistory\":" + jsonGames + "}";
	    }
		Map<String,JSONArray> map =(Map<String,JSONArray>)JSON.parse(jsonGames);
		//if(map!=null)
		List<Game> gameList = new ArrayList<Game>();
		JSONArray jsonArray = map.get("playHistory");
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = (JSONObject)jsonArray.get(i);
			//LOG.info(jsonObject.toJSONString());
	    	Game game = new Game();
	    	game.setId(jsonObject.getString("id"));
	    	game.setName(jsonObject.getString("name"));
	    	game.setLogoPicUrl(jsonObject.getString("logoPicUrl"));
	    	game.setOnlienCount(jsonObject.getString("onlineCount"));
	    	game.setGameUrl(buildGameUrl(game,wver,esid,v));
	    	game.setSharing(jsonObject.getString("sharing"));
	    	gameList.add(game);
		}
		return gameList;
	}

	/**
	 * 通过游戏接口获得游戏历史json
	 * @param userId
	 * @param count
	 * @param v
	 * @return
	 */
	private String getJsonGames(String userId,int count,String v){
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("game.url"));
		urlBuffer.append("/plat/showPlayHistory.e");
		Map paraMap = new HashMap();
		paraMap.put("pid", userId);
		paraMap.put("v", v);
		if(count>0){
		    paraMap.put("historyCount", count);
		}
		String json = EucHttpClient.httpGet(urlBuffer.toString(), paraMap);
		return json;
	}
	
	/**
	 * 例子：http://localhost:8080/plat/start.e?gameId=1273&esid=55daHozOw3q&wver=c&qn=33&fr=7&l=7
	 * 
	 * @param game
	 * @param wver
	 * @param esid
	 * @return
	 */
	private String buildGameUrl(Game game,String wver,String esid,String v){
		StringBuffer urlBuffer = new StringBuffer(GameConfig.getProperty("game.url"));
		//urlBuffer.append(GameConfig.getProperty("game.hisgams.url"));
		urlBuffer.append("/start.e");
		if(StringUtil.isEmpty(wver)){//默认触版
			wver="t";
		}
		urlBuffer.append("?wver=").append(wver);
		urlBuffer.append("&version=").append(v);
		if(!StringUtil.isEmpty(esid)){
			urlBuffer.append("&esid=").append(esid);
		}
		urlBuffer.append("&gameId=").append(game.getId());
		urlBuffer.append("&onlineCount=").append(game.getOnlienCount());
		urlBuffer.append("&qn=33&fr=7&l=7");
		return urlBuffer.toString();
	}
	

}
