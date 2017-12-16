/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ApadinheTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VisitaDetailComponent } from '../../../../../../main/webapp/app/entities/visita/visita-detail.component';
import { VisitaService } from '../../../../../../main/webapp/app/entities/visita/visita.service';
import { Visita } from '../../../../../../main/webapp/app/entities/visita/visita.model';

describe('Component Tests', () => {

    describe('Visita Management Detail Component', () => {
        let comp: VisitaDetailComponent;
        let fixture: ComponentFixture<VisitaDetailComponent>;
        let service: VisitaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ApadinheTestModule],
                declarations: [VisitaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VisitaService,
                    JhiEventManager
                ]
            }).overrideTemplate(VisitaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VisitaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VisitaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Visita(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.visita).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
