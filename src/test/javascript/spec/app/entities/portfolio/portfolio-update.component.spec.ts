/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Csi3370TestModule } from '../../../test.module';
import { PortfolioUpdateComponent } from 'app/entities/portfolio/portfolio-update.component';
import { PortfolioService } from 'app/entities/portfolio/portfolio.service';
import { Portfolio } from 'app/shared/model/portfolio.model';

describe('Component Tests', () => {
    describe('Portfolio Management Update Component', () => {
        let comp: PortfolioUpdateComponent;
        let fixture: ComponentFixture<PortfolioUpdateComponent>;
        let service: PortfolioService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [PortfolioUpdateComponent]
            })
                .overrideTemplate(PortfolioUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PortfolioUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PortfolioService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Portfolio(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.portfolio = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Portfolio();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.portfolio = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
