import { BaseEntity, User } from './../../shared';

export const enum ProcessoApadinhamentoSituacao {
    'ABERTO',
    'ANALISE',
    'REJEITADO',
    'ACEITO'
}

export class ProcessoApadinhamento implements BaseEntity {
    constructor(
        public id?: number,
        public situacao?: ProcessoApadinhamentoSituacao,
        public texto?: string,
        public padrinhos?: User[],
        public criancas?: BaseEntity[],
    ) {
    }
}
