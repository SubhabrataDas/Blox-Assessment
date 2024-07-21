package com.weareblox.assessment.controller;

import java.time.Duration;

import javax.validation.Valid;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.weareblox.assessment.coin.api.query.FindCoinQuery;
import com.weareblox.assessment.coin.dto.Coin;
import com.weareblox.assessment.coin.dto.CoinPatch;
import com.weareblox.assessment.coin.service.CoinService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/coin")
public class CoinController {

	@Autowired
	private CoinService coinService;

	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Registers a coin in the system")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The coin has been successfully registered in the system. "
					+ "This returns a CompletableFuture for async processing.", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = Coin.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid input provided supplied", content = @Content) })
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Coin> register(@Valid @RequestBody Coin coin) {
		return Mono.fromFuture(coinService.registerCoin(coin));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Gets all the coins in the system")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returns the coins in the system") })
	@GetMapping()
	public Flux<Coin> getCoins(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		return Flux.fromIterable(coinService.getCoins(pageIndex, pageSize));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Update the price or add more coins")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returns the coins in the system") })
	@PatchMapping("/{id}")
	public Mono<Coin> add(@Parameter(description = "id of the coin to patch") @PathVariable("id") String id,
			@RequestBody CoinPatch coinpatch) {
		if (coinpatch.getPrice() != null) {
			return Mono.fromFuture(coinService.updatePrice(id, coinpatch.getPrice()));
		} else if (coinpatch.getQuantity() != null) {
			return Mono.fromFuture(coinService.addCoins(id, coinpatch.getQuantity()));
		}
		throw new IllegalArgumentException("Other attributes of coins are not patchable");
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Returns details of a coin. This returns a flux. "
			+ "Thus the client can register and get updated ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returns the price of the coin") })
	@GetMapping(path = "/{id}")
	public Coin getCoinUpates(@Parameter(description = "Id of the coin") @PathVariable("id") String id) {
		return coinService.getCoin(id);
	}

}
