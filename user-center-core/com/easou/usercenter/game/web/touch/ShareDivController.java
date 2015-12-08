package com.easou.usercenter.game.web.touch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.usercenter.AjaxBaseController;
import com.easou.usercenter.util.ConditionUtil;
@Controller
public class ShareDivController extends AjaxBaseController {
	
	private static Logger LOG = Logger.getLogger(ShareDivController.class);

    @RequestMapping("/game/touch/shareDiv")
	public String show(final HttpServletRequest request,
			final HttpServletResponse response, ModelMap model) {
    	StringBuffer buffer = new StringBuffer();
    	//分享内容
    	String shareContent = request.getParameter("shareContent")!=null?request.getParameter("shareContent"):"梵町游戏";
    	//腾讯微博分享
    	buffer.append("<div style=\"padding-left: 30%;padding-right: 30%;padding-bottom: 10px;\">");
    	buffer.append("<div id=\"qqwb_share__\"");
    	buffer.append(" data-appkey=\"801316669\"");
    	buffer.append(" data-icon=\"1\"");  
    	buffer.append(" data-counter_pos=\"top\""); 
    	buffer.append(" data-content=\"").append(shareContent).append("\""); 
    	buffer.append(" data-richcontent=\"{line1}|{line2}|{line3}\"");
    	buffer.append(" data-pic=\"{pic}\"");
    	buffer.append(">");
    	buffer.append("</div>");
    	buffer.append("</div>");
    	//新浪微博分享
    	buffer.append("<div style=\"padding-left: 30%;padding-right: 30%; height: 35px;\">");
    	buffer.append("<wb:share-button"); 
    	buffer.append(" title=\"").append(shareContent).append("\""); 
    	//显示微博来源
    	//buffer.append(" appkey=\"").append(appkey).append("\""); 
    	buffer.append(" count=\"n\""); 
    	buffer.append(" type=\"button\""); 
    	buffer.append(" size=\"middle\""); 
    	buffer.append(">");
    	buffer.append("</wb:share-button>");
    	buffer.append("</div>");
    	//取消按钮
    	buffer.append("<div>"); 
    	buffer.append(" <input type=\"button\" value=\"返&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;回\"");
    	buffer.append(" onclick=\"javascript:shareDiv(-1);\">"); 
    	buffer.append("</div>"); 
    	
		return renderJSONSuccess(response,buffer.toString());
	}

}
