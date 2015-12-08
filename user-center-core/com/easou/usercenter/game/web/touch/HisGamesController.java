package com.easou.usercenter.game.web.touch;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.game.entity.Game;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.HisGamesService;
import com.easou.usercenter.service.impl.HisGamesServiceImpl;
import com.easou.usercenter.web.user.ArrestValidation;

/**
 * 获玩过的历史游戏
 * 
 * @author damon
 * @since 2012.01.11
 * @version 1.0
 */
@Controller
public class HisGamesController extends GameBaseController {

	private static Logger LOG = Logger.getLogger(HisGamesController.class);
	
	@Resource(name = "hisGamesService")
	HisGamesService hisGamesService;

	@RequestMapping("/game/touch/getHisGames")
	@ResponseBody
	public String show(final HttpServletRequest request,
			final HttpServletResponse response, ModelMap model) {
		LOG.debug("start getHisGames...");
		ArrestValidation av = checkLogin(request, model);
		//获取esid
		String esid = request.getParameter("esid");
		String wver = request.getParameter("wver")!=null?request.getParameter("wver"):"t";
		String v = request.getParameter("v");
		boolean isApp = false;
		if("AndroidGameHall".equals(request.getAttribute("appAgent"))) {
			isApp = true;
		}
		// 获取游戏条数
		int count = 10;
	    //历史游戏列表
	    List<Game> gameList = hisGamesService.getGameList(av.getUserId(),wver, esid,v, count);
	    if(null==gameList) {
	    	return renderJSONFailure(response, "获取不到您玩过的游戏");
	    }
	    request.setAttribute("gameList",gameList);
		return renderJSONSuccess(response, rendSuccess(gameList, isApp));
	}
	
	/**
	 * 成功返回结果
	 * 
	 * @param gameList
	 * @return
	 */
	private String rendSuccess(List<Game> gameList, boolean isApp){
		if(gameList==null||gameList.size()==0){
			return "&nbsp;&nbsp;您还没有玩过的游戏，快去游戏吧！";
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<gameList.size();i++){
			Game game=gameList.get(i);
			
			
			buffer.append("<li>");
			buffer.append("<span class=\"info-account-text\">");
			if(isApp) {
				// 客户端点图标不进入游戏(因为不是全屏)
				buffer.append("<img src=\"").append(game.getLogoPicUrl()).append("\" class=\"fl info-account-logo\"/>");
			} else {
				buffer.append("<a href=\"").append(game.getGameUrl()).append("\" >");
				buffer.append("<img src=\"").append(game.getLogoPicUrl()).append("\" class=\"fl info-account-logo\"/>");
				buffer.append("</a>");
			}
			buffer.append("<ol  class=\"fl\">");
			buffer.append("<dt>").append(game.getName()).append("</dt>");
			buffer.append("<ds>").append("如果觉得不错，请告诉你的朋友~").append("</ds>");
			buffer.append("</ol>");
			buffer.append("</span>");
			buffer.append("<a href=\"javascript:shareDiv(").append(game.getId()).append(");\" class=\"info-account-button button-blue\" >");
			buffer.append("分享");
			buffer.append("</a>");
			buffer.append("<input type=\"hidden\" id=\"shareContent").append(game.getId()).append("\" value=\"").append(game.getSharing()).append("\"/>");
			buffer.append("<input type=\"hidden\" id=\"shareUrl").append(game.getId()).append("\" value=\"").append("").append("\"/>");
			buffer.append("</li>");
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		HisGamesService hisGamesService= new HisGamesServiceImpl();
		List<Game> list = hisGamesService.getGameList("1000", "t", "", "2.1", 10);
		System.out.println(new HisGamesController().rendSuccess(list, false));
	}
}
