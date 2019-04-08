import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, AccountService, Account } from 'app/core';
import { ShareBuyModalService } from 'app/entities/share/share-buy-modal.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
    account: Account;
    modalRef: NgbModalRef;
    // Ticker display
    defaultCategories: { name: string; symbols: string[] }[] = [
        { name: 'Market ETFs', symbols: ['SPY', 'DIA', 'QQQ', 'IWM'] },
        { name: 'Sector ETFs', symbols: ['XLF', 'XLK', 'XLC', 'XLV', 'XLP', 'XLY', 'XLE', 'XLB', 'XLI', 'XLU', 'XLRE'] },
        { name: 'Banks', symbols: ['GS', 'MS', 'JPM', 'WFC', 'C', 'BAC', 'BCS', 'DB', 'CS', 'RBS'] },
        {
            name: 'Tech',
            symbols: [
                'AAPL',
                'GOOGL',
                'MSFT',
                'AMZN',
                'FB',
                'TWTR',
                'NFLX',
                'LYFT',
                'SNAP',
                'SPOT',
                'DBX',
                'SQ',
                'SFIX',
                'BABA',
                'INTC',
                'AMD',
                'NVDA',
                'ORCL'
            ]
        },
        { name: 'Cryptos', symbols: ['BTCUSDT', 'ETHUSDT'] },
        { name: 'Bond ETFs', symbols: ['BND', 'BIV', 'JNK'] },
        {
            name: 'Other ETFs',
            symbols: [
                'VOO',
                'VTI',
                'VGK',
                'VPL',
                'VWO',
                'VDE',
                'XOP',
                'VFH',
                'VHT',
                'VIG',
                'VYM',
                'VAW',
                'REM',
                'XHB',
                'XRT',
                'GLD',
                'SHV',
                'FLOT',
                'BKLN',
                'MJ'
            ]
        },
        { name: 'Mortgage REITs', symbols: ['EFC', 'EARN', 'NLY', 'AGNC', 'CIM', 'TWO', 'NRZ'] },
        { name: 'Autos', symbols: ['F', 'GM', 'FCAU', 'TM', 'HMC', 'TSLA'] },
        { name: 'Airlines', symbols: ['DAL', 'LUV', 'UAL', 'AAL', 'ALK', 'JBLU', 'SAVE'] },
        { name: 'BigCos', symbols: ['XOM', 'WMT', 'JNJ', 'GE', 'T', 'KO', 'DIS', 'MCD', 'PG'] }
    ];
    allSymbols: string[];
    refreshSeconds = 10;
    batchSize = 100;
    baseURL = 'https://api.iextrading.com/1.0/stock/market/batch';
    tickerTable: { [symbol: string]: { latestPrice: number; change: number; changePercent: number; marketCap: number } } = {};
    lastUpdated: Date;
    intervalId;

    constructor(
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private shareBuyModalService: ShareBuyModalService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        this.allSymbols = [];
        this.defaultCategories.forEach(category => {
            this.allSymbols = this.allSymbols.concat(category.symbols);
        });
        this.allSymbols.forEach(symbol => {
            this.tickerTable[symbol] = { latestPrice: 0, change: 0, changePercent: 0, marketCap: 0 };
        });

        this.updateData();
        this.intervalId = setInterval(() => {
            this.updateData();
        }, this.refreshSeconds * 1000);
    }

    ngOnDestroy() {
        clearInterval(this.intervalId);
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    buyShare() {
        this.modalRef = this.shareBuyModalService.open();
    }

    symbolUrl(symbol) {
        return `https://iextrading.com/apps/stocks/${symbol}`;
    }

    updateData() {
        const numberOfBatches = Math.ceil(this.allSymbols.length / this.batchSize);
        for (let i = 0; i < numberOfBatches; i++) {
            const symbolsBatch = this.allSymbols.slice(i * this.batchSize, (i + 1) * this.batchSize);
            this.updateDataForBatch(symbolsBatch);
        }
        this.lastUpdated = new Date();
    }

    updateDataForBatch(symbols) {
        const filters = ['latestPrice', 'change', 'changePercent', 'marketCap'];
        const url = `${this.baseURL}?types=quote&symbols=${symbols.join(',')}&filter=${filters.join(',')}`;

        fetch(url)
            .then(response => response.json())
            .then(json => {
                symbols.forEach(symbol => {
                    const data = json[symbol];
                    if (typeof data === 'undefined') {
                        return;
                    }
                    this.tickerTable[symbol].latestPrice = data.quote.latestPrice;
                    this.tickerTable[symbol].change = data.quote.change;
                    this.tickerTable[symbol].changePercent = data.quote.changePercent;
                    this.tickerTable[symbol].marketCap = data.quote.marketCap;
                });
            });
    }
}
