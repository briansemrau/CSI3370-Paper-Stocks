import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Csi3370SharedModule } from 'app/shared';
import {
    ShareComponent,
    ShareDetailComponent,
    ShareUpdateComponent,
    ShareDeletePopupComponent,
    ShareDeleteDialogComponent,
    shareRoute,
    sharePopupRoute
} from './';

const ENTITY_STATES = [...shareRoute, ...sharePopupRoute];

@NgModule({
    imports: [Csi3370SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ShareComponent, ShareDetailComponent, ShareUpdateComponent, ShareDeleteDialogComponent, ShareDeletePopupComponent],
    entryComponents: [ShareComponent, ShareUpdateComponent, ShareDeleteDialogComponent, ShareDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370ShareModule {}
