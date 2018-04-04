/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { YeastTestModule } from '../../../test.module';
import { DocumentComponent } from '../../../../../../main/webapp/app/entities/document/document.component';
import { DocumentService } from '../../../../../../main/webapp/app/entities/document/document.service';
import { Document } from '../../../../../../main/webapp/app/entities/document/document.model';

describe('Component Tests', () => {

    describe('Document Management Component', () => {
        let comp: DocumentComponent;
        let fixture: ComponentFixture<DocumentComponent>;
        let service: DocumentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [YeastTestModule],
                declarations: [DocumentComponent],
                providers: [
                    DocumentService
                ]
            })
            .overrideTemplate(DocumentComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DocumentComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DocumentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Document(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.documents[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
