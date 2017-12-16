/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ApadinheTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcessoApadinhamentoDetailComponent } from '../../../../../../main/webapp/app/entities/processo-apadinhamento/processo-apadinhamento-detail.component';
import { ProcessoApadinhamentoService } from '../../../../../../main/webapp/app/entities/processo-apadinhamento/processo-apadinhamento.service';
import { ProcessoApadinhamento } from '../../../../../../main/webapp/app/entities/processo-apadinhamento/processo-apadinhamento.model';

describe('Component Tests', () => {

    describe('ProcessoApadinhamento Management Detail Component', () => {
        let comp: ProcessoApadinhamentoDetailComponent;
        let fixture: ComponentFixture<ProcessoApadinhamentoDetailComponent>;
        let service: ProcessoApadinhamentoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ApadinheTestModule],
                declarations: [ProcessoApadinhamentoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcessoApadinhamentoService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcessoApadinhamentoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcessoApadinhamentoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcessoApadinhamentoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProcessoApadinhamento(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.processoApadinhamento).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
