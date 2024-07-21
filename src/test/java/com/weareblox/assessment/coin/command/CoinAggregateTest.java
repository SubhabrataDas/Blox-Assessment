package com.weareblox.assessment.coin.command;

import java.math.BigDecimal;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.weareblox.assessment.coin.api.command.AddCoinCommand;
import com.weareblox.assessment.coin.api.command.RemoveCoinCommand;
import com.weareblox.assessment.coin.api.command.UpdateCoinPriceCommand;
import com.weareblox.assessment.coin.api.event.CoinAddedEvent;
import com.weareblox.assessment.coin.api.event.CoinRegisteredEvent;
import com.weareblox.assessment.coin.api.event.CoinRemovedEvent;

public class CoinAggregateTest {
	
	private static final int BALANCE = 0;
	
	private static final BigDecimal PRICE = BigDecimal.TEN;
	
	private static final String COINNAME = "bitcoin";
	
	private static final String DESCRIPTION = "description";
	
	private static final String LOGOURL = "http://logourl";
	
	private static final String COINID = UUID.randomUUID().toString();
	
	private static final String ORDERID = UUID.randomUUID().toString();
	
	private static final int QUANTITY = 10;
	
	private static final BigDecimal COMMISSION = BigDecimal.TEN;

	private AggregateTestFixture<CoinAggregate> fixture;

	@BeforeEach
	void setup() {
		fixture = new AggregateTestFixture<>(CoinAggregate.class);
	}
	
	
	/**
	 * Test for adding a coin
	 */
	@Test
    void addCoin() {
        fixture.given(new CoinRegisteredEvent(COINID,COINNAME,LOGOURL,DESCRIPTION,BALANCE,PRICE,COMMISSION))
                .when(new AddCoinCommand(COINID, QUANTITY, ORDERID,false))
                .expectEvents(new CoinAddedEvent(COINID, QUANTITY, ORDERID,false));
    }
	
	/**
	 * Test for trying to remove more coins than present
	 */
	@Test
    void removeCoinMoreThanPresentInRepository() {
        fixture.given(new CoinRegisteredEvent(COINID,COINNAME,LOGOURL,DESCRIPTION,BALANCE,PRICE,COMMISSION))
                .when(new RemoveCoinCommand(COINID, QUANTITY, ORDERID,false))
                .expectEvents(new CoinRemovedEvent(COINID, 0, ORDERID,BALANCE,false));
    }
	
	/**
	 * Test for trying to remove less coins than present
	 */
	@Test
    void removeCoinMoreLessPresentInRepository() {
        fixture.given(new CoinRegisteredEvent(COINID,COINNAME,LOGOURL,DESCRIPTION,10,PRICE,COMMISSION))
                .when(new RemoveCoinCommand(COINID, 2, ORDERID,false))
                .expectEvents(new CoinRemovedEvent(COINID,2, ORDERID,10,false));
    }
	
	
	/**
	 * Test for adding negative price
	 */
	@Test
    void updateCoinPrice() {
        fixture.given(new CoinRegisteredEvent(COINID,COINNAME,LOGOURL,DESCRIPTION,10,PRICE,COMMISSION))
                .when(new UpdateCoinPriceCommand(COINID,  BigDecimal.ONE.negate()))
                .expectNoEvents();
    }
}
