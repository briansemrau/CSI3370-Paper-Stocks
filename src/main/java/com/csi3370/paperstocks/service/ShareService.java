package com.csi3370.paperstocks.service;

import com.csi3370.paperstocks.domain.*;
import com.csi3370.paperstocks.repository.CreditRepository;
import com.csi3370.paperstocks.repository.ShareRepository;
import com.csi3370.paperstocks.repository.UserRepository;
import com.csi3370.paperstocks.security.SecurityUtils;
import com.csi3370.paperstocks.web.rest.CreditResource;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import pl.zankowski.iextrading4j.api.marketdata.LastTrade;


import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.csi3370.paperstocks.domain.Credit_.credit;

/**
 * Service Implementation for managing Share.
 */
@Service
@Transactional
public class ShareService {

    private final Logger log = LoggerFactory.getLogger(ShareService.class);

    private final ShareRepository shareRepository;

    private final StockDataService stockDataService;

    private final UserRepository userRepository;

    private final CreditRepository creditRepository;

    public ShareService(ShareRepository shareRepository, StockDataService stockDataService, UserRepository userRepository, CreditRepository creditRepository) {
        this.shareRepository = shareRepository;
        this.stockDataService = stockDataService;
        this.userRepository = userRepository;
        this.creditRepository = creditRepository;
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
        });
    }

    public void buyShare(Share share) {
        LastTrade lastTrade;
        lastTrade = stockDataService.getLastTrade(share.getTicker());
        double price = lastTrade.getPrice().doubleValue();
        double numShares = share.getQuantity();
        log.debug("Request to buy Share : {}", share);

        //reduce the users credit

        // get users credit
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
            if (user.isPresent()) {
                Optional<Credit> myCredit = creditRepository.findById(user.get().getId());
                if (myCredit.isPresent()) {
                    double myValue = myCredit.get().getCredit();
                    double newValue = myValue - (price * numShares);
                    myCredit.get().setCredit(newValue);


                    creditRepository.save(myCredit.get());

                    Optional<Share> existingShare = shareRepository.findOneByTickerAndPortfolioId(share.getTicker(), share.getPortfolio());
                    if (existingShare.isPresent()) {
                        existingShare.get().setQuantity(existingShare.get().getQuantity() + share.getQuantity());
                        shareRepository.save(existingShare.get());
                    } else {
                        shareRepository.save(share);
                    }
                }
            }
        }

    }

    private void sellShare(Share share) {
        LastTrade lastTrade;
        lastTrade = stockDataService.getLastTrade(share.getTicker());
        double price = lastTrade.getPrice().doubleValue();
        double numShares = share.getQuantity();
        log.debug("Request to buy Share : {}", share);


        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
            if (user.isPresent()) {
                Optional<Credit> myCredit = creditRepository.findById(user.get().getId());
                if (myCredit.isPresent()) {
                    double myValue = myCredit.get().getCredit();
                    double newValue = myValue + (price * numShares);

                    myCredit.get().setCredit(newValue);

                    Optional<Share> existingShare = shareRepository.findOneByTickerAndPortfolioId(share.getTicker(), share.getPortfolio());
                    if (existingShare.isPresent()) {
                        existingShare.get().setQuantity(existingShare.get().getQuantity() - share.getQuantity());
                        shareRepository.save(existingShare.get());
                        if(existingShare.get().getQuantity()==0)
                        {
                            shareRepository.delete(share);
                        }
                        else{

                        }
                    } else {
                        shareRepository.save(share);
                    }


                    creditRepository.save(myCredit.get());
                    shareRepository.save(share);
                }
            }
        }

    }

}

    }

