import { Component, AfterViewInit, Renderer, ElementRef, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';

import { IShare, Share } from 'app/shared/model/share.model';

import { ShareService } from 'app/entities/share/share.service';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from 'app/entities/portfolio';

@Component({
    selector: 'jhi-deleteaccount-modal',
    templateUrl: './deleteaccount-modal.component.html'
})
export class DeleteAccountModalComponent implements AfterViewInit {
    sellError: boolean;
    @Input() public share: IShare = new Share(null, 'AAAA', 1, null);
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
        this.sellError = false;
        this.activeModal.dismiss('cancel');
    }

    buy() {
        this.shareService.sell(this.share).subscribe(
            data => {
                this.sellError = false;
                this.activeModal.dismiss('sell success');
            },
            error => {
                this.sellError = true;
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
