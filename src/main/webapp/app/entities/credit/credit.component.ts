import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICredit } from 'app/shared/model/credit.model';
import { AccountService } from 'app/core';
import { CreditService } from './credit.service';

@Component({
    selector: 'jhi-credit',
    templateUrl: './credit.component.html'
})
export class CreditComponent implements OnInit, OnDestroy {
    credits: ICredit[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected creditService: CreditService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.creditService
            .query()
            .pipe(
                filter((res: HttpResponse<ICredit[]>) => res.ok),
                map((res: HttpResponse<ICredit[]>) => res.body)
            )
            .subscribe(
                (res: ICredit[]) => {
                    this.credits = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCredits();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICredit) {
        return item.id;
    }

    registerChangeInCredits() {
        this.eventSubscriber = this.eventManager.subscribe('creditListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
