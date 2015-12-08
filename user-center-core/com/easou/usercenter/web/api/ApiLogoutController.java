package com.easou.usercenter.web.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JHead;
import com.easou.common.util.CookieUtil;
import com.easou.usercenter.web.BaseController;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;
/**
 * 登出接口
 * 
 * @author damon
 * @since 2012.07.06
 * @version 1.0
 */
@Controller
@RequestMapping("/api/")
public class ApiLogoutController  extends AbstractAPIController {
	
	@Resource
	private CentralAuthenticationService centralAuthenticationService;
	
	public CentralAuthenticationService getCentralAuthenticationService() {
		return centralAuthenticationService;
	}

	public void setCentralAuthenticationService(
			CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	@RequestMapping("logout")
	@ResponseBody
	public String logout(final HttpServletRequest request,
			final HttpServletResponse response) {
		

		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {
			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JHead jHead = new JHead();
				JBody body = new JBody();
				JDesc jdesc = new JDesc(); // 错误描述
				// 获取TGC
				String ticketGrantingTicketId = getRequestBean().getBody()
						.getString(CookieUtil.COOKIE_TGC);
				if(ticketGrantingTicketId!=null){
					//删除服务端缓存中的用户信息
					centralAuthenticationService
					.destroyTicketGrantingTicket(ticketGrantingTicketId);
					//返回成功信息
					body.put("success", true);
					jbean.setBody(body);					
					return jbean;
				}else{//用户未登录
					jHead.setRet(CodeConstant.BUSI_ERROR);
					// 生成TGT失败，可能是密码错误
					jdesc.addReason("9", "用户未登录");
					jbean.setHead(jHead);
					jbean.setDesc(jdesc);
				    return jbean;
				}		
			}
		};

		return abc.genReturnJson();
	}

}
