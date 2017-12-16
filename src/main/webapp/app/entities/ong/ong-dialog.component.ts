import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Ong } from './ong.model';
import { OngPopupService } from './ong-popup.service';
import { OngService } from './ong.service';

@Component({
    selector: 'jhi-ong-dialog',
    templateUrl: './ong-dialog.component.html'
})
export class OngDialogComponent implements OnInit {

    ong: Ong;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ongService: OngService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ong.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ongService.update(this.ong));
        } else {
            this.subscribeToSaveResponse(
                this.ongService.create(this.ong));
        }
    }

    private subscribeToSaveResponse(result: Observable<Ong>) {
        result.subscribe((res: Ong) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Ong) {
        this.eventManager.broadcast({ name: 'ongListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-ong-popup',
    template: ''
})
export class OngPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ongPopupService: OngPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ongPopupService
                    .open(OngDialogComponent as Component, params['id']);
            } else {
                this.ongPopupService
                    .open(OngDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
