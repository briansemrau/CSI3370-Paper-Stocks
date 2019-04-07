package com.csi3370.paperstocks.repository;

import com.csi3370.paperstocks.domain.Portfolio;
import com.csi3370.paperstocks.domain.Share;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Share entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query("select share from Share share where share.portfolio.id = :#{#portfolio.id} and share.ticker = :ticker")
    Optional<Share> findOneByTickerAndPortfolioId(@Param("ticker") String ticker, @Param("portfolio") Portfolio portfolio);

}
