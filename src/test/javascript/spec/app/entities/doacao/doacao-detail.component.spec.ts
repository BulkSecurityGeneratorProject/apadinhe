/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ApadinheTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DoacaoDetailComponent } from '../../../../../../main/webapp/app/entities/doacao/doacao-detail.component';
import { DoacaoService } from '../../../../../../main/webapp/app/entities/doacao/doacao.service';
import { Doacao } from '../../../../../../main/webapp/app/entities/doacao/doacao.model';

describe('Component Tests', () => {

    describe('Doacao Management Detail Component', () => {
        let comp: DoacaoDetailComponent;
        let fixture: ComponentFixture<DoacaoDetailComponent>;
        let service: DoacaoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ApadinheTestModule],
                declarations: [DoacaoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DoacaoService,
                    JhiEventManager
                ]
            }).overrideTemplate(DoacaoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DoacaoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DoacaoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Doacao(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.doacao).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
