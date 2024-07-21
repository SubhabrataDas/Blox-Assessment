package com.weareblox.assessment.coin.api.event;

import lombok.Value;

@Value
public class CoinRemovedEvent {
	
	String coinId;
	
	int quantity;
	
	String orderId;
	
	int openingBalance;
	
	boolean isRevert;

}
