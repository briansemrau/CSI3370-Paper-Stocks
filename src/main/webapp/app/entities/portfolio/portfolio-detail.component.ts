import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPortfolio } from 'app/shared/model/portfolio.model';

@Component({
    selector: 'jhi-portfolio-detail',
    templateUrl: './portfolio-detail.component.html'
})
export class PortfolioDetailComponent implements OnInit {
    portfolio: IPortfolio;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ portfolio }) => {
            this.portfolio = portfolio;
        });
    }

    previousState() {
        window.history.back();
    }
}
