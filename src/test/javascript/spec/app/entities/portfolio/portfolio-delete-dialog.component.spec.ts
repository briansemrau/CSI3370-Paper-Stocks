/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Csi3370TestModule } from '../../../test.module';
import { PortfolioDeleteDialogComponent } from 'app/entities/portfolio/portfolio-delete-dialog.component';
import { PortfolioService } from 'app/entities/portfolio/portfolio.service';

describe('Component Tests', () => {
    describe('Portfolio Management Delete Component', () => {
        let comp: PortfolioDeleteDialogComponent;
        let fixture: ComponentFixture<PortfolioDeleteDialogComponent>;
        let service: PortfolioService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Csi3370TestModule],
                declarations: [PortfolioDeleteDialogComponent]
            })
                .overrideTemplate(PortfolioDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PortfolioDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PortfolioService);
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
