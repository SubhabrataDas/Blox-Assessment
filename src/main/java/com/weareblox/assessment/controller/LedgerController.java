package com.weareblox.assessment.controller;

import static org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf;
import static reactor.core.publisher.Flux.defer;

import java.util.List;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weareblox.assessment.ledger.api.query.FindLedgerQueryByCustomer;
import com.weareblox.assessment.ledger.dto.Ledger;
import com.weareblox.assessment.ledger.service.LedgerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Contorller for Ledger operations. Allows to get ledgers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(
        path = "/ledger")
public class LedgerController {

    private final QueryGateway queryGateway;
    @Autowired LedgerService ledgerService;


    @Operation(summary = "Gets the ledger in the system for a perticular customer")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Returns the ledgers in the system"
			  		+ "Returns a mono with ledger")
			  
			  })
    @GetMapping()
    public Mono<List<Ledger>> getLedgers(@RequestParam(name= "customerId", required = false)
    String customerId) {
    	return Mono.justOrEmpty(ledgerService.getLedgerForCustomer(customerId));
    
    }
    
    @Operation(summary = "Gets the ledger in the system. Clients can subscribe for updates")
   	@ApiResponses(value = { 
   			  @ApiResponse(responseCode = "200", description = "Returns the ledgers in the system"
   			  		+ "Returns a mono to subscribe for updates.")
   			  
   			  })
       @GetMapping("/subscribe")
       public Mono<List<Ledger>> getLedgerSubscription(@RequestParam(name= "customerId", required = false) String customerId) {
         
       	var queryResult = queryGateway.subscriptionQuery(
                   new FindLedgerQueryByCustomer(customerId),
                   multipleInstancesOf(Ledger.class),
                   multipleInstancesOf(Ledger.class));

           
           return queryResult.initialResult()
                   .concatWith(defer(queryResult::updates))
                   .flatMap(Mono::justOrEmpty)
                   .next()
                   .doFinally(unused -> queryResult.cancel());
       }
    
    
}
