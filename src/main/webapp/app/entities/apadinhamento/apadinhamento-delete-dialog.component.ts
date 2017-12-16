import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Apadinhamento } from './apadinhamento.model';
import { ApadinhamentoPopupService } from './apadinhamento-popup.service';
import { ApadinhamentoService } from './apadinhamento.service';

@Component({
    selector: 'jhi-apadinhamento-delete-dialog',
    templateUrl: './apadinhamento-delete-dialog.component.html'
})
export class ApadinhamentoDeleteDialogComponent {

    apadinhamento: Apadinhamento;

    constructor(
        private apadinhamentoService: ApadinhamentoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.apadinhamentoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'apadinhamentoListModification',
                content: 'Deleted an apadinhamento'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-apadinhamento-delete-popup',
    template: ''
})
export class ApadinhamentoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private apadinhamentoPopupService: ApadinhamentoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.apadinhamentoPopupService
                .open(ApadinhamentoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
