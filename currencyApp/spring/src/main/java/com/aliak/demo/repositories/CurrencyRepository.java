package com.aliak.demo.repositories;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.payload.CurrencyBuySell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface CurrencyRepository extends JpaRepository<Currency,Long> {



    @Query("SELECT c FROM Currency c WHERE c.bank IN :banks " +
            "AND c.currency = :currency " +
            "AND c.buy BETWEEN :minBuyPrice AND :maxBuyPrice " +
            "AND c.sell BETWEEN :minSellPrice AND :maxSellPrice " +
            "AND c.date BETWEEN :startDateTime AND :endDateTime")
    List<Currency> searchQueries(
            @Param("banks") List<String> banks,
            @Param("currency") String currency,
            @Param("minBuyPrice") Double minBuyPrice,
            @Param("maxBuyPrice") Double maxBuyPrice,
            @Param("minSellPrice") Double minSellPrice,
            @Param("maxSellPrice") Double maxSellPrice,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    List<Currency> findAllByBankAndCurrencyAndDateBetween(String bank,String currency,LocalDateTime startDate,LocalDateTime endDateTime);

}
