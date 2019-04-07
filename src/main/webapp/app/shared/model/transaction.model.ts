import { Moment } from 'moment';
import { IPortfolio } from 'app/shared/model/portfolio.model';

export interface ITransaction {
    id?: number;
    ticker?: string;
    quantity?: number;
    pricePerShare?: number;
    date?: Moment;
    portfolio?: IPortfolio;
}

export class Transaction implements ITransaction {
    constructor(
        public id?: number,
        public ticker?: string,
        public quantity?: number,
        public pricePerShare?: number,
        public date?: Moment,
        public portfolio?: IPortfolio
    ) {}
}
