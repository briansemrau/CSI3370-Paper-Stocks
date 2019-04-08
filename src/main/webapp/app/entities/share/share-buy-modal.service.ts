import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ShareBuyModalComponent } from 'app/shared';

@Injectable({ providedIn: 'root' })
export class ShareBuyModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(symbol?: string): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(ShareBuyModalComponent);
        modalRef.componentInstance.share.ticker = symbol;
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
