package com.weareblox.assessment.coin.dto;


import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Value;

/**
 * DTO for storing Coin information.
 */
@Value
public class Coin {
	
	private String id;
	
	/**
	 * name of the coin
	 */
	@NotBlank(message = "Coin name cannot be empty")
	private String name;
	
	/**
	 * logo url of the coin
	 */
    @NotEmpty(message = "LogoUrl cannot be empty")
	private String logoUrl;
	 
    /**
     * description of the coin.
     */
    @NotEmpty(message = "Description cannot be empty")
	private String description;
	
    /**
     * balance of the coin.
     */
	@NotNull(message = "Balance cannot be null")
	@Min(0)
	private Integer balance;
	
	/**
	 * price of the coin
	 */
	@NotNull(message = "Price cannot be null")
	@Min(0)
	private BigDecimal price;
	
	/**
	 * commission to be paid for the coin.
	 */
	@NotNull(message = "Commission cannot be null")
	@Min(0)
	private BigDecimal commission;

	
}
