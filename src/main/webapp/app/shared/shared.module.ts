import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { Csi3370SharedLibsModule, Csi3370SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { ShareBuyModalComponent } from 'app/entities/share';

@NgModule({
    imports: [Csi3370SharedLibsModule, Csi3370SharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, ShareBuyModalComponent],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent, ShareBuyModalComponent],
    exports: [Csi3370SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, ShareBuyModalComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370SharedModule {
    static forRoot() {
        return {
            ngModule: Csi3370SharedModule
        };
    }
}
