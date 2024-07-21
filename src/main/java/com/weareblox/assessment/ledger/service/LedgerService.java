package com.weareblox.assessment.ledger.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.ledger.dto.Ledger;
import com.weareblox.assessment.ledger.jpa.LedgerEntity;
import com.weareblox.assessment.ledger.jpa.LedgerRepository;

@Service
public class LedgerService {

	@Autowired private LedgerRepository ledgerRepository;
	
	public List<Ledger> getLedgerForCustomer(String customerId){
		return ledgerRepository.findByCustomerIdOrderByCreatedDateDesc(customerId)
				.stream()
				.map(this::toDto)
				.toList();
	}
	
	
	
	Ledger toDto(LedgerEntity ledgerEntity) {
		if(ledgerEntity == null) {
			return null;
		}
		 return  Ledger.builder()
				 .id(ledgerEntity.getId())
				 .amount(ledgerEntity.getAmount())
	        		.openingBalence(ledgerEntity.getOpeningBalance())
	        		.closingBalance(ledgerEntity.getClosingBalance())
	        		.coinId(ledgerEntity.getCoinId())
	        		.coinsleft(ledgerEntity.getCoinBalance())
	        		.transactionType(ledgerEntity.getType().name()).build();
	}
}
