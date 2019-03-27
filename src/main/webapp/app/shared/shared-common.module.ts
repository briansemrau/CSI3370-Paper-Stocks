import { NgModule } from '@angular/core';

import { Csi3370SharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [Csi3370SharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [Csi3370SharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class Csi3370SharedCommonModule {}
