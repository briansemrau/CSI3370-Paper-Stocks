package com.csi3370.paperstocks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zankowski.iextrading4j.api.marketdata.LastTrade;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.marketdata.LastTradeRequestBuilder;

import java.util.List;

@Service
//@Transactional
public class StockDataService {

    private final Logger log = LoggerFactory.getLogger(StockDataService.class);

    private final IEXTradingClient tradingClient = IEXTradingClient.create();

    public LastTrade getLastTrade(String symbol) {
        final List<LastTrade> lastTradeList = tradingClient.executeRequest(new LastTradeRequestBuilder()
            .withSymbol(symbol)
            .build());
        return lastTradeList.get(0);
    }

}
