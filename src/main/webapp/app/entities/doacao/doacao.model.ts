import { BaseEntity, User } from './../../shared';

export class Doacao implements BaseEntity {
    constructor(
        public id?: number,
        public comprovante?: string,
        public valor?: number,
        public ong?: BaseEntity,
        public doador?: User,
    ) {
    }
}
