import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApadinheSharedModule } from '../../shared';
import { ApadinheAdminModule } from '../../admin/admin.module';
import {
    ApadinhamentoService,
    ApadinhamentoPopupService,
    ApadinhamentoComponent,
    ApadinhamentoDetailComponent,
    ApadinhamentoDialogComponent,
    ApadinhamentoPopupComponent,
    ApadinhamentoDeletePopupComponent,
    ApadinhamentoDeleteDialogComponent,
    apadinhamentoRoute,
    apadinhamentoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...apadinhamentoRoute,
    ...apadinhamentoPopupRoute,
];

@NgModule({
    imports: [
        ApadinheSharedModule,
        ApadinheAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ApadinhamentoComponent,
        ApadinhamentoDetailComponent,
        ApadinhamentoDialogComponent,
        ApadinhamentoDeleteDialogComponent,
        ApadinhamentoPopupComponent,
        ApadinhamentoDeletePopupComponent,
    ],
    entryComponents: [
        ApadinhamentoComponent,
        ApadinhamentoDialogComponent,
        ApadinhamentoPopupComponent,
        ApadinhamentoDeleteDialogComponent,
        ApadinhamentoDeletePopupComponent,
    ],
    providers: [
        ApadinhamentoService,
        ApadinhamentoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheApadinhamentoModule {}
