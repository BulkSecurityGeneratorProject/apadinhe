/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ApadinheTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CriancaDetailComponent } from '../../../../../../main/webapp/app/entities/crianca/crianca-detail.component';
import { CriancaService } from '../../../../../../main/webapp/app/entities/crianca/crianca.service';
import { Crianca } from '../../../../../../main/webapp/app/entities/crianca/crianca.model';

describe('Component Tests', () => {

    describe('Crianca Management Detail Component', () => {
        let comp: CriancaDetailComponent;
        let fixture: ComponentFixture<CriancaDetailComponent>;
        let service: CriancaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ApadinheTestModule],
                declarations: [CriancaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CriancaService,
                    JhiEventManager
                ]
            }).overrideTemplate(CriancaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CriancaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CriancaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Crianca(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.crianca).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
