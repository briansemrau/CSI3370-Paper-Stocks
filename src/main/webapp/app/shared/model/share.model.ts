import { IPortfolio } from 'app/shared/model/portfolio.model';

export interface IShare {
    id?: number;
    ticker?: string;
    quantity?: number;
    portfolio?: IPortfolio;
}

export class Share implements IShare {
    constructor(public id?: number, public ticker?: string, public quantity?: number, public portfolio?: IPortfolio) {}
}
