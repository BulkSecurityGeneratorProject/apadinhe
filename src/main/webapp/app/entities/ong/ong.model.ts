import { BaseEntity } from './../../shared';

export const enum Banco {
    'BB',
    'CAIXA',
    'BRADESCO',
    'SANTANDER'
}

export class Ong implements BaseEntity {
    constructor(
        public id?: number,
        public nome?: string,
        public requisitosApadrinhamento?: string,
        public conta?: string,
        public agencia?: string,
        public banco?: Banco,
        public email?: string,
        public telefone?: string,
    ) {
    }
}
