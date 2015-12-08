/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.ticket.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Provides the Ticket Granting Ticket expiration policy.  Ticket Granting Tickets
 * can be used any number of times, have a fixed lifetime, and an idle timeout.
 *
 * @author William G. Thompson, Jr.
 * @version $Revision$ $Date$
 * @since 3.4.10
 */
public final class TicketGrantingTicketExpirationPolicy implements ExpirationPolicy, InitializingBean {

//    private static final Logger log = LoggerFactory.getLogger(TicketGrantingTicketExpirationPolicy.class);
    protected final Log log = LogFactory.getLog(TicketGrantingTicketExpirationPolicy.class);

    /** Static ID for serialization. */
    private static final long serialVersionUID = 2136490343650084287L;

    /** Maximum time this ticket is valid  */
    private long maxTimeToLiveInMilliSeconds;

    /** Time to kill in milliseconds. */
    private long timeToKillInMilliSeconds;

    public void setMaxTimeToLiveInMilliSeconds(final long maxTimeToLiveInMilliSeconds){
        this.maxTimeToLiveInMilliSeconds = maxTimeToLiveInMilliSeconds;
    }

    public void setTimeToKillInMilliSeconds(final long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.isTrue((maxTimeToLiveInMilliSeconds >= timeToKillInMilliSeconds), "maxTimeToLiveInMilliSeconds must be greater than or equal to timeToKillInMilliSeconds.");
    }

    public boolean isExpired(final TicketState ticketState) {
        // Ticket has been used, check maxTimeToLive (hard window)
        if ((System.currentTimeMillis() - ticketState.getCreationTime() >= maxTimeToLiveInMilliSeconds)) {
            if (log.isDebugEnabled()) {
                log.debug("Ticket is expired due to the time since creation being greater than the maxTimeToLiveInMilliSeconds");
            }
            return true;
        }

        // Ticket is within hard window, check timeToKill (sliding window)
        if ((System.currentTimeMillis() - ticketState.getLastTimeUsed() >= timeToKillInMilliSeconds)) {
            if (log.isDebugEnabled()) {
                log.debug("Ticket is expired due to the time since last use being greater than the timeToKillInMilliseconds");
            }
            return true;
        }

        return false;
    }

}
