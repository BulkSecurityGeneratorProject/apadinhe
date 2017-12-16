import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApadinheSharedModule } from '../../shared';
import {
    OngService,
    OngPopupService,
    OngComponent,
    OngDetailComponent,
    OngDialogComponent,
    OngPopupComponent,
    OngDeletePopupComponent,
    OngDeleteDialogComponent,
    ongRoute,
    ongPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ongRoute,
    ...ongPopupRoute,
];

@NgModule({
    imports: [
        ApadinheSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OngComponent,
        OngDetailComponent,
        OngDialogComponent,
        OngDeleteDialogComponent,
        OngPopupComponent,
        OngDeletePopupComponent,
    ],
    entryComponents: [
        OngComponent,
        OngDialogComponent,
        OngPopupComponent,
        OngDeleteDialogComponent,
        OngDeletePopupComponent,
    ],
    providers: [
        OngService,
        OngPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheOngModule {}
