import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from './portfolio.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-portfolio-update',
    templateUrl: './portfolio-update.component.html'
})
export class PortfolioUpdateComponent implements OnInit {
    portfolio: IPortfolio;
    isSaving: boolean;

    users: IUser[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected portfolioService: PortfolioService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ portfolio }) => {
            this.portfolio = portfolio;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.portfolio.id !== undefined) {
            this.subscribeToSaveResponse(this.portfolioService.update(this.portfolio));
        } else {
            this.subscribeToSaveResponse(this.portfolioService.create(this.portfolio));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortfolio>>) {
        result.subscribe((res: HttpResponse<IPortfolio>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
