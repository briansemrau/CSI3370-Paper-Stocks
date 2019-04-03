/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Csi3370TestModule } from '../../../test.module';
import { CreditComponent } from 'app/entities/credit/credit.component';
import { CreditService } from 'app/entities/credit/credit.service';
import { Credit } from 'app/shared/model/credit.model';

describe('Component Tests', () => {
    describe('Credit Management Component', () => {
        let comp: CreditComponent;
        let fixture: ComponentFixture<CreditComponent>;
        let service: CreditService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [CreditComponent],
                providers: []
            })
                .overrideTemplate(CreditComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CreditComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CreditService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Credit(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.credits[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
