import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Document } from './document.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DocumentService {

    private resourceUrl =  SERVER_API_URL + 'api/documents';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(document: Document): Observable<Document> {
        const copy = this.convert(document);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(document: Document): Observable<Document> {
        const copy = this.convert(document);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Document> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Document.
     */
    private convertItemFromServer(json: any): Document {
        const entity: Document = Object.assign(new Document(), json);
        entity.creation_date = this.dateUtils
            .convertLocalDateFromServer(json.creation_date);
        return entity;
    }

    /**
     * Convert a Document to a JSON which can be sent to the server.
     */
    private convert(document: Document): Document {
        const copy: Document = Object.assign({}, document);
        copy.creation_date = this.dateUtils
            .convertLocalDateToServer(document.creation_date);
        return copy;
    }
}
