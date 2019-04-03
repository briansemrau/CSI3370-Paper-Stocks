package com.csi3370.paperstocks.repository;

import com.csi3370.paperstocks.domain.Credit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Credit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

}
