import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Apadinhamento } from './apadinhamento.model';
import { ApadinhamentoPopupService } from './apadinhamento-popup.service';
import { ApadinhamentoService } from './apadinhamento.service';
import { ProcessoApadinhamento, ProcessoApadinhamentoService } from '../processo-apadinhamento';
import { User, UserService } from '../../shared';
import { Crianca, CriancaService } from '../crianca';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-apadinhamento-dialog',
    templateUrl: './apadinhamento-dialog.component.html'
})
export class ApadinhamentoDialogComponent implements OnInit {

    apadinhamento: Apadinhamento;
    isSaving: boolean;

    processos: ProcessoApadinhamento[];

    users: User[];

    criancas: Crianca[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private apadinhamentoService: ApadinhamentoService,
        private processoApadinhamentoService: ProcessoApadinhamentoService,
        private userService: UserService,
        private criancaService: CriancaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.processoApadinhamentoService
            .query({filter: 'apadinhamento-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.apadinhamento.processo || !this.apadinhamento.processo.id) {
                    this.processos = res.json;
                } else {
                    this.processoApadinhamentoService
                        .find(this.apadinhamento.processo.id)
                        .subscribe((subRes: ProcessoApadinhamento) => {
                            this.processos = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.criancaService.query()
            .subscribe((res: ResponseWrapper) => { this.criancas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.apadinhamento.id !== undefined) {
            this.subscribeToSaveResponse(
                this.apadinhamentoService.update(this.apadinhamento));
        } else {
            this.subscribeToSaveResponse(
                this.apadinhamentoService.create(this.apadinhamento));
        }
    }

    private subscribeToSaveResponse(result: Observable<Apadinhamento>) {
        result.subscribe((res: Apadinhamento) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Apadinhamento) {
        this.eventManager.broadcast({ name: 'apadinhamentoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProcessoApadinhamentoById(index: number, item: ProcessoApadinhamento) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackCriancaById(index: number, item: Crianca) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-apadinhamento-popup',
    template: ''
})
export class ApadinhamentoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private apadinhamentoPopupService: ApadinhamentoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.apadinhamentoPopupService
                    .open(ApadinhamentoDialogComponent as Component, params['id']);
            } else {
                this.apadinhamentoPopupService
                    .open(ApadinhamentoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
