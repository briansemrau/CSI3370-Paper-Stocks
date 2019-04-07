package com.csi3370.paperstocks.web.rest;

import com.csi3370.paperstocks.Csi3370App;
import com.csi3370.paperstocks.service.StockDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.zankowski.iextrading4j.api.marketdata.LastTrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Csi3370App.class)
public class StockDataServiceIntTest {
    
    @Autowired
    private StockDataService stockDataService;
    @Test
    public void verifyAPIisUp()
    {
        LastTrade lastTradeTest = stockDataService.getLastTrade("AMD");
        assertThat(lastTradeTest.getPrice()).isNotEqualTo(null);
        assertThat(lastTradeTest.getSize()).isNotEqualTo(null);
        assertThat(lastTradeTest.getSymbol()).isNotEqualTo(null);
        assertThat(lastTradeTest.getTime()).isNotEqualTo(null);


    }

}
