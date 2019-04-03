import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Csi3370SharedModule } from 'app/shared';
import {
    CreditComponent,
    CreditDetailComponent,
    CreditUpdateComponent,
    CreditDeletePopupComponent,
    CreditDeleteDialogComponent,
    creditRoute,
    creditPopupRoute
} from './';

const ENTITY_STATES = [...creditRoute, ...creditPopupRoute];

@NgModule({
    imports: [Csi3370SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CreditComponent, CreditDetailComponent, CreditUpdateComponent, CreditDeleteDialogComponent, CreditDeletePopupComponent],
    entryComponents: [CreditComponent, CreditUpdateComponent, CreditDeleteDialogComponent, CreditDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370CreditModule {}
