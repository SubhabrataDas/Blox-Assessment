package com.weareblox.assessment.coin.api.event;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class CoinRegisteredEvent {
	
	String coinId;
	
	private String name;

	private String logoUrl;

	private String description;

	private Integer balance;

	private BigDecimal price;
	
	private BigDecimal commission;

}
