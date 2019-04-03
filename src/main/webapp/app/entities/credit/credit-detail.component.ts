import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICredit } from 'app/shared/model/credit.model';

@Component({
    selector: 'jhi-credit-detail',
    templateUrl: './credit-detail.component.html'
})
export class CreditDetailComponent implements OnInit {
    credit: ICredit;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ credit }) => {
            this.credit = credit;
        });
    }

    previousState() {
        window.history.back();
    }
}
