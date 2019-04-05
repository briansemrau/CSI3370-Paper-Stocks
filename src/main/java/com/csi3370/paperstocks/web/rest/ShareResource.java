package com.csi3370.paperstocks.web.rest;
import com.csi3370.paperstocks.domain.Share;
import com.csi3370.paperstocks.security.AuthoritiesConstants;
import com.csi3370.paperstocks.service.ShareService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Share.
 */
@RestController
@RequestMapping("/api")
public class ShareResource {

    private final Logger log = LoggerFactory.getLogger(ShareResource.class);

    private static final String ENTITY_NAME = "share";

    private final ShareService shareService;

    public ShareResource(ShareService shareService) {
        this.shareService = shareService;
    }

    /**
     * POST  /shares : Create a new share.
     *
     * @param share the share to create
     * @return the ResponseEntity with status 201 (Created) and with body the new share, or with status 400 (Bad Request) if the share has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shares")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Share> createShare(@Valid @RequestBody Share share) throws URISyntaxException {
        log.debug("REST request to save Share : {}", share);
        if (share.getId() != null) {
            throw new BadRequestAlertException("A new share cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Share result = shareService.save(share);
        return ResponseEntity.created(new URI("/api/shares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shares : Updates an existing share.
     *
     * @param share the share to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated share,
     * or with status 400 (Bad Request) if the share is not valid,
     * or with status 500 (Internal Server Error) if the share couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shares")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Share> updateShare(@Valid @RequestBody Share share) throws URISyntaxException {
        log.debug("REST request to update Share : {}", share);
        if (share.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Share result = shareService.save(share);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, share.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shares : get all the shares.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shares in body
     */
    @GetMapping("/shares")
    public ResponseEntity<List<Share>> getAllShares(Pageable pageable) {
        log.debug("REST request to get a page of Shares");
        Page<Share> page = shareService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shares");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /shares/:id : get the "id" share.
     *
     * @param id the id of the share to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the share, or with status 404 (Not Found)
     */
    @GetMapping("/shares/{id}")
    public ResponseEntity<Share> getShare(@PathVariable Long id) {
        log.debug("REST request to get Share : {}", id);
        Optional<Share> share = shareService.findOne(id);
        return ResponseUtil.wrapOrNotFound(share);
    }

    /**
     * DELETE  /shares/:id : delete the "id" share.
     *
     * @param id the id of the share to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shares/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteShare(@PathVariable Long id) {
        log.debug("REST request to delete Share : {}", id);
        shareService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
