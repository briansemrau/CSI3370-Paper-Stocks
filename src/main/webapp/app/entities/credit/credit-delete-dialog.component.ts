import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICredit } from 'app/shared/model/credit.model';
import { CreditService } from './credit.service';

@Component({
    selector: 'jhi-credit-delete-dialog',
    templateUrl: './credit-delete-dialog.component.html'
})
export class CreditDeleteDialogComponent {
    credit: ICredit;

    constructor(protected creditService: CreditService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.creditService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'creditListModification',
                content: 'Deleted an credit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-credit-delete-popup',
    template: ''
})
export class CreditDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ credit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CreditDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.credit = credit;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/credit', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/credit', { outlets: { popup: null } }]);
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
