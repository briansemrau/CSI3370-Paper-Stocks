import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { Csi3370SharedLibsModule, Csi3370SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { ShareBuyModalComponent } from 'app/shared/buyshare/share-buy-modal.component';
import { ShareSellModalComponent } from 'app/shared/sellshare/share-sell-modal.component';

@NgModule({
    imports: [Csi3370SharedLibsModule, Csi3370SharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, ShareBuyModalComponent, ShareSellModalComponent],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent, ShareBuyModalComponent, ShareSellModalComponent],
    exports: [Csi3370SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, ShareBuyModalComponent, ShareSellModalComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370SharedModule {
    static forRoot() {
        return {
            ngModule: Csi3370SharedModule
        };
    }
}
