package com.csi3370.paperstocks.service;

import com.csi3370.paperstocks.domain.Portfolio;
import com.csi3370.paperstocks.repository.*;
import com.csi3370.paperstocks.web.rest.errors.PortfolioNotEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Portfolio.
 */
@Service
@Transactional
public class PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioService.class);

    private final PortfolioRepository portfolioRepository;

    private final CacheManager cacheManager;

    public PortfolioService(PortfolioRepository portfolioRepository, CacheManager cacheManager) {
        this.portfolioRepository = portfolioRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Save a portfolio.
     *
     * @param portfolio the entity to save
     * @return the persisted entity
     */
    public Portfolio save(Portfolio portfolio) {
        log.debug("Request to save Portfolio : {}", portfolio);
        return portfolioRepository.save(portfolio);
    }

    /**
     * Get all the portfolios.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Portfolio> findAll() {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAll();
    }

    /**
     * Get all the portfolios owned by the current user.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Portfolio> findByUserIsCurrentUser() {
        log.debug("Request to get all Portfolios by current User");
        return portfolioRepository.findByUserIsCurrentUser();
    }

    /**
     * Get one portfolio by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Portfolio> findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        return portfolioRepository.findById(id);
    }

    /**
     * Delete the portfolio by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        if (portfolio.isPresent() &&
            !portfolio.get().getShares().isEmpty()) {
            throw new PortfolioNotEmptyException();
        }
        portfolioRepository.deleteById(id);
    }
}
