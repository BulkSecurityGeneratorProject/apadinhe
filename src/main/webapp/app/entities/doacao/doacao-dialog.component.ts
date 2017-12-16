import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Doacao } from './doacao.model';
import { DoacaoPopupService } from './doacao-popup.service';
import { DoacaoService } from './doacao.service';
import { Ong, OngService } from '../ong';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-doacao-dialog',
    templateUrl: './doacao-dialog.component.html'
})
export class DoacaoDialogComponent implements OnInit {

    doacao: Doacao;
    isSaving: boolean;

    ongs: Ong[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private doacaoService: DoacaoService,
        private ongService: OngService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.ongService.query()
            .subscribe((res: ResponseWrapper) => { this.ongs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.doacao.id !== undefined) {
            this.subscribeToSaveResponse(
                this.doacaoService.update(this.doacao));
        } else {
            this.subscribeToSaveResponse(
                this.doacaoService.create(this.doacao));
        }
    }

    private subscribeToSaveResponse(result: Observable<Doacao>) {
        result.subscribe((res: Doacao) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Doacao) {
        this.eventManager.broadcast({ name: 'doacaoListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-doacao-popup',
    template: ''
})
export class DoacaoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private doacaoPopupService: DoacaoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.doacaoPopupService
                    .open(DoacaoDialogComponent as Component, params['id']);
            } else {
                this.doacaoPopupService
                    .open(DoacaoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
