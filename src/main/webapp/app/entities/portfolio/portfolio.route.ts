import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Portfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from './portfolio.service';
import { PortfolioComponent } from './portfolio.component';
import { PortfolioDetailComponent } from './portfolio-detail.component';
import { PortfolioUpdateComponent } from './portfolio-update.component';
import { PortfolioDeletePopupComponent } from './portfolio-delete-dialog.component';
import { IPortfolio } from 'app/shared/model/portfolio.model';

@Injectable({ providedIn: 'root' })
export class PortfolioResolve implements Resolve<IPortfolio> {
    constructor(private service: PortfolioService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPortfolio> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Portfolio>) => response.ok),
                map((portfolio: HttpResponse<Portfolio>) => portfolio.body)
            );
        }
        return of(new Portfolio());
    }
}

export const portfolioRoute: Routes = [
    {
        path: '',
        component: PortfolioComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PortfolioDetailComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PortfolioUpdateComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PortfolioUpdateComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const portfolioPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PortfolioDeletePopupComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
