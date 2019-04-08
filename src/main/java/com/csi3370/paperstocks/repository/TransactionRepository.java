package com.csi3370.paperstocks.repository;

import com.csi3370.paperstocks.domain.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByPortfolio(@NotNull Portfolio portfolio);

    @Query("select transaction from Transaction transaction where transaction.portfolio.user.login = ?#{principal.username}")
    Page<Transaction> findByUserIsCurrentUser(Pageable pageable);

}
