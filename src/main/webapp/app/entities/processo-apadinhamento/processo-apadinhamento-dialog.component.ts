import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProcessoApadinhamento } from './processo-apadinhamento.model';
import { ProcessoApadinhamentoPopupService } from './processo-apadinhamento-popup.service';
import { ProcessoApadinhamentoService } from './processo-apadinhamento.service';
import { User, UserService } from '../../shared';
import { Crianca, CriancaService } from '../crianca';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-processo-apadinhamento-dialog',
    templateUrl: './processo-apadinhamento-dialog.component.html'
})
export class ProcessoApadinhamentoDialogComponent implements OnInit {

    processoApadinhamento: ProcessoApadinhamento;
    isSaving: boolean;

    users: User[];

    criancas: Crianca[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private processoApadinhamentoService: ProcessoApadinhamentoService,
        private userService: UserService,
        private criancaService: CriancaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
        if (this.processoApadinhamento.id !== undefined) {
            this.subscribeToSaveResponse(
                this.processoApadinhamentoService.update(this.processoApadinhamento));
        } else {
            this.subscribeToSaveResponse(
                this.processoApadinhamentoService.create(this.processoApadinhamento));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProcessoApadinhamento>) {
        result.subscribe((res: ProcessoApadinhamento) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProcessoApadinhamento) {
        this.eventManager.broadcast({ name: 'processoApadinhamentoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
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
    selector: 'jhi-processo-apadinhamento-popup',
    template: ''
})
export class ProcessoApadinhamentoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private processoApadinhamentoPopupService: ProcessoApadinhamentoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.processoApadinhamentoPopupService
                    .open(ProcessoApadinhamentoDialogComponent as Component, params['id']);
            } else {
                this.processoApadinhamentoPopupService
                    .open(ProcessoApadinhamentoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
