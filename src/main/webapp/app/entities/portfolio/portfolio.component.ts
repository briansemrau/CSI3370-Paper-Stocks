import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPortfolio } from 'app/shared/model/portfolio.model';
import { AccountService } from 'app/core';
import { PortfolioService } from './portfolio.service';

@Component({
    selector: 'jhi-portfolio',
    templateUrl: './portfolio.component.html'
})
export class PortfolioComponent implements OnInit, OnDestroy {
    portfolios: IPortfolio[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected portfolioService: PortfolioService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.portfolioService
            .query()
            .pipe(
                filter((res: HttpResponse<IPortfolio[]>) => res.ok),
                map((res: HttpResponse<IPortfolio[]>) => res.body)
            )
            .subscribe(
                (res: IPortfolio[]) => {
                    this.portfolios = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPortfolios();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPortfolio) {
        return item.id;
    }

    registerChangeInPortfolios() {
        this.eventSubscriber = this.eventManager.subscribe('portfolioListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
