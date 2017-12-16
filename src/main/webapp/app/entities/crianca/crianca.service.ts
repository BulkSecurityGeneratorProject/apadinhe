import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Crianca } from './crianca.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CriancaService {

    private resourceUrl = SERVER_API_URL + 'api/criancas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/criancas';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(crianca: Crianca): Observable<Crianca> {
        const copy = this.convert(crianca);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(crianca: Crianca): Observable<Crianca> {
        const copy = this.convert(crianca);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Crianca> {
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
     * Convert a returned JSON object to Crianca.
     */
    private convertItemFromServer(json: any): Crianca {
        const entity: Crianca = Object.assign(new Crianca(), json);
        entity.dataNascimento = this.dateUtils
            .convertLocalDateFromServer(json.dataNascimento);
        return entity;
    }

    /**
     * Convert a Crianca to a JSON which can be sent to the server.
     */
    private convert(crianca: Crianca): Crianca {
        const copy: Crianca = Object.assign({}, crianca);
        copy.dataNascimento = this.dateUtils
            .convertLocalDateToServer(crianca.dataNascimento);
        return copy;
    }
}
