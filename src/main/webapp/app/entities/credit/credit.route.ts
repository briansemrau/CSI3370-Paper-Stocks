import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Credit } from 'app/shared/model/credit.model';
import { CreditService } from './credit.service';
import { CreditComponent } from './credit.component';
import { CreditDetailComponent } from './credit-detail.component';
import { CreditUpdateComponent } from './credit-update.component';
import { CreditDeletePopupComponent } from './credit-delete-dialog.component';
import { ICredit } from 'app/shared/model/credit.model';

@Injectable({ providedIn: 'root' })
export class CreditResolve implements Resolve<ICredit> {
    constructor(private service: CreditService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICredit> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Credit>) => response.ok),
                map((credit: HttpResponse<Credit>) => credit.body)
            );
        }
        return of(new Credit());
    }
}

export const creditRoute: Routes = [
    {
        path: '',
        component: CreditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Credits'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CreditDetailComponent,
        resolve: {
            credit: CreditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Credits'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CreditUpdateComponent,
        resolve: {
            credit: CreditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Credits'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CreditUpdateComponent,
        resolve: {
            credit: CreditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Credits'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const creditPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CreditDeletePopupComponent,
        resolve: {
            credit: CreditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Credits'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
