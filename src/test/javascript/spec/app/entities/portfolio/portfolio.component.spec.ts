/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Csi3370TestModule } from '../../../test.module';
import { PortfolioComponent } from 'app/entities/portfolio/portfolio.component';
import { PortfolioService } from 'app/entities/portfolio/portfolio.service';
import { Portfolio } from 'app/shared/model/portfolio.model';

describe('Component Tests', () => {
    describe('Portfolio Management Component', () => {
        let comp: PortfolioComponent;
        let fixture: ComponentFixture<PortfolioComponent>;
        let service: PortfolioService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [PortfolioComponent],
                providers: []
            })
                .overrideTemplate(PortfolioComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PortfolioComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PortfolioService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Portfolio(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.portfolios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
