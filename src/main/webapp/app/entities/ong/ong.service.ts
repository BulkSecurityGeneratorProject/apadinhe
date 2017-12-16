import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Ong } from './ong.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OngService {

    private resourceUrl = SERVER_API_URL + 'api/ongs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ongs';

    constructor(private http: Http) { }

    create(ong: Ong): Observable<Ong> {
        const copy = this.convert(ong);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(ong: Ong): Observable<Ong> {
        const copy = this.convert(ong);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Ong> {
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

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
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
     * Convert a returned JSON object to Ong.
     */
    private convertItemFromServer(json: any): Ong {
        const entity: Ong = Object.assign(new Ong(), json);
        return entity;
    }

    /**
     * Convert a Ong to a JSON which can be sent to the server.
     */
    private convert(ong: Ong): Ong {
        const copy: Ong = Object.assign({}, ong);
        return copy;
    }
}
