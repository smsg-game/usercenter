package com.easou.cas.ticket;

 

import java.util.Collection;

import net.rubyeye.xmemcached.MemcachedClient;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;
import org.springframework.beans.factory.DisposableBean;
 

public final class XmemCacheTicketRegistry extends
		AbstractDistributedTicketRegistry implements DisposableBean {

	private MemcachedClient client;
	
//	private MemCachedClient tkBackClient;
//
//	public MemCachedClient getTkBackClient() {
//		return tkBackClient;
//	}
//
//	public void setTkBackClient(MemCachedClient tkBackClient) {
//		this.tkBackClient = tkBackClient;
//	}

	public MemcachedClient getClient() {
		return client;
	}

	public void setClient(MemcachedClient client) {
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
	
	public static int[] buildWeights(String str){
		String[] arr = str.split(",");
		
		int[] weights = new int[arr.length];
		for(int i=0;i<arr.length;i++){
			weights[i] = Integer.parseInt(arr[i]);
		}
		return weights;
	}
	
	public void updateTicket(final Ticket ticket) {
		try {
			if (ticket instanceof TicketGrantingTicket) {
				this.client.set(ticket.getId(), tgtTimeout, ticket);
			}
	
			if (ticket instanceof ServiceTicket) {
				this.client.set(ticket.getId(), stTimeout, ticket);
			}
		} catch (Throwable e) {
			log.error("update ticket to memcached id:" + ticket.getId() + " ERROR! ", e);
		}
	}

	public void addTicket(final Ticket ticket) {
		try {
			if (ticket instanceof TicketGrantingTicket) {
				log.debug("write TG ticket to memcached " + ticket.getId());
				this.client.add(ticket.getId(), tgtTimeout, ticket);
			}
			
			if (ticket instanceof ServiceTicket) {
				log.debug("write service ticket to memcached " + ticket.getId());
				this.client.add(ticket.getId(), stTimeout, ticket);
			}
			if(log.isDebugEnabled()) {
				log.debug(client.get(ticket.getId()).toString());
			}
		} catch (Throwable e) {
			log.error("add ticket to memcached id:" + ticket.getId() + " ERROR! ", e);
		}
	}

	public boolean deleteTicket(final String ticketId) {
		log.debug("删除 delete ticket in memcached " + ticketId);
		try {
			return this.client.delete(ticketId);
		} catch (Throwable e) {
			log.error("delete ticket to memcached id:" + ticketId + " ERROR! ", e);
		}
		return false;
	}

	public Ticket getTicket(final String ticketId) {
		Ticket t = null;
		try {
			t = (Ticket) this.client.get(ticketId);
		} catch (Throwable e) {
			log.error("get ticket from memcached id:" + ticketId + " ERROR! ", e);
		}
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
