import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApadinheSharedModule } from '../../shared';
import { ApadinheAdminModule } from '../../admin/admin.module';
import {
    DoacaoService,
    DoacaoPopupService,
    DoacaoComponent,
    DoacaoDetailComponent,
    DoacaoDialogComponent,
    DoacaoPopupComponent,
    DoacaoDeletePopupComponent,
    DoacaoDeleteDialogComponent,
    doacaoRoute,
    doacaoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...doacaoRoute,
    ...doacaoPopupRoute,
];

@NgModule({
    imports: [
        ApadinheSharedModule,
        ApadinheAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DoacaoComponent,
        DoacaoDetailComponent,
        DoacaoDialogComponent,
        DoacaoDeleteDialogComponent,
        DoacaoPopupComponent,
        DoacaoDeletePopupComponent,
    ],
    entryComponents: [
        DoacaoComponent,
        DoacaoDialogComponent,
        DoacaoPopupComponent,
        DoacaoDeleteDialogComponent,
        DoacaoDeletePopupComponent,
    ],
    providers: [
        DoacaoService,
        DoacaoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheDoacaoModule {}
