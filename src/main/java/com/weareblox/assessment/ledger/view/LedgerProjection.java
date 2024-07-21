package com.weareblox.assessment.ledger.view;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.ledger.api.event.LedgerCreatedEvent;
import com.weareblox.assessment.ledger.api.query.FindLedgerQuery;
import com.weareblox.assessment.ledger.api.query.FindLedgerQueryByCustomer;
import com.weareblox.assessment.ledger.dto.Ledger;
import com.weareblox.assessment.ledger.jpa.LedgerEntity;
import com.weareblox.assessment.ledger.jpa.LedgerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Projection class for Ledger to handle events and then update the database with a  
 * view of the ledger.
 */

@Slf4j
@Service
@ProcessingGroup("ledger")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LedgerProjection {

    private final LedgerRepository repository;
    private final QueryUpdateEmitter updateEmitter;

    /**
     * This handles the ledger created event and stores the ledger in DB.
     * @param event
     */
    @EventHandler
    public void on(LedgerCreatedEvent event) {
    	
    	log.debug("Saving Ledger to Database",event);
    	
        var ledger = new LedgerEntity();
        ledger.setId(UUID.randomUUID().toString());
        ledger.setAmount(event.getPayment());
        ledger.setOpeningBalance(event.getOpeningBalance());
        ledger.setClosingBalance(event.getClosingBalance());
        ledger.setCoinBalance(event.getCurrentCoinBalance());
        ledger.setCoinId(event.getCoinId());
        ledger.setOrderId(event.getOrderId());
        ledger.setCustomerId(event.getLedgerId());
        ledger.setType(event.getType());

        repository.save(ledger);
        
        updateEmitter.emit(FindLedgerQueryByCustomer.class,
                query -> Objects.equals(query.getCustomerId(), ledger.getCustomerId()),
                Optional.of(ledger).map(this::toDto));
    }

    /**
     * This handles the query for getting a ledger entry.
     * @param query
     * @return
     */
    @QueryHandler
    public Optional<Ledger> findLedger(FindLedgerQuery query) {
        return repository.findById(query.getLedgerId())
                .map(this::toDto);
    }
    
    /**
     * this handles the query to get a ledger by customer id
     * @param query
     * @return
     */
    @QueryHandler
    public List<Ledger> findLedgerByCustomerId(FindLedgerQueryByCustomer query) {
    	return repository.findByCustomerIdOrderByCreatedDateDesc(query.getCustomerId())
    			.stream().map(this::toDto).toList();
    }
    
  
    /**
     * Utility method to convert entity to DTO
     * @param entity
     * @return
     */
    private Ledger toDto(LedgerEntity entity) {
        if(entity == null) {
            return null;
        }
        return  Ledger.builder().amount(entity.getAmount())
        		.openingBalence(entity.getOpeningBalance())
        		.closingBalance(entity.getClosingBalance())
        		.coinId(entity.getCoinId())
        		.coinsleft(entity.getCoinBalance())
        		.transactionType(entity.getType().name()).build();
    }
}
