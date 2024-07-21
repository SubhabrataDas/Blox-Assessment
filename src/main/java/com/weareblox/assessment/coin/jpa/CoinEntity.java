package com.weareblox.assessment.coin.jpa;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;


/**
 * This the JPA entity for the coin
 */
@Entity
@Table(name = "coin")
@Data
public class CoinEntity {
	
	/**
	 * primary key of the coin
	 */
    @Id
    private String id;
    
    /**
	 * name of the coin
	 */
    private String name;
    
    /**
	 * logo of the coin
	 */
    private String logoUrl;
    
    /**
	 * description  of the coin
	 */
    private String description;
    
    /**
     * how many coins in the repository
     */
    private Integer balance;
    
    /**
     * price of each coin
     */
    private BigDecimal price;
    
    /**
     * commission to be paid for each coin.
     */
    private BigDecimal commision;

}
