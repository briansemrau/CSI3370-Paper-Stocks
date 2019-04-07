package com.csi3370.paperstocks.repository;

import com.csi3370.paperstocks.domain.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("select portfolio from Portfolio portfolio where portfolio.user.login = ?#{principal.username}")
    List<Transaction> findByUserIsCurrentUser();
}
