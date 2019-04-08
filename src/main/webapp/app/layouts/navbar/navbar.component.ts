import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { formatCurrency } from '@angular/common';

import { VERSION } from 'app/app.constants';
import { Account, AccountService, DeleteAccountModalService, LoginModalService, LoginService } from 'app/core';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { CreditService } from 'app/entities/credit';
import { ICredit } from 'app/shared/model/credit.model';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    credit: ICredit;
    account: Account;

    constructor(
        private loginService: LoginService,
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private deleteAccountModalService: DeleteAccountModalService,
        private profileService: ProfileService,
        private creditService: CreditService,
        private router: Router
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    loadCredit() {
        this.creditService
            .userCredit()
            .pipe(
                filter((res: HttpResponse<ICredit>) => res.ok),
                map((res: HttpResponse<ICredit>) => res.body)
            )
            .subscribe(
                (res: ICredit) => {
                    this.credit = res;
                },
                (res: HttpErrorResponse) => {
                    this.credit = null;
                    //this.onError(res.message)
                }
            );
    }

    ngOnInit() {
        this.loadCredit();
        this.profileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
        this.accountService.identity().then((account: Account) => {
            this.account = account;
        });
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    deleteAccount() {
        this.modalRef = this.deleteAccountModalService.open();
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.accountService.getImageUrl() : null;
    }

    formatCurrency(value: number, locale: string, currency: string, currencyCode?: string, digitsInfo?: string) {
        return formatCurrency(value, locale, currency, currencyCode, digitsInfo);
    }
}
