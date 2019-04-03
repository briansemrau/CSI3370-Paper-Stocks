package com.csi3370.paperstocks.repository;

import com.csi3370.paperstocks.domain.Portfolio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Portfolio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("select portfolio from Portfolio portfolio where portfolio.user.login = ?#{principal.username}")
    List<Portfolio> findByUserIsCurrentUser();

}
