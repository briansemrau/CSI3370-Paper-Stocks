import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Csi3370SharedModule } from 'app/shared';
import {
    PortfolioComponent,
    PortfolioDetailComponent,
    PortfolioUpdateComponent,
    PortfolioDeletePopupComponent,
    PortfolioDeleteDialogComponent,
    portfolioRoute,
    portfolioPopupRoute
} from './';

const ENTITY_STATES = [...portfolioRoute, ...portfolioPopupRoute];

@NgModule({
    imports: [Csi3370SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PortfolioComponent,
        PortfolioDetailComponent,
        PortfolioUpdateComponent,
        PortfolioDeleteDialogComponent,
        PortfolioDeletePopupComponent
    ],
    entryComponents: [PortfolioComponent, PortfolioUpdateComponent, PortfolioDeleteDialogComponent, PortfolioDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370PortfolioModule {}
