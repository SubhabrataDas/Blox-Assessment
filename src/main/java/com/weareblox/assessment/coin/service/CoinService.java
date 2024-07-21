package com.weareblox.assessment.coin.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.coin.api.command.AddCoinCommand;
import com.weareblox.assessment.coin.api.command.RegisterCoinCommand;
import com.weareblox.assessment.coin.api.command.UpdateCoinPriceCommand;
import com.weareblox.assessment.coin.api.query.FindCoinQuery;
import com.weareblox.assessment.coin.dto.Coin;
import com.weareblox.assessment.coin.exception.CoinNotFoundException;
import com.weareblox.assessment.coin.jpa.CoinEntity;
import com.weareblox.assessment.coin.jpa.CoinRepository;

import reactor.core.publisher.Flux;

/**
 * Service for the Coin. This allows for adding/patching a coin.
 * Also has accessing methods for the coins
 */
@Service
public class CoinService {
	
	@Autowired
	private CoinRepository coinRepository;
	
	@Autowired
	private CommandGateway commandGateway;
	
	@Autowired
	private QueryGateway queryGateway;
	
	/**
	 * Registers a coin to the the Repository
	 * @param coin Coin DTO with all the information
	 * @return CompletableFuture<Coin> with the eventually registered coin
	 */
	public CompletableFuture<Coin> registerCoin(Coin coin) {

		return commandGateway.send(new RegisterCoinCommand(
									UUID.randomUUID().toString(),
									coin.getName(), 
									coin.getLogoUrl(),
									coin.getDescription(),
									coin.getBalance(),
									coin.getPrice(),
									coin.getCommission()));
		
	}

	/**
	 * gets all the coins in the system. Support pagination
	 * @param pageIndex zero-based page number, must not be negative
	 * @param pageSize the size of the page to be returned, must be greater than 0.
	 * @return
	 */
	public List<Coin> getCoins(int pageIndex,int pageSize){		
		if(pageIndex < 0 || pageSize < 0) {
			throw new IllegalArgumentException("Index or page size cannot be negetive");
		}
		return coinRepository.findAll(PageRequest.of(pageIndex, pageSize)).map(this::toDto).toList();
	}
	
	/**
	 * allows to add some coins to the repository
	 * @param id id of the coin
	 * @param quantity number of coins to be added
	 * @return CompletableFuture<Coin> with the coin information after coins are added
	 * @throws CoinNotFoundException In case the id does not exist in the repository, it will
	 * throw this error
	 */
	public CompletableFuture<Coin> addCoins(String id, Integer quantity) throws CoinNotFoundException{		
		Optional<CoinEntity> coin = coinRepository.findById(id);
		if(coin.isPresent()) {
			AddCoinCommand addCoinCommand = new AddCoinCommand(id,quantity);
			return commandGateway.send(addCoinCommand);
		}
		throw new CoinNotFoundException();
	}
	
	/**
	 * allows to update a coin's price in the repository
	 * @param id id of the coin
	 * @param price price of each coin.
	 * @return CompletableFuture<Coin> with the coin information after coins are added
	 * @throws CoinNotFoundException In case the id does not exist in the repository, it will
	 * throw this error
	 */
	public CompletableFuture<Coin> updatePrice(String id, BigDecimal price) throws CoinNotFoundException{		
		Optional<CoinEntity> coin = coinRepository.findById(id);
		if(coin.isPresent()) {
			return commandGateway.send(new UpdateCoinPriceCommand(coin.get().getId(),price));
		}
		throw new CoinNotFoundException();
	}
	

	/**
	 * allows to get a coin from the repository
	 * @param id id of the coin
	 * @param price price of each coin.
	 * @return Coin with the coin information after coins are added
	 * @throws CoinNotFoundException In case the id does not exist in the repository, it will
	 * throw this error
	 */
	public Coin getCoin(String id) throws CoinNotFoundException{		
		Optional<CoinEntity> coinE = coinRepository.findById(id);
		if(coinE.isPresent()) {
			return toDto(coinE.get());
		}
		throw new CoinNotFoundException();
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
				entity.getBalance()!=null?entity.getBalance():0,
				entity.getPrice(),
				entity.getCommision());
		
	}	
	
}
