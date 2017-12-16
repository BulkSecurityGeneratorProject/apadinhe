import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Doacao } from './doacao.model';
import { DoacaoPopupService } from './doacao-popup.service';
import { DoacaoService } from './doacao.service';

@Component({
    selector: 'jhi-doacao-delete-dialog',
    templateUrl: './doacao-delete-dialog.component.html'
})
export class DoacaoDeleteDialogComponent {

    doacao: Doacao;

    constructor(
        private doacaoService: DoacaoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.doacaoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'doacaoListModification',
                content: 'Deleted an doacao'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-doacao-delete-popup',
    template: ''
})
export class DoacaoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private doacaoPopupService: DoacaoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.doacaoPopupService
                .open(DoacaoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
