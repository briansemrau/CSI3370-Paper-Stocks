import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IShare } from 'app/shared/model/share.model';

type EntityResponseType = HttpResponse<IShare>;
type EntityArrayResponseType = HttpResponse<IShare[]>;

@Injectable({ providedIn: 'root' })
export class ShareService {
    public resourceUrl = SERVER_API_URL + 'api/shares';

    constructor(protected http: HttpClient) {}

    create(share: IShare): Observable<EntityResponseType> {
        return this.http.post<IShare>(this.resourceUrl, share, { observe: 'response' });
    }

    update(share: IShare): Observable<EntityResponseType> {
        return this.http.put<IShare>(this.resourceUrl, share, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IShare>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IShare[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryByPortfolioId(portfolioId: number, req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IShare[]>(`portfolios/${portfolioId}/shares`, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    buy(share: IShare): Observable<EntityResponseType> {
        return this.http.post<IShare>(`${this.resourceUrl}/buy`, share, { observe: 'response' });
    }

    sell(share: IShare): Observable<EntityResponseType> {
        return this.http.post<IShare>(`${this.resourceUrl}/sell`, share, { observe: 'response' });
    }
}
