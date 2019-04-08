import { Component, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IShare, Share } from 'app/shared/model/share.model';
import { StateStorageService } from 'app/core';

import { ShareService } from './share.service';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from 'app/entities/portfolio';

@Component({
    selector: 'jhi-share-buy-modal',
    templateUrl: './share-buy-modal.component.html'
})
export class ShareBuyModalComponent implements AfterViewInit {
    buyError: boolean;
    share: IShare = new Share(null, 'AAAA', 1, null);
    portfolio: IPortfolio;

    portfolios: IPortfolio[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private shareService: ShareService,
        private portfolioService: PortfolioService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        public activeModal: NgbActiveModal
    ) {}

    ngAfterViewInit() {
        setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#quantity'), 'focus', []), 0);
        this.portfolioService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPortfolio[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPortfolio[]>) => response.body)
            )
            .subscribe((res: IPortfolio[]) => (this.portfolios = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    cancel() {
        this.buyError = false;
        this.activeModal.dismiss('cancel');
    }

    buy() {
        this.shareService.buy(this.share).subscribe(
            data => {
                this.buyError = false;
                this.activeModal.dismiss('buy success');
            },
            error => {
                this.buyError = true;
            }
        );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPortfolioById(index: number, item: IPortfolio) {
        return item.id;
    }
}
