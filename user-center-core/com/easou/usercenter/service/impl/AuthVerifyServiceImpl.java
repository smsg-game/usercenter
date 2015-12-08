package com.easou.usercenter.service.impl;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;

import com.easou.cas.auth.EucToken;
import com.easou.usercenter.service.AuthVerifyService;

public class AuthVerifyServiceImpl implements AuthVerifyService{
	
//	@Autowired
	TicketRegistry ticketRegistry;
	
	
	
	

	public TicketRegistry getTicketRegistry() {
		return ticketRegistry;
	}



	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}



	@Override
	public Long verify(EucToken token) {
		
		Ticket ticket = ticketRegistry.getTicket(token.getToken());
		if (ticket != null && ticket instanceof TicketGrantingTicket) {
			Authentication authentication = ((TicketGrantingTicket) ticket)
					.getAuthentication();
			Principal principal = authentication.getPrincipal();
			// 用户ID
			return Long.valueOf(principal.getId()).longValue();
		}
		return null;
	}

}
