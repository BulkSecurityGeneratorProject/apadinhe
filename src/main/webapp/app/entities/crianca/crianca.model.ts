import { BaseEntity } from './../../shared';

export class Crianca implements BaseEntity {
    constructor(
        public id?: number,
        public nome?: string,
        public dataNascimento?: any,
        public preferencia?: string,
        public ong?: BaseEntity,
    ) {
    }
}
