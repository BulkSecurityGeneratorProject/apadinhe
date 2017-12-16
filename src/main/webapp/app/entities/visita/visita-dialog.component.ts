import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Visita } from './visita.model';
import { VisitaPopupService } from './visita-popup.service';
import { VisitaService } from './visita.service';
import { Crianca, CriancaService } from '../crianca';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-visita-dialog',
    templateUrl: './visita-dialog.component.html'
})
export class VisitaDialogComponent implements OnInit {

    visita: Visita;
    isSaving: boolean;

    criancas: Crianca[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private visitaService: VisitaService,
        private criancaService: CriancaService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.criancaService.query()
            .subscribe((res: ResponseWrapper) => { this.criancas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.visita.id !== undefined) {
            this.subscribeToSaveResponse(
                this.visitaService.update(this.visita));
        } else {
            this.subscribeToSaveResponse(
                this.visitaService.create(this.visita));
        }
    }

    private subscribeToSaveResponse(result: Observable<Visita>) {
        result.subscribe((res: Visita) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Visita) {
        this.eventManager.broadcast({ name: 'visitaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCriancaById(index: number, item: Crianca) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-visita-popup',
    template: ''
})
export class VisitaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private visitaPopupService: VisitaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.visitaPopupService
                    .open(VisitaDialogComponent as Component, params['id']);
            } else {
                this.visitaPopupService
                    .open(VisitaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
