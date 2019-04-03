/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Csi3370TestModule } from '../../../test.module';
import { CreditUpdateComponent } from 'app/entities/credit/credit-update.component';
import { CreditService } from 'app/entities/credit/credit.service';
import { Credit } from 'app/shared/model/credit.model';

describe('Component Tests', () => {
    describe('Credit Management Update Component', () => {
        let comp: CreditUpdateComponent;
        let fixture: ComponentFixture<CreditUpdateComponent>;
        let service: CreditService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [CreditUpdateComponent]
            })
                .overrideTemplate(CreditUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CreditUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CreditService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Credit(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.credit = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Credit();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.credit = entity;
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
