import { IUser } from 'app/core/user/user.model';

export interface ICredit {
    id?: number;
    credit?: number;
    user?: IUser;
}

export class Credit implements ICredit {
    constructor(public id?: number, public credit?: number, public user?: IUser) {}
}
