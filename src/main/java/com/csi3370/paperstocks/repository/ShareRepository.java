package com.csi3370.paperstocks.repository;

import com.csi3370.paperstocks.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Spring Data  repository for the Share entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {

    List<Share> findAllByPortfolio(@NotNull Portfolio portfolio);

}
