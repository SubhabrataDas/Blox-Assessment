package com.weareblox.assessment.coin.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Value;

/**
 * This entity is used to patch the Coin.
 */
@Value
public class CoinPatch {
	
	/**
	 * how many coins to add to the repository
	 */
	@NotNull(message = "Quantity cannot be null")
	@Size(min = 0 , message = "Quantity cannot be negetive")
	private Integer quantity;
	
	/**
	 * price of each coin
	 */
	@NotNull(message = "price cannot be null")
	@Size(min = 0 , message = "price cannot be negetive")
	private BigDecimal price;
	
	/**
	 * commission to be paid on each transaction
	 */
	@NotNull(message = "Commission cannot be null")
	@Size(min = 0 , message = "Commission cannot be negetive")
	private BigDecimal commission;

}
