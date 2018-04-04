import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { YeastDocumentModule } from './document/document.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        YeastDocumentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class YeastEntityModule {}
