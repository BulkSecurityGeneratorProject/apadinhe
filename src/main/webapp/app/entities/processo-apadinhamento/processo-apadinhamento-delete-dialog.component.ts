import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProcessoApadinhamento } from './processo-apadinhamento.model';
import { ProcessoApadinhamentoPopupService } from './processo-apadinhamento-popup.service';
import { ProcessoApadinhamentoService } from './processo-apadinhamento.service';

@Component({
    selector: 'jhi-processo-apadinhamento-delete-dialog',
    templateUrl: './processo-apadinhamento-delete-dialog.component.html'
})
export class ProcessoApadinhamentoDeleteDialogComponent {

    processoApadinhamento: ProcessoApadinhamento;

    constructor(
        private processoApadinhamentoService: ProcessoApadinhamentoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.processoApadinhamentoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'processoApadinhamentoListModification',
                content: 'Deleted an processoApadinhamento'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-processo-apadinhamento-delete-popup',
    template: ''
})
export class ProcessoApadinhamentoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private processoApadinhamentoPopupService: ProcessoApadinhamentoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.processoApadinhamentoPopupService
                .open(ProcessoApadinhamentoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
