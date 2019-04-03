import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICredit } from 'app/shared/model/credit.model';
import { CreditService } from './credit.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-credit-update',
    templateUrl: './credit-update.component.html'
})
export class CreditUpdateComponent implements OnInit {
    credit: ICredit;
    isSaving: boolean;

    users: IUser[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected creditService: CreditService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ credit }) => {
            this.credit = credit;
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
        if (this.credit.id !== undefined) {
            this.subscribeToSaveResponse(this.creditService.update(this.credit));
        } else {
            this.subscribeToSaveResponse(this.creditService.create(this.credit));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICredit>>) {
        result.subscribe((res: HttpResponse<ICredit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
