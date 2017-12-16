import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Crianca } from './crianca.model';
import { CriancaPopupService } from './crianca-popup.service';
import { CriancaService } from './crianca.service';
import { Ong, OngService } from '../ong';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-crianca-dialog',
    templateUrl: './crianca-dialog.component.html'
})
export class CriancaDialogComponent implements OnInit {

    crianca: Crianca;
    isSaving: boolean;

    ongs: Ong[];
    dataNascimentoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private criancaService: CriancaService,
        private ongService: OngService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.ongService.query()
            .subscribe((res: ResponseWrapper) => { this.ongs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.crianca.id !== undefined) {
            this.subscribeToSaveResponse(
                this.criancaService.update(this.crianca));
        } else {
            this.subscribeToSaveResponse(
                this.criancaService.create(this.crianca));
        }
    }

    private subscribeToSaveResponse(result: Observable<Crianca>) {
        result.subscribe((res: Crianca) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Crianca) {
        this.eventManager.broadcast({ name: 'criancaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackOngById(index: number, item: Ong) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-crianca-popup',
    template: ''
})
export class CriancaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private criancaPopupService: CriancaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.criancaPopupService
                    .open(CriancaDialogComponent as Component, params['id']);
            } else {
                this.criancaPopupService
                    .open(CriancaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
