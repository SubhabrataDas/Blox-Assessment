package com.weareblox.assessment.controller;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.weareblox.assessment.coin.dto.Coin;
import com.weareblox.assessment.coin.service.CoinService;

/**
 * Test for coin controller
 */
@ExtendWith(MockitoExtension.class)
@WebFluxTest(controllers = CoinController.class)
public class CoinControllerTest {

	@MockBean
	CoinService coinService;

	@InjectMocks
	CoinController coinController;

	@Autowired
	private WebTestClient webClient;

	private Coin requestcoin;

	private Coin persistedcoin;

	/**
	 * initial setup
	 */
	@BeforeEach
	void setup() {
		requestcoin = new Coin(null, "BitCoin", "http://logoUrl", "bitcoin", 10, BigDecimal.ONE, BigDecimal.ONE);

		persistedcoin = new Coin(UUID.randomUUID().toString(), "BitCoin", "http://logoUrl", "bitcoin", 10,
				BigDecimal.ONE, BigDecimal.ONE);

	}

	/**
	 * calling api to register a coin
	 */
	@Test
	void testRegisterCoin() {
		when(coinService.registerCoin(requestcoin)).thenReturn(CompletableFuture.completedFuture(persistedcoin));
		webClient.post().uri("/coin").contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(requestcoin))
		.exchange().expectStatus().isCreated().expectBody(Coin.class).isEqualTo(persistedcoin);

	}

	/**
	 * calling api to register a coin with invalid input
	 */
	@Test
	void testRegisterCoinBadInput() {
		//bad input
		Coin badCoin = new Coin(null, null, "http://logoUrl", "bitcoin", 10, BigDecimal.ONE, BigDecimal.ONE);
		webClient.post().uri("/coin").contentType(MediaType.APPLICATION_JSON).
		body(BodyInserters.fromValue(badCoin))
		.exchange().expectStatus().isBadRequest()
		.expectBody().json("{\"name\":\"Coin name cannot be empty\"}");

	}

	/**
	 * test for getting coins registered in the system
	 */
	@Test
	void testGetCoins() {
		List<Coin> savedCoins = IntStream.range(0, 10).mapToObj(x ->
		new Coin(UUID.randomUUID().toString(), "BitCoin",
		"http://logoUrl", "bitcoin", 10, BigDecimal.ONE, BigDecimal.ONE)).
		collect(Collectors.toList());

		when(coinService.getCoins(0, 10)).thenReturn(savedCoins);

		webClient.get().uri("/coin").exchange().expectStatus().isOk().expectBodyList(Coin.class).hasSize(10);

	}
	

}
