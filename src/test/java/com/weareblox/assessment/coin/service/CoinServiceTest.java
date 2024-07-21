package com.weareblox.assessment.coin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weareblox.assessment.coin.dto.Coin;
import com.weareblox.assessment.coin.exception.CoinNotFoundException;
import com.weareblox.assessment.coin.jpa.CoinEntity;
import com.weareblox.assessment.coin.jpa.CoinRepository;

/**
 * Test for Coin service
 */
@ExtendWith(MockitoExtension.class)
public class CoinServiceTest {

	@Mock CoinRepository coinRepository;
	
	@Mock private CommandGateway commandGateway;
	
	@Mock private QueryGateway queryGateway;
	
	@InjectMocks
	private CoinService service;
	
	private static Coin coin;
	
	/**
	 * initial setup
	 */
	@BeforeEach
	void setup() {
		coin = new Coin(UUID.randomUUID().toString(), "BitCoin", 
				"https://logourl", "Bitcoin", 10, BigDecimal.TEN, BigDecimal.TEN);
	}
	
	/**
	 * Registering a coin
	 */
	@Test
	void registerCoin() {	
		when(commandGateway.send(Mockito.any(Object.class)))
		.thenReturn((CompletableFuture.completedFuture(coin)));	
		assertThat(service.registerCoin(coin)).isCompletedWithValue(coin);
		
	}
	

	/**
	 * Adding more coins for a coin
	 */
	@Test
	void addCoins() {
		String coinId = UUID.randomUUID().toString();		
		CoinEntity coinEntity = new CoinEntity();
		when(coinRepository.findById(coinId)).thenReturn(Optional.of(coinEntity));
		when(commandGateway.send(Mockito.any(Object.class)))
		.thenReturn((CompletableFuture.completedFuture(coin)));	
		
		assertThat(service.addCoins(coinId,10)).isCompletedWithValue(coin);
		
	}
	
	/**
	 * Trying to add coin for a non existent coin
	 */
	@Test
	void addCoinstoNonExistentCoin() {
		String coinId = UUID.randomUUID().toString();	
		CoinEntity coinEntity = null;
		when(coinRepository.findById(coinId)).thenReturn(Optional.ofNullable(coinEntity));
		assertThrows(CoinNotFoundException.class, () -> {
			service.addCoins(coinId,10);
	    });
	}
	
	
	/**
	 * Trying to update price of a coin
	 */
	@Test
	void upatePrice() {	
		String coinId = UUID.randomUUID().toString();	
		CoinEntity coinEntity = new CoinEntity();
		when(coinRepository.findById(coinId)).thenReturn(Optional.ofNullable(coinEntity));
		when(commandGateway.send(Mockito.any(Object.class)))
		.thenReturn((CompletableFuture.completedFuture(coin)));	
		
		assertThat(service.updatePrice(coinId,BigDecimal.ZERO)).isCompletedWithValue(coin);
	}
	
	/**
	 * Trying to update price of an non existent coin.
	 */
	@Test
	void upatePriceNonExistentCoin() {
		String coinId = UUID.randomUUID().toString();	
		CoinEntity coinEntity = null;
		when(coinRepository.findById(coinId)).thenReturn(Optional.ofNullable(coinEntity));
		assertThrows(CoinNotFoundException.class, () -> {
			service.updatePrice(coinId, BigDecimal.TEN);
	    });
	}
}
