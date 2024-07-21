package com.weareblox.assessment.ledger.api.event;

import java.math.BigDecimal;

import com.weareblox.assessment.ledger.api.command.TransactionType;

import lombok.Builder;
import lombok.Value;

/**
 * This is the event generated after an event has been successfully created.
 */
@Builder
@Value
public class LedgerCreatedEvent {

	private String ledgerId;
	
	private String orderId;
	
	private TransactionType type;

	private BigDecimal openingBalance;
	
	private BigDecimal payment;
	
	private BigDecimal commision;
	
	private BigDecimal closingBalance;

	private int currentCoinBalance;

	private int coinAmount;
	
	private String coinId;
}
