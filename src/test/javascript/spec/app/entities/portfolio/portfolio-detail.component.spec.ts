/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Csi3370TestModule } from '../../../test.module';
import { PortfolioDetailComponent } from 'app/entities/portfolio/portfolio-detail.component';
import { Portfolio } from 'app/shared/model/portfolio.model';

describe('Component Tests', () => {
    describe('Portfolio Management Detail Component', () => {
        let comp: PortfolioDetailComponent;
        let fixture: ComponentFixture<PortfolioDetailComponent>;
        const route = ({ data: of({ portfolio: new Portfolio(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [PortfolioDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PortfolioDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PortfolioDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.portfolio).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
