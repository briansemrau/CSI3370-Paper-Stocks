package com.csi3370.paperstocks.service;

import com.csi3370.paperstocks.domain.*;
import com.csi3370.paperstocks.repository.CreditRepository;
import com.csi3370.paperstocks.repository.ShareRepository;
import com.csi3370.paperstocks.repository.TransactionRepository;
import com.csi3370.paperstocks.repository.UserRepository;
import com.csi3370.paperstocks.security.SecurityUtils;
import com.csi3370.paperstocks.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zankowski.iextrading4j.api.marketdata.LastTrade;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

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

    private final TransactionRepository transactionRepository;

    public ShareService(ShareRepository shareRepository, StockDataService stockDataService, UserRepository userRepository, CreditRepository creditRepository, TransactionRepository transactionRepository) {
        this.shareRepository = shareRepository;
        this.stockDataService = stockDataService;
        this.userRepository = userRepository;
        this.creditRepository = creditRepository;
        this.transactionRepository = transactionRepository;
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
     * Get all the shares in a portfolio.
     *
     * @param portfolioId the portfolio ID
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Share> findAllByPortfolioId(Long portfolioId, Pageable pageable) {
        log.debug("Request to get all Shares in Portfolio");
        return shareRepository.findAllByPortfolioId(portfolioId, pageable);
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

    public Share buyShare(Share share) {
        LastTrade lastTrade;
        lastTrade = stockDataService.getLastTrade(share.getTicker());

        if (lastTrade == null) {
            throw new BadRequestAlertException("Ticker symbol must be a tradeable stock.", "share", "idnull");
        }

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
                    //check that a user has enough money to buy the shares
                    if((myValue-(price*numShares))>0)
                    {
                        double newValue = myValue - (price * numShares);
                        myCredit.get().setCredit(newValue);

                        creditRepository.save(myCredit.get());

                        //gets the current time for the Transaction
                        Instant now = Instant.now();

                        //crafts the Transaction to submit to the repository
                        Transaction newTransaction = new Transaction().pricePerShare(price).date(now)
                            .portfolio(share.getPortfolio()).quantity(share.getQuantity()).ticker(share.getTicker());

                        //sumbits the transaction to the transaction repository
                        transactionRepository.save(newTransaction);
                    }else{
                        //throw an error
                        log.debug("User did not have enough money to buy shares");
                        throw new BadRequestAlertException("No money :(", "share", "idnull");
                    }



                    Optional<Share> existingShare = shareRepository.findOneByTickerAndPortfolioId(share.getTicker(), share.getPortfolio());
                    if (existingShare.isPresent()) {
                        existingShare.get().setQuantity(existingShare.get().getQuantity() + share.getQuantity());
                        shareRepository.save(existingShare.get());
                        return shareRepository.save(existingShare.get());
                    } else {
                        shareRepository.save(share);
                        return shareRepository.save(share);
                    }
                }
            }
        }
    return share;
    }

    public Share sellShare(Share share) {
        LastTrade lastTrade;
        lastTrade = stockDataService.getLastTrade(share.getTicker());
        double price = lastTrade.getPrice().doubleValue();
        double numShares = share.getQuantity();
        log.debug("Request to buy Share : {}", share);
        //gets the current time
        Instant now = Instant.now();
        //
        Optional<Share> existingShare = shareRepository.findOneByTickerAndPortfolioId(share.getTicker(), share.getPortfolio());



        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
            if (user.isPresent()) {
                Optional<Credit> myCredit = creditRepository.findById(user.get().getId());
                if (myCredit.isPresent()) {
                    //check to make sure that a user has enough shares in their account to sell
                    if(existingShare.isPresent() && (existingShare.get().getQuantity()>share.getQuantity())) {
                        double myValue = myCredit.get().getCredit();
                        double newValue = myValue + (price * numShares);

                        //crafts the Transaction to submit to the repository
                        Transaction newTransaction = new Transaction().pricePerShare(price).date(now).portfolio(share.getPortfolio())
                            .quantity(share.getQuantity()).ticker(share.getTicker());

                        //sumbits the transaction to the transaction repository
                        transactionRepository.save(newTransaction);

                        //sets the users credit to the amount they had before the sale, plus the proceeds
                        myCredit.get().setCredit(newValue);

                        //subtract the shares from the users account
                        existingShare.get().setQuantity(existingShare.get().getQuantity() - share.getQuantity());
                        shareRepository.save(existingShare.get());
                        creditRepository.save(myCredit.get());
                        if (existingShare.get().getQuantity() == 0) {
                            shareRepository.delete(share);
                            return shareRepository.save(existingShare.get());

                        }
                    } else {
                        shareRepository.save(share);
                        return shareRepository.save(existingShare.get());
                    }



                }
            }
        }
        return share;

    }

}
