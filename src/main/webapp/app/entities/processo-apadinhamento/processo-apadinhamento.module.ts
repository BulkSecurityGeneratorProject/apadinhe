import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApadinheSharedModule } from '../../shared';
import { ApadinheAdminModule } from '../../admin/admin.module';
import {
    ProcessoApadinhamentoService,
    ProcessoApadinhamentoPopupService,
    ProcessoApadinhamentoComponent,
    ProcessoApadinhamentoDetailComponent,
    ProcessoApadinhamentoDialogComponent,
    ProcessoApadinhamentoPopupComponent,
    ProcessoApadinhamentoDeletePopupComponent,
    ProcessoApadinhamentoDeleteDialogComponent,
    processoApadinhamentoRoute,
    processoApadinhamentoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...processoApadinhamentoRoute,
    ...processoApadinhamentoPopupRoute,
];

@NgModule({
    imports: [
        ApadinheSharedModule,
        ApadinheAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProcessoApadinhamentoComponent,
        ProcessoApadinhamentoDetailComponent,
        ProcessoApadinhamentoDialogComponent,
        ProcessoApadinhamentoDeleteDialogComponent,
        ProcessoApadinhamentoPopupComponent,
        ProcessoApadinhamentoDeletePopupComponent,
    ],
    entryComponents: [
        ProcessoApadinhamentoComponent,
        ProcessoApadinhamentoDialogComponent,
        ProcessoApadinhamentoPopupComponent,
        ProcessoApadinhamentoDeleteDialogComponent,
        ProcessoApadinhamentoDeletePopupComponent,
    ],
    providers: [
        ProcessoApadinhamentoService,
        ProcessoApadinhamentoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheProcessoApadinhamentoModule {}
