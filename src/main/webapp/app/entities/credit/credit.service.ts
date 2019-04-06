import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICredit } from 'app/shared/model/credit.model';

type EntityResponseType = HttpResponse<ICredit>;
type EntityArrayResponseType = HttpResponse<ICredit[]>;

@Injectable({ providedIn: 'root' })
export class CreditService {
    public resourceUrl = SERVER_API_URL + 'api/credits';

    constructor(protected http: HttpClient) {}

    create(credit: ICredit): Observable<EntityResponseType> {
        return this.http.post<ICredit>(this.resourceUrl, credit, { observe: 'response' });
    }

    update(credit: ICredit): Observable<EntityResponseType> {
        return this.http.put<ICredit>(this.resourceUrl, credit, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICredit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICredit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    userCredit(): Observable<EntityResponseType> {
        return this.http.get<ICredit>(`${this.resourceUrl}/mycredit`, { observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
