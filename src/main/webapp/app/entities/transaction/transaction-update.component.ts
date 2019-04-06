import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from 'app/entities/portfolio';

@Component({
    selector: 'jhi-transaction-update',
    templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
    transaction: ITransaction;
    isSaving: boolean;

    portfolios: IPortfolio[];
    date: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected transactionService: TransactionService,
        protected portfolioService: PortfolioService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ transaction }) => {
            this.transaction = transaction;
            this.date = this.transaction.date != null ? this.transaction.date.format(DATE_TIME_FORMAT) : null;
        });
        this.portfolioService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPortfolio[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPortfolio[]>) => response.body)
            )
            .subscribe((res: IPortfolio[]) => (this.portfolios = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.transaction.date = this.date != null ? moment(this.date, DATE_TIME_FORMAT) : null;
        if (this.transaction.id !== undefined) {
            this.subscribeToSaveResponse(this.transactionService.update(this.transaction));
        } else {
            this.subscribeToSaveResponse(this.transactionService.create(this.transaction));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>) {
        result.subscribe((res: HttpResponse<ITransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPortfolioById(index: number, item: IPortfolio) {
        return item.id;
    }
}
