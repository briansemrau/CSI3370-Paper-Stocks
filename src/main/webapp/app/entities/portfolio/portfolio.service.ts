import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPortfolio } from 'app/shared/model/portfolio.model';

type EntityResponseType = HttpResponse<IPortfolio>;
type EntityArrayResponseType = HttpResponse<IPortfolio[]>;

@Injectable({ providedIn: 'root' })
export class PortfolioService {
    public resourceUrl = SERVER_API_URL + 'api/portfolios';

    constructor(protected http: HttpClient) {}

    create(portfolio: IPortfolio): Observable<EntityResponseType> {
        return this.http.post<IPortfolio>(this.resourceUrl, portfolio, { observe: 'response' });
    }

    update(portfolio: IPortfolio): Observable<EntityResponseType> {
        return this.http.put<IPortfolio>(this.resourceUrl, portfolio, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPortfolio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPortfolio[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
