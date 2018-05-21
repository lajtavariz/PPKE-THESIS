import {Component, OnDestroy, OnInit} from '@angular/core';
import {ResponseWrapper} from '../../shared';
import {DocumentService} from './document.service';
import {DocumentSearchResult} from './document-search.model';
import {JhiAlertService} from 'ng-jhipster';

@Component({
    selector: 'jhi-document-search',
    templateUrl: './document-search.component.html'
})
export class DocumentSearchComponent implements OnInit, OnDestroy {

    documentSearchResults: DocumentSearchResult[];

    constructor(
        private documentService: DocumentService,
        private jhiAlertService: JhiAlertService
    ) {
    }

    ngOnDestroy(): void {
    }

    ngOnInit(): void {
    }

    onSearch(query: string, measure: string): void {
        this.documentService.search(query, measure).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json),
            (res: ResponseWrapper) => this.onError(res.json)
        );

        console.log('Search button has been clicked');
        console.log(this.documentSearchResults);
    }

    private onSuccess(data) {
        this.documentSearchResults = data;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
