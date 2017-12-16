/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ApadinheTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OngDetailComponent } from '../../../../../../main/webapp/app/entities/ong/ong-detail.component';
import { OngService } from '../../../../../../main/webapp/app/entities/ong/ong.service';
import { Ong } from '../../../../../../main/webapp/app/entities/ong/ong.model';

describe('Component Tests', () => {

    describe('Ong Management Detail Component', () => {
        let comp: OngDetailComponent;
        let fixture: ComponentFixture<OngDetailComponent>;
        let service: OngService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ApadinheTestModule],
                declarations: [OngDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OngService,
                    JhiEventManager
                ]
            }).overrideTemplate(OngDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OngDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OngService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Ong(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ong).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
