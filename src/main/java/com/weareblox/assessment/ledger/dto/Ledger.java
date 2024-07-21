package com.weareblox.assessment.ledger.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

import com.weareblox.assessment.controller.validator.ValueOfEnum;
import com.weareblox.assessment.ledger.api.command.TransactionType;

import lombok.Builder;
import lombok.Value;

/**
 * This is Ledger entry DTO saved after a successful transaction.
 * This can be used for querying and showing the result in a client system.
 */

@Builder
@Value
public class Ledger {
   
	String id;
    
	/**
     * opening balance before the transaction
     */
	@NotBlank
    BigDecimal openingBalence;
   
    /**
     * final balance after the transaction
     */
	@NotBlank
    BigDecimal closingBalance;
    
	 /**
     * amount payed for the transaction
     */
	@NotBlank
    BigDecimal amount;
    
	 /**
     * Coins present with the customer after transaction
     */
	@NotBlank
    Integer coinsleft;
    
	 /**
     * Coin used for transaction
     */
	@NotBlank
    String coinId;
    
	 
    /**
     * Type of the transaction - BUY or SELL
     */
	@ValueOfEnum(enumClass = TransactionType.class)
    String transactionType;
}
