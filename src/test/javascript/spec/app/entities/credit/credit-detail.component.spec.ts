/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Csi3370TestModule } from '../../../test.module';
import { CreditDetailComponent } from 'app/entities/credit/credit-detail.component';
import { Credit } from 'app/shared/model/credit.model';

describe('Component Tests', () => {
    describe('Credit Management Detail Component', () => {
        let comp: CreditDetailComponent;
        let fixture: ComponentFixture<CreditDetailComponent>;
        const route = ({ data: of({ credit: new Credit(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [CreditDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CreditDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CreditDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.credit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
