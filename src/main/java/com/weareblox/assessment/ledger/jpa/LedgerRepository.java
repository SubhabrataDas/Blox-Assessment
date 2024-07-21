package com.weareblox.assessment.ledger.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<LedgerEntity, String> {
	
	public List<LedgerEntity>  findByCustomerIdOrderByCreatedDateDesc(String customerId);
}
