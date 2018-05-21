import {BaseEntity} from './../../shared';

export class DocumentSearchResult implements BaseEntity {
    constructor(
        public id?: number,
        public creation_date?: any,
        public content?: string,
        public evaluationId?: number,
        public similarityMeasure?: any
    ) {
    }
}
