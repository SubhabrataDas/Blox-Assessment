package com.weareblox.assessment.ledger.api.query;

import lombok.Value;


/**
 * this is a query to find a ledger by customer id
 */
@Value
public class FindLedgerQueryByCustomer {
	String customerId;
}
