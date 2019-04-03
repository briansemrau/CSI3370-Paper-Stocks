/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Csi3370TestModule } from '../../../test.module';
import { CreditDeleteDialogComponent } from 'app/entities/credit/credit-delete-dialog.component';
import { CreditService } from 'app/entities/credit/credit.service';

describe('Component Tests', () => {
    describe('Credit Management Delete Component', () => {
        let comp: CreditDeleteDialogComponent;
        let fixture: ComponentFixture<CreditDeleteDialogComponent>;
        let service: CreditService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [CreditDeleteDialogComponent]
            })
                .overrideTemplate(CreditDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CreditDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CreditService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
