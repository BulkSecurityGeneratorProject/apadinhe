import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ApadinheOngModule } from './ong/ong.module';
import { ApadinheApadinhamentoModule } from './apadinhamento/apadinhamento.module';
import { ApadinheProcessoApadinhamentoModule } from './processo-apadinhamento/processo-apadinhamento.module';
import { ApadinheVisitaModule } from './visita/visita.module';
import { ApadinheDoacaoModule } from './doacao/doacao.module';
import { ApadinheCriancaModule } from './crianca/crianca.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ApadinheOngModule,
        ApadinheApadinhamentoModule,
        ApadinheProcessoApadinhamentoModule,
        ApadinheVisitaModule,
        ApadinheDoacaoModule,
        ApadinheCriancaModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApadinheEntityModule {}
