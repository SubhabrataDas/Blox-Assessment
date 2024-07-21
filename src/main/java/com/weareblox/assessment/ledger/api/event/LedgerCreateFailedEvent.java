package com.weareblox.assessment.ledger.api.event;

import lombok.Builder;
import lombok.Value;



/**
 * This is the event generated after an event generation has failed
 */
@Builder
@Value
public class LedgerCreateFailedEvent {
	
	public String coinId;
	
	public String orderId;

}
