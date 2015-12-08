package com.easou.usercenter.web.touch;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easou.game.entity.Game;
import com.easou.usercenter.service.HisGamesService;
import com.easou.usercenter.web.user.ArrestValidation;
import com.easou.usercenter.web.user.UserBaseController;

/**
 * 获玩过的历史游戏
 * 
 * @author damon
 * @since 2012.01.11
 * @version 1.0
 */
@Controller
public class HisGamesController extends UserBaseController {

	private static Logger LOG = Logger.getLogger(HisGamesController.class);
	
	@Resource(name = "hisGamesService")
	HisGamesService hisGamesService;

	@RequestMapping("/user/getHisGames")
	@ResponseBody
	public String show(final HttpServletRequest request,
			final HttpServletResponse response, ModelMap model) {
		LOG.debug("init getHisGames...");
		ArrestValidation av = checkLogin(request, model);
		//获取esid
		String esid = request.getParameter("esid");
		String wver = request.getParameter("wver")!=null?request.getParameter("wver"):"t";
		String v = request.getParameter("v");
		// 获取游戏条数
		int count = 10;
	    //历史游戏列表
	    List<Game> gameList = hisGamesService.getGameList(av.getUserId(),wver,esid,v, count);
	    if(null==gameList) {
	    	return renderJSONFailure(response, "获取不到您玩过的游戏");
	    }	    
		return renderJSONSuccess(response, rendSuccess(gameList));
	}
	
	/**
	 * 成功返回结果
	 * 
	 * @param gameList
	 * @return
	 */
	private String rendSuccess(List<Game> gameList){
		if(gameList==null||gameList.size()==0){
			return "您还没有玩过的游戏，快去游戏吧！";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("<table>");
		for(int i=0;i<gameList.size();i++){
			Game game=gameList.get(i);
			if(i%3==0){
				buffer.append("<tr>");
			}
			buffer.append(" <td style=\"border:10px; border-color:#FFF;text-align:center;\">");
			buffer.append("<a href=\"").append(game.getGameUrl()).append("\" >");
			buffer.append("<img src=\"").append(game.getLogoPicUrl()).append("\" width=\"70px\" height=\"70px\" style=\"margin:5px;\"/><br/>");
			buffer.append(game.getName());
			buffer.append("</a>");
			buffer.append("</td>");
            if((i+1)%3==0){
            	buffer.append("</tr>");
			}
		}
		buffer.append("</table>");
		return buffer.toString();
	}
}
