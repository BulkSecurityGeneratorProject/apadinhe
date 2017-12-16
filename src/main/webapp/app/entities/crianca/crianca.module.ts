import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApadinheSharedModule } from '../../shared';
import {
    CriancaService,
    CriancaPopupService,
    CriancaComponent,
    CriancaDetailComponent,
    CriancaDialogComponent,
    CriancaPopupComponent,
    CriancaDeletePopupComponent,
    CriancaDeleteDialogComponent,
    criancaRoute,
    criancaPopupRoute,
} from './';

const ENTITY_STATES = [
    ...criancaRoute,
    ...criancaPopupRoute,
];

@NgModule({
    imports: [
        ApadinheSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CriancaComponent,
        CriancaDetailComponent,
        CriancaDialogComponent,
        CriancaDeleteDialogComponent,
        CriancaPopupComponent,
        CriancaDeletePopupComponent,
    ],
    entryComponents: [
        CriancaComponent,
        CriancaDialogComponent,
        CriancaPopupComponent,
        CriancaDeleteDialogComponent,
        CriancaDeletePopupComponent,
    ],
    providers: [
        CriancaService,
        CriancaPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheCriancaModule {}
