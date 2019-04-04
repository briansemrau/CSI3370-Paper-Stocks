import { IUser } from 'app/core/user/user.model';

export interface IPortfolio {
    id?: number;
    name?: string;
    user?: IUser;
}

export class Portfolio implements IPortfolio {
    constructor(public id?: number, public name?: string, public user?: IUser) {}
}
