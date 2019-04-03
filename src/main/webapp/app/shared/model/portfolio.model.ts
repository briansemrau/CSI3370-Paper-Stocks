import { IShare } from 'app/shared/model/share.model';
import { IUser } from 'app/core/user/user.model';

export interface IPortfolio {
    id?: number;
    name?: string;
    shares?: IShare[];
    user?: IUser;
}

export class Portfolio implements IPortfolio {
    constructor(public id?: number, public name?: string, public shares?: IShare[], public user?: IUser) {}
}
