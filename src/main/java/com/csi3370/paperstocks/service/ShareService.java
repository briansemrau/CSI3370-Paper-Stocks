package com.csi3370.paperstocks.service;

import com.csi3370.paperstocks.domain.*;
import com.csi3370.paperstocks.repository.ShareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing Share.
 */
@Service
@Transactional
public class ShareService {

    private final Logger log = LoggerFactory.getLogger(ShareService.class);

    private final ShareRepository shareRepository;

    private final CacheManager cacheManager;

    public ShareService(ShareRepository shareRepository, CacheManager cacheManager) {
        this.shareRepository = shareRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Save a share.
     *
     * @param share the entity to save
     * @return the persisted entity
     */
    public Share save(Share share) {
        log.debug("Request to save Share : {}", share);
        Share result = shareRepository.save(share);
        clearShareCache(result);
        return result;
    }

    /**
     * Get all the shares.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Share> findAll(Pageable pageable) {
        log.debug("Request to get all Shares");
        return shareRepository.findAll(pageable);
    }


    /**
     * Get one share by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Share> findOne(Long id) {
        log.debug("Request to get Share : {}", id);
        return shareRepository.findById(id);
    }

    /**
     * Delete the share by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Share : {}", id);
        shareRepository.findById(id).ifPresent(share -> {
            shareRepository.delete(share);
            this.clearShareCache(share);
        });
    }

    private void clearShareCache(Share share) {
        //Objects.requireNonNull(cacheManager.getCache(Portfolio.class.getName() + ".shares")).evict(share);
    }

}
