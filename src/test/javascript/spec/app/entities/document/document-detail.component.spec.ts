/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { YeastTestModule } from '../../../test.module';
import { DocumentDetailComponent } from '../../../../../../main/webapp/app/entities/document/document-detail.component';
import { DocumentService } from '../../../../../../main/webapp/app/entities/document/document.service';
import { Document } from '../../../../../../main/webapp/app/entities/document/document.model';

describe('Component Tests', () => {

    describe('Document Management Detail Component', () => {
        let comp: DocumentDetailComponent;
        let fixture: ComponentFixture<DocumentDetailComponent>;
        let service: DocumentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [YeastTestModule],
                declarations: [DocumentDetailComponent],
                providers: [
                    DocumentService
                ]
            })
            .overrideTemplate(DocumentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DocumentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DocumentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Document(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.document).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
