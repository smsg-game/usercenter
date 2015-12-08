package com.easou.usercenter.web.api2;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.cas.auth.EucToken;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JUser;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.asyn.AsynGameManager;
import com.easou.usercenter.asyn.AsynManager;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.LogConstant;

/**
 * 验证ST
 * 
 * @author jay
 * @since 2013.01.14
 * @version 1.0
 */
@Controller
@RequestMapping("/api2/")
public class Api2ValidateServiceTicketController extends AbstractAPI2Controller {

	@Resource
	private TicketRegistry serviceTicketRegistry;

	@Resource
	private EucUserService userService;

	@Resource(name = "ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	
	@Resource(name = "asynUserManager")
	private AsynManager asynUserManager;
	
	@Resource(name = "asynGameManager")
	private AsynGameManager asynGameManager;

	@RequestMapping("validateServiceTicket.json")
	@ResponseBody
	public String validateServiceTicket(final HttpServletRequest request,
			final HttpServletResponse response) {
		AbstractOpenBeanCreator abc = new AbstractOpenBeanCreator(request,
				response) {

			@Override
			public JBean buildJBean(JBean jbean) {
				JBody body = new JBody();
				String ticket = getRequestBean().getBody().getString("ticket");
				ServiceTicket serviceTicket = null;
				if (!StringUtil.isEmpty(ticket)) {
					Ticket tTicket = serviceTicketRegistry.getTicket(ticket, ServiceTicket.class);
					
					if (tTicket == null) {
						BizLogUtil.stLog("", "game", LogConstant.RESULT_FAILURE, "", ticket, LogConstant.ST_VALIDATE_ERROR);
						return busiErrorBean(jbean, "1", "验证失败");
					} else {
						serviceTicket = (ServiceTicket) tTicket;
					}
				}
				final int authenticationChainSize = serviceTicket
						.getGrantingTicket().getChainedAuthentications().size();
				final Authentication authentication = serviceTicket
						.getGrantingTicket().getChainedAuthentications()
						.get(authenticationChainSize - 1);
				String ticketGrantingTicketId = serviceTicket
						.getGrantingTicket().getId();
				// 获取用户信息
				EucUser eucUser = userService.queryUserInfoById(authentication
						.getPrincipal().getId());
				/* 返回用户信息 */
				JUser jUser = transJUser(eucUser, new JUser());
				body.put("user", jUser);
				/* 返回TGC */
				EucToken token = new EucToken();
				token.setToken(ticketGrantingTicketId);
				token.setDomain(ticketGrantingTicketCookieGenerator
						.getCookieDomain());
				token.setPath(ticketGrantingTicketCookieGenerator
						.getCookiePath());
				token.setAge(ticketGrantingTicketCookieGenerator
						.getCookieMaxAge());
				body.put("token", token);
				/* 异步更新数据 */
				EucUser tempUser = new EucUser();
				tempUser.setId(eucUser.getId());
				tempUser.setLastLoginTime(new Date(System
						.currentTimeMillis()));
				asynUserManager.asynUpdate(tempUser);
				/* TODO 统计登录游戏的人数 */
				BizLogUtil.stLog("", "game", LogConstant.RESULT_SUCCESS, "", ticket, LogConstant.ST_VALIDATE_SUCCESS);
                BizLogUtil.gLoginLog(qn, appId, tempUser.getId(),LogConstant.RESULT_SUCCESS, eucUser.getRegisterTime(), tempUser.getLastLoginTime(), agent);
				/*记录游戏历史记录*/
                if(!StringUtil.isEmpty(appId)){
                    asynGameManager.savePlayHistory(appId,tempUser.getId());
                }
				jbean.setBody(body);
				return jbean;
			}
		};
		return abc.genReturnJson();
	}

	public TicketRegistry getServiceTicketRegistry() {
		return serviceTicketRegistry;
	}

	public void setServiceTicketRegistry(TicketRegistry serviceTicketRegistry) {
		this.serviceTicketRegistry = serviceTicketRegistry;
	}

	public EucUserService getUserService() {
		return userService;
	}

	public void setUserService(EucUserService userService) {
		this.userService = userService;
	}

	public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
		return ticketGrantingTicketCookieGenerator;
	}

	public void setTicketGrantingTicketCookieGenerator(
			CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

}
