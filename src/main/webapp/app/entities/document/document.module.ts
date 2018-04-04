import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { YeastSharedModule } from '../../shared';
import {
    DocumentService,
    DocumentPopupService,
    DocumentComponent,
    DocumentDetailComponent,
    DocumentDialogComponent,
    DocumentPopupComponent,
    DocumentDeletePopupComponent,
    DocumentDeleteDialogComponent,
    documentRoute,
    documentPopupRoute,
    DocumentResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...documentRoute,
    ...documentPopupRoute,
];

@NgModule({
    imports: [
        YeastSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DocumentComponent,
        DocumentDetailComponent,
        DocumentDialogComponent,
        DocumentDeleteDialogComponent,
        DocumentPopupComponent,
        DocumentDeletePopupComponent,
    ],
    entryComponents: [
        DocumentComponent,
        DocumentDialogComponent,
        DocumentPopupComponent,
        DocumentDeleteDialogComponent,
        DocumentDeletePopupComponent,
    ],
    providers: [
        DocumentService,
        DocumentPopupService,
        DocumentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class YeastDocumentModule {}
