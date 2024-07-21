package com.weareblox.assessment.coin.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to handle all database queries /transactions for the Coin entity
 */
public interface CoinRepository extends JpaRepository<CoinEntity, String> {
	
	Optional<CoinPriceProjection> findPriceById(String id);

}
