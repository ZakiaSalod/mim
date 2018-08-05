import { IEvent } from 'app/shared/model//event.model';

export interface ICategory {
    id?: number;
    name?: string;
    description?: any;
    events?: IEvent[];
}

export class Category implements ICategory {
    constructor(public id?: number, public name?: string, public description?: any, public events?: IEvent[]) {}
}
