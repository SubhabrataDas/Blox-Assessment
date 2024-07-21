package com.weareblox.assessment.coin.api.event;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class CoinPriceUpdatedEvent {

	String coinId;
	  
	BigDecimal price;

}
