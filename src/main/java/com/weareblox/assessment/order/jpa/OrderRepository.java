package com.weareblox.assessment.order.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for database operations of the Order
 */
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
	
	List<OrderEntity> findByCustomerId(String customerId);
	

}
