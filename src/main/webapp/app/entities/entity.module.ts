import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'portfolio',
                loadChildren: './portfolio/portfolio.module#Csi3370PortfolioModule'
            },
            {
                path: 'share',
                loadChildren: './share/share.module#Csi3370ShareModule'
            },
            {
                path: 'credit',
                loadChildren: './credit/credit.module#Csi3370CreditModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#Csi3370TransactionModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Csi3370EntityModule {}
