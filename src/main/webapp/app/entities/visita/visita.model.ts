import { BaseEntity, User } from './../../shared';

export class Visita implements BaseEntity {
    constructor(
        public id?: number,
        public data?: any,
        public crianca?: BaseEntity,
        public padrinho?: User,
    ) {
    }
}
