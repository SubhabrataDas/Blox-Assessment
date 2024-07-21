package com.weareblox.assessment.coin.api.event;

import lombok.Value;

@Value
public class InsufficientCoinEvent {
	
	String coinId;
	
	int quantity;
	
	String comment;

	String orderId;
	
}
