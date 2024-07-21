package com.weareblox.assessment.customer.dto;

import javax.validation.constraints.NotBlank;

import lombok.Value;

/**
 * Customer DTO
 */
@Value
public class Customer {
	
	/**
	 * id of the customer
	 */ 
	private String id;
	
	/**
	 * name of the customer
	 */
	@NotBlank(message = "Name cannot be empty")
	private String name;

}
