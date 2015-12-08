package com.easou.cas.ticket;

 

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;
import org.springframework.beans.factory.DisposableBean;
import com.danga.MemCached.MemCachedClient;
 

public final class MemCacheTicketRegistry extends
		AbstractDistributedTicketRegistry implements DisposableBean {

	private MemCachedClient client;
	
//	private MemCachedClient tkBackClient;
//
//	public MemCachedClient getTkBackClient() {
//		return tkBackClient;
//	}
//
//	public void setTkBackClient(MemCachedClient tkBackClient) {
//		this.tkBackClient = tkBackClient;
//	}

	public MemCachedClient getClient() {
		return client;
	}

	public void setClient(MemCachedClient client) {
		this.client = client;
	}

	public int getTgtTimeout() {
		return tgtTimeout;
	}

	public void setTgtTimeout(int tgtTimeout) {
		this.tgtTimeout = tgtTimeout;
	}

	public int getStTimeout() {
		return stTimeout;
	}

	public void setStTimeout(int stTimeout) {
		this.stTimeout = stTimeout;
	}

	private int tgtTimeout;

	private int stTimeout;
	
	private Date addSecond(int second){
		java.util.Calendar ca = Calendar.getInstance();
		ca.add(Calendar.SECOND, second);
		return ca.getTime();
	}
	
	public void updateTicket(final Ticket ticket) {
		if (ticket instanceof TicketGrantingTicket) {
			this.client.set(ticket.getId(), ticket,addSecond(tgtTimeout));
		}

		if (ticket instanceof ServiceTicket) {
			this.client.set(ticket.getId(), ticket,addSecond(stTimeout));
		}
	}

	public void addTicket(final Ticket ticket) {
		if (ticket instanceof TicketGrantingTicket) {
			log.debug("write TG ticket to memcached " + ticket.getId());
			this.client.add(ticket.getId(), ticket,addSecond(tgtTimeout));
		}
		
		if (ticket instanceof ServiceTicket) {
			log.debug("write service ticket to memcached " + ticket.getId());
			this.client.add(ticket.getId(), ticket,addSecond(stTimeout));
		}
		if(log.isDebugEnabled()) {
			log.debug(client.get(ticket.getId()).toString());
		}
	}

	public boolean deleteTicket(final String ticketId) {
		log.debug("删除 delete ticket in memcached " + ticketId);
		return this.client.delete(ticketId);

	}

	public Ticket getTicket(final String ticketId) {
		Ticket t = (Ticket) this.client.get(ticketId);
		if (t == null) {
			log.debug("无法找到本地的验证票：" + ticketId);
//			t = (Ticket) this.tkBackClient.get(ticketId);
//			if(t == null) {
//				log.debug("无法找到远程的验证票：" + ticketId);
//				return null;
//			}
		}
		return getProxiedTicketInstance(t);
	}

	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 *             if you try and call this operation.
	 */
	public Collection<Ticket> getTickets() {
		throw new UnsupportedOperationException("GetTickets not supported.");
	}

	public void destroy() throws Exception {
		throw new UnsupportedOperationException("GetTickets not supported.");
	}

	@Override
	protected boolean needsCallback() {
		return true;
	}
}
