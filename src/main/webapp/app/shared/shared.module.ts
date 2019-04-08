import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { Csi3370SharedLibsModule, Csi3370SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { ShareBuyModalComponent } from 'app/shared/buyshare/share-buy-modal.component';
import { ShareSellModalComponent } from 'app/shared/sellshare/share-sell-modal.component';
import { DeleteAccountModalComponent } from 'app/shared/deleteaccount/deleteaccount-modal.component';

@NgModule({
    imports: [Csi3370SharedLibsModule, Csi3370SharedCommonModule],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        ShareBuyModalComponent,
        ShareSellModalComponent,
        DeleteAccountModalComponent
    ],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent, ShareBuyModalComponent, ShareSellModalComponent, DeleteAccountModalComponent],
    exports: [
        Csi3370SharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        ShareBuyModalComponent,
        ShareSellModalComponent,
        DeleteAccountModalComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370SharedModule {
    static forRoot() {
        return {
            ngModule: Csi3370SharedModule
        };
    }
}
