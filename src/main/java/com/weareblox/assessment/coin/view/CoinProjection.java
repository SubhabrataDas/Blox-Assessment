package com.weareblox.assessment.coin.view;

import java.util.Objects;
import java.util.Optional;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.queryhandling.SubscriptionQueryMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.coin.api.event.CoinAddedEvent;
import com.weareblox.assessment.coin.api.event.CoinPriceUpdatedEvent;
import com.weareblox.assessment.coin.api.event.CoinRegisteredEvent;
import com.weareblox.assessment.coin.api.event.CoinRemovedEvent;
import com.weareblox.assessment.coin.api.query.FindCoinQuery;
import com.weareblox.assessment.coin.dto.Coin;
import com.weareblox.assessment.coin.jpa.CoinEntity;
import com.weareblox.assessment.coin.jpa.CoinRepository;

import lombok.RequiredArgsConstructor;

/**
 * This is the projection class that handled the query related to the Coin.
 * Also handles all the event for the coin.
 */
@Service
@ProcessingGroup("coin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoinProjection {

    @Autowired
	private final CoinRepository repository;
    @Autowired
    private final QueryUpdateEmitter updateEmitter;
    
    /**
     * The handle for coin registered event.
     * This updates the coin the database.
     * @param event CoinRegisteredEvent
     */
    @EventHandler
    public void on(CoinRegisteredEvent event) {
    	CoinEntity coinEntity = toEntity(event);
    	if(coinEntity == null) {
    		return;
    	}
    	repository.saveAndFlush(coinEntity);
    }
    
    /**
     * This handles the event for update of price of a Coin.
     * Update price of Coin in database.
     * @param event
     */
    @EventHandler
    public void on(CoinPriceUpdatedEvent event) {
    	Optional<CoinEntity> entity = repository.findById(event.getCoinId());
    	if(entity.isPresent()) {
    		CoinEntity coinEntity = entity.get();
    		coinEntity.setPrice(event.getPrice());
    		repository.save(coinEntity);
    		
    		SubscriptionQueryMessage queryMessage =  updateEmitter.activeSubscriptions().iterator().next();
    		System.out.println(queryMessage.getIdentifier() 
    				+ " "+queryMessage.getQueryName()
    				+ " "+queryMessage.getPayloadType());
    		
    		System.out.println(queryMessage.getMetaData());
    		
    		System.out.println(updateEmitter.queryUpdateHandlerRegistered(queryMessage));

    		updateEmitter.emit(FindCoinQuery.class,
                    query -> true,
                    Optional.of(coinEntity).map(this::toDto));
    	}
    	
    }
    
    /**
     * This handles the event for addition of the coin in the repository.
     * Updates the quantity of the coin in the database.
     * @param event
     */
    @EventHandler
    public void on(CoinAddedEvent event) {
    	Optional<CoinEntity> entity = repository.findById(event.getCoinId());
    	if(entity.isPresent()) {
    		CoinEntity coinEntity = entity.get();
    		coinEntity.setBalance(coinEntity.getBalance()+event.getQuantity());
    		repository.save(coinEntity);
    		updateEmitter.emit(FindCoinQuery.class,
    	                query -> Objects.equals(query.getCoinId(), event.getCoinId()),
    	                Optional.of(coinEntity).map(this::toDto));
    	}

    }
    
    /**
    * This handles the event for addition of the coin in the repository.
     * Updates the quantity of the coin in the database.
     * @param event
     */
    @EventHandler
    public void on(CoinRemovedEvent event) {
    	System.out.println("coin removed event - projection" + event.getQuantity() + "opening balance "+event.getOpeningBalance());
    	Optional<CoinEntity> entity = repository.findById(event.getCoinId());
    	if(entity.isPresent()) {
    		CoinEntity coinEntity = entity.get();
    		if(coinEntity.getBalance()-event.getQuantity() < 0) {
    			return;
    		}
    		coinEntity.setBalance(coinEntity.getBalance()-event.getQuantity());
    		repository.save(coinEntity);
    		
    		System.out.println(updateEmitter.activeSubscriptions());
    		
    		
    		updateEmitter.emit(FindCoinQuery.class,
    	                query -> Objects.equals(query.getCoinId(), event.getCoinId()),
    	                Optional.of(coinEntity).map(this::toDto));
    	}

    }

    /**
     * Query handle to find a specific coin
     * @param query FindCoinQuery query
     * @return Optional<Coin> 
     */
    @QueryHandler()
    public Coin findCoin(FindCoinQuery query) {
        return repository.findById(query.getCoinId())
                .map(this::toDto).get();
    }

    
    /**
	 * converter method to convert the Coin entity to the DTO
	 * @param entity Coin entity to be converted
	 * @return Coin DTO
	 */
    private Coin toDto(CoinEntity entity) {
		if(entity == null) {
            return null;
        }
		return new Coin(entity.getId(),
				entity.getName(),
				entity.getDescription(),
				entity.getLogoUrl(),
				entity.getBalance(),
				entity.getPrice(),
				entity.getCommision());
		
    }
    
    /**
	 * utility method to fill the Coin entity from a registered
	 * @param event Event for Coin registration
	 * @return CoinEntity coin entity to be persisted in DB
	 */
    private CoinEntity toEntity(CoinRegisteredEvent event) {
		if(event == null) {
            return null;
        }
		CoinEntity entity = new CoinEntity();
		entity.setId(event.getCoinId());
		entity.setDescription(event.getDescription());
		entity.setLogoUrl(event.getLogoUrl());
		entity.setName(event.getName());
		entity.setBalance(event.getBalance());
		entity.setPrice(event.getPrice());
		entity.setCommision(event.getCommission());
		return entity;
    }
		
}