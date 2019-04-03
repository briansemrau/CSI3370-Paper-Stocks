package com.csi3370.paperstocks.web.rest;
import com.csi3370.paperstocks.domain.Portfolio;
import com.csi3370.paperstocks.repository.PortfolioRepository;
import com.csi3370.paperstocks.web.rest.errors.BadRequestAlertException;
import com.csi3370.paperstocks.web.rest.util.HeaderUtil;
import com.csi3370.paperstocks.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Portfolio.
 */
@RestController
@RequestMapping("/api")
public class PortfolioResource {

    private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);

    private static final String ENTITY_NAME = "portfolio";

    private final PortfolioRepository portfolioRepository;

    public PortfolioResource(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * POST  /portfolios : Create a new portfolio.
     *
     * @param portfolio the portfolio to create
     * @return the ResponseEntity with status 201 (Created) and with body the new portfolio, or with status 400 (Bad Request) if the portfolio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/portfolios")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to save Portfolio : {}", portfolio);
        if (portfolio.getId() != null) {
            throw new BadRequestAlertException("A new portfolio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Portfolio result = portfolioRepository.save(portfolio);
        return ResponseEntity.created(new URI("/api/portfolios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /portfolios : Updates an existing portfolio.
     *
     * @param portfolio the portfolio to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated portfolio,
     * or with status 400 (Bad Request) if the portfolio is not valid,
     * or with status 500 (Internal Server Error) if the portfolio couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/portfolios")
    public ResponseEntity<Portfolio> updatePortfolio(@RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to update Portfolio : {}", portfolio);
        if (portfolio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Portfolio result = portfolioRepository.save(portfolio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, portfolio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /portfolios : get all the portfolios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of portfolios in body
     */
    @GetMapping("/portfolios")
    public ResponseEntity<List<Portfolio>> getAllPortfolios(Pageable pageable) {
        log.debug("REST request to get a page of Portfolios");
        Page<Portfolio> page = portfolioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/portfolios");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /portfolios/:id : get the "id" portfolio.
     *
     * @param id the id of the portfolio to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the portfolio, or with status 404 (Not Found)
     */
    @GetMapping("/portfolios/{id}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long id) {
        log.debug("REST request to get Portfolio : {}", id);
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(portfolio);
    }

    /**
     * DELETE  /portfolios/:id : delete the "id" portfolio.
     *
     * @param id the id of the portfolio to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/portfolios/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        log.debug("REST request to delete Portfolio : {}", id);
        portfolioRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
