package com.easou.usercenter.game.web.touch;

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
import com.alibaba.fastjson.JSONObject;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.game.web.GameBaseController;
import com.easou.usercenter.service.CurrencyService;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.web.user.ArrestValidation;

/**
 * 获取E币
 * 
 * @author damon
 * @since 2012.01.11
 * @version 1.0
 */
@Controller
@RequestMapping("/game/touch/getECurr")
public class CurrencyController extends GameBaseController {

	private static Logger LOG = Logger.getLogger(CurrencyController.class);
	
	@Resource
	private CurrencyService currencyService; 

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String show(final HttpServletRequest request,
			final HttpServletResponse response, ModelMap model) {
		LOG.debug("init getECurr...");
		ArrestValidation av = checkLogin(request, model);
		// TODO 调用接口获取E币
		int eCurr = 0;
		String json = currencyService.getJSONCurrency(av.getUserId());
		if(json!=null){//返回JSON结果
			JSONObject o = JSON.parseObject(json);
			String temp = (String)o.get("ebNum");
			if(!StringUtil.isEmpty(temp)){
				eCurr = Integer.valueOf(temp);
			}
		}
        
		return renderJSONSuccess(response, String.valueOf(eCurr));
	}

}
