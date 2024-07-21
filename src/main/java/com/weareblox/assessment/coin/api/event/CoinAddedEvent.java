package com.weareblox.assessment.coin.api.event;

import lombok.Value;

@Value
public class CoinAddedEvent {

	String coinId;
	  
	Integer quantity;
	
	String orderId;
	
	boolean isRevert;

}
