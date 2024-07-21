package com.weareblox.assessment.order.jpa;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.Nullable;

import lombok.Data;

/**
 * Entity for storing the order of the customer
 */
@Entity
@Table(name = "orders")
@Data
public class OrderEntity {
	
	/**
	 * identifier of the order entity
	 */
	@Id
    private String id;
	
	/**
	 * identifier for the coin for which the order has been placed
	 */
	private String coinId;
	
	/**
	 * identifier for the customer who is placing the order
	 */
	private String customerId;
	
	/**
	 * number of coin to buy or sell in this order
	 */
	private Integer quantity;
	
	/**
	 * current status of the order
	 */
	private OrderStatus status;
	
	/**
	 * type of order - buy or sell
	 */
	private OrderType type;
	
	/**
	 * optional comment for the order
	 */
	@Nullable
	private String comment;
	
	@CreatedDate
	private Instant createdDate;
	
	@LastModifiedDate
	private Instant modifiedDate;
	
	

}
