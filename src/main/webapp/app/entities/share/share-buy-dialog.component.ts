import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShare } from 'app/shared/model/share.model';
import { ShareService } from './share.service';

@Component({
    selector: 'jhi-share-buy-dialog',
    templateUrl: './share-buy-dialog.component.html'
})
export class ShareBuyDialogComponent {
    share: IShare;

    constructor(protected shareService: ShareService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmBuy(id: number) {
        this.shareService.buy(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'shareListModification',
                content: 'Shares have been bought'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-share-delete-popup',
    template: ''
})
export class ShareBuyPopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ share }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ShareBuyDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.share = share;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/share', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/share', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
