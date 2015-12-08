package com.easou.usercenter.web.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;

@Controller
@RequestMapping("/api/")
public class ApiCommonController extends AbstractAPIController {

	@Resource(name="eucUserServiceImpl")
	private EucUserService eucUserService;

	@RequestMapping("getOccupations")
	@ResponseBody
	public String getOccus(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request,response) {

			@SuppressWarnings("unchecked")
			@Override
			public JBean getJBean() {
				JBean jbean=JSonBeanFactory.getDefaultBean();
				List<Map> list=eucUserService.getOccupation();
				JSONArray ja=new JSONArray();
				ja.addAll(list);
				JBody jbody=new JBody();
				jbody.putContent("occus", ja);
				jbean.setBody(jbody);
				return jbean;
			}
			
		};
		return abc.genReturnJson();
	}
}