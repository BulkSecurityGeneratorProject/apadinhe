import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApadinheSharedModule } from '../../shared';
import { ApadinheAdminModule } from '../../admin/admin.module';
import {
    VisitaService,
    VisitaPopupService,
    VisitaComponent,
    VisitaDetailComponent,
    VisitaDialogComponent,
    VisitaPopupComponent,
    VisitaDeletePopupComponent,
    VisitaDeleteDialogComponent,
    visitaRoute,
    visitaPopupRoute,
} from './';

const ENTITY_STATES = [
    ...visitaRoute,
    ...visitaPopupRoute,
];

@NgModule({
    imports: [
        ApadinheSharedModule,
        ApadinheAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VisitaComponent,
        VisitaDetailComponent,
        VisitaDialogComponent,
        VisitaDeleteDialogComponent,
        VisitaPopupComponent,
        VisitaDeletePopupComponent,
    ],
    entryComponents: [
        VisitaComponent,
        VisitaDialogComponent,
        VisitaPopupComponent,
        VisitaDeleteDialogComponent,
        VisitaDeletePopupComponent,
    ],
    providers: [
        VisitaService,
        VisitaPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheVisitaModule {}
