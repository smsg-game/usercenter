package com.easou.usercenter.web.api2;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.cas.auth.EucToken;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.util.CookieUtil;
import com.easou.usercenter.context.ServiceFactory;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.service.OUserService;

@Controller
@RequestMapping("/api2/")
public class Api2TrdUserController extends AbstractAPI2Controller {

	@Resource(name = "oUserService")
	private OUserService oUserService;

	/**
	 * 根据用户id获得第三方帐号的绑定情况
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getTrdBindList.json")
	@ResponseBody
	public String getTrdBindList(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "无效的TOKEN");
				}
				
				List<OUser> list = oUserService.queryOUserByEaId(userId+"");
				JBody body=new JBody();
				body.putContent("list", list);
				jbean.setBody(body);
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
	
	/**
	 * 解除第三方帐号的绑定情况
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("jbindTrdUser.json")
	@ResponseBody
	public String jbindTrdUser(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				String token = getRequestBean().getBody().getString(
						CookieUtil.COOKIE_TGC);
				EucToken eucToken = new EucToken();
				eucToken.setToken(token);
				Long userId = ServiceFactory.getInstance()
						.getAuthVerifyService().verify(eucToken);
				if (null == userId || 0 == userId.longValue()) {
					return busiErrorBean(jbean, "1", "无效的TOKEN");
				}
				String netType = getRequestBean().getBody().getString("netType");
				if(null==netType || "".equals(netType)) {
					return busiErrorBean(jbean, "2", "netType参数错误");
				}
				oUserService.deleteUserByEaid(userId+"", netType);
				List<OUser> list = oUserService.queryOUserByEaId(userId+"");
				JBody body=new JBody();
				body.putContent("list", list);
				jbean.setBody(body);
				return jbean;
			}
		};
		return abc.genReturnJson();
	}
}
