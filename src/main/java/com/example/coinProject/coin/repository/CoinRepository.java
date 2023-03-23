package com.example.coinProject.coin.repository;

import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.dto.coin.CoinResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoinRepository extends JpaRepository<Coin, Integer> {

    Optional<CoinResponse> findCoinByMarket (String market);
    Coin findFirstByMarket(String market);

    @Modifying
    @Query("UPDATE Coin as c set c.rsi = :rsi where c.market = :market")
    void updateRsi(@Param("rsi")Double rsi, @Param("market")String market);
}
