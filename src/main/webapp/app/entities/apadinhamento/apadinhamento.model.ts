import { BaseEntity, User } from './../../shared';

export class Apadinhamento implements BaseEntity {
    constructor(
        public id?: number,
        public observacao?: string,
        public processo?: BaseEntity,
        public padrinhos?: User[],
        public criancas?: BaseEntity[],
    ) {
    }
}
