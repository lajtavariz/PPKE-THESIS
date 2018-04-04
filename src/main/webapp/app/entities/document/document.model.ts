import { BaseEntity } from './../../shared';

export class Document implements BaseEntity {
    constructor(
        public id?: number,
        public creation_date?: any,
        public content?: string,
    ) {
    }
}
