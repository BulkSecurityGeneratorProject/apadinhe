/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ApadinheTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ApadinhamentoDetailComponent } from '../../../../../../main/webapp/app/entities/apadinhamento/apadinhamento-detail.component';
import { ApadinhamentoService } from '../../../../../../main/webapp/app/entities/apadinhamento/apadinhamento.service';
import { Apadinhamento } from '../../../../../../main/webapp/app/entities/apadinhamento/apadinhamento.model';

describe('Component Tests', () => {

    describe('Apadinhamento Management Detail Component', () => {
        let comp: ApadinhamentoDetailComponent;
        let fixture: ComponentFixture<ApadinhamentoDetailComponent>;
        let service: ApadinhamentoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ApadinheTestModule],
                declarations: [ApadinhamentoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ApadinhamentoService,
                    JhiEventManager
                ]
            }).overrideTemplate(ApadinhamentoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApadinhamentoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApadinhamentoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Apadinhamento(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.apadinhamento).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
