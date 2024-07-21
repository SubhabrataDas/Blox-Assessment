package com.weareblox.assessment.ledger.api.query;

import lombok.Value;


/**
 * This is the query to find a Ledger by id
 */
@Value
public class FindLedgerQuery {
    String ledgerId;
}
