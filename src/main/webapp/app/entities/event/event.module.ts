import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MimSharedModule } from 'app/shared';
import { MimAdminModule } from 'app/admin/admin.module';
import {
    EventComponent,
    EventDetailComponent,
    EventUpdateComponent,
    EventDeletePopupComponent,
    EventDeleteDialogComponent,
    eventRoute,
    eventPopupRoute
} from './';

const ENTITY_STATES = [...eventRoute, ...eventPopupRoute];

@NgModule({
    imports: [MimSharedModule, MimAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [EventComponent, EventDetailComponent, EventUpdateComponent, EventDeleteDialogComponent, EventDeletePopupComponent],
    entryComponents: [EventComponent, EventUpdateComponent, EventDeleteDialogComponent, EventDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MimEventModule {}
