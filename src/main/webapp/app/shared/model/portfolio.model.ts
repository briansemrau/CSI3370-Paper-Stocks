import { IShare } from 'app/shared/model/share.model';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IUser } from 'app/core/user/user.model';

export interface IPortfolio {
    id?: number;
    name?: string;
    shares?: IShare[];
    transactions?: ITransaction[];
    user?: IUser;
}

export class Portfolio implements IPortfolio {
    constructor(
        public id?: number,
        public name?: string,
        public shares?: IShare[],
        public transactions?: ITransaction[],
        public user?: IUser
    ) {}
}
