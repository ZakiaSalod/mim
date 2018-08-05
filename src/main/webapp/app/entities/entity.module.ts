import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MimEventModule } from './event/event.module';
import { MimCategoryModule } from './category/category.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MimEventModule,
        MimCategoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MimEntityModule {}
