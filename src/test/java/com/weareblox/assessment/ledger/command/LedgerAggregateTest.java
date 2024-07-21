package com.weareblox.assessment.ledger.command;

import com.weareblox.assessment.coin.api.event.CoinRegisteredEvent;
import com.weareblox.assessment.coin.api.event.CoinRemovedEvent;
import com.weareblox.assessment.coin.command.CoinAggregate;
import com.weareblox.assessment.ledger.api.command.CreateLedgerCommand;
import com.weareblox.assessment.ledger.api.command.TransactionType;
import com.weareblox.assessment.ledger.api.event.LedgerCreateFailedEvent;
import com.weareblox.assessment.ledger.api.event.LedgerCreatedEvent;
import com.weareblox.assessment.order.api.event.OrderCreatedEvent;
import com.weareblox.assessment.order.jpa.OrderType;

import java.math.BigDecimal;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LedgerAggregateTest {
    
	private static final String COIN_ID = UUID.randomUUID().toString();
    private static final String ORDER_ID = UUID.randomUUID().toString();
    private static final String LEDGER_ID = UUID.randomUUID().toString();
    private static final String CUSTOMER_ID = UUID.randomUUID().toString();
    
    private static final Integer BUY_AMOUNT = 5;
 
    private AggregateTestFixture<LedgerAggregate> fixture;

    @BeforeEach
    void setup() {
        fixture = new AggregateTestFixture<>(LedgerAggregate.class);
    }

   /**
    * This test a ledger created from beginning
    * Since the order is to BUY 10 coins(consider price of each coin as 1 euro), payment should be 10 euro
    * Also the commission is 10 euro
    * 
    * Thus the opening balance for this transaction  is 0
    *  but closing balance is 0 - 10(payment) - 10(commission) = -20 euro
    *  
    *  Also total coin balance becomes 10 since now there is 10 coins in owner ledger
    * 
    */
   @Test
    void createLedger() {
        fixture
        .givenNoPriorActivity()
         .when(new CreateLedgerCommand(LEDGER_ID,COIN_ID,BigDecimal.TEN,10,
                		TransactionType.BUY, BigDecimal.TEN,ORDER_ID))
                .expectEvents(LedgerCreatedEvent.builder()
                		.ledgerId(LEDGER_ID).orderId(ORDER_ID)
                		.type(TransactionType.BUY)
                		.commision(BigDecimal.TEN)
                		.payment(BigDecimal.TEN)
                		.coinAmount(10)
                		// final coin balance
                		.currentCoinBalance(10)
                		//closing balance
                		.closingBalance(BigDecimal.valueOf(20).negate())
                		.commision(BigDecimal.TEN)
                		.coinId(COIN_ID)
                		//opening balance
                		.openingBalance(BigDecimal.ZERO)
                		.build());
    }
}