import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ICategory } from 'app/shared/model//category.model';

export const enum Province {
    EASTERN_CAPE = 'EASTERN_CAPE',
    FREE_STATE = 'FREE_STATE',
    GAUTENG = 'GAUTENG',
    KWAZULU_NATAL = 'KWAZULU_NATAL',
    LIMPOPO = 'LIMPOPO',
    MPUMALANGA = 'MPUMALANGA',
    NORTHERN_CAPE = 'NORTHERN_CAPE',
    NORTH_WEST = 'NORTH_WEST',
    WESTERN_CAPE = 'WESTERN_CAPE'
}

export interface IEvent {
    id?: number;
    postedBy?: string;
    eventName?: string;
    briefDescription?: any;
    eventDate?: Moment;
    province?: Province;
    region?: string;
    address?: any;
    eventWebsite?: string;
    user?: IUser;
    categories?: ICategory[];
}

export class Event implements IEvent {
    constructor(
        public id?: number,
        public postedBy?: string,
        public eventName?: string,
        public briefDescription?: any,
        public eventDate?: Moment,
        public province?: Province,
        public region?: string,
        public address?: any,
        public eventWebsite?: string,
        public user?: IUser,
        public categories?: ICategory[]
    ) {}
}
