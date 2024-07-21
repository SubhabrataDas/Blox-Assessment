package com.weareblox.assessment.ledger.jpa;

import java.math.BigDecimal;

import com.weareblox.assessment.ledger.api.command.TransactionType;

/**
 * this is a jpa projection used to get a user view 
 * of some of the columns from the Ledger entity
 */
public interface LedgerEntityProjection {
	
	/**
     * opening balance before the transaction
     */
	BigDecimal getOpeningBalance();
	
    /**
     * final balance after the transaction
     */
	BigDecimal getClosingBalance();
	
	 /**
     * amount payed for the transaction
     */
	BigDecimal getAmount();
	
    /**
     * Type of the transaction - BUY or SELL
     */
	TransactionType getType();
	
	  /**
     * Coins present with the customer after transaction
     */
	int getCoinBalance();

}
