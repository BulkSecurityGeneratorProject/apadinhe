import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Crianca } from './crianca.model';
import { CriancaPopupService } from './crianca-popup.service';
import { CriancaService } from './crianca.service';

@Component({
    selector: 'jhi-crianca-delete-dialog',
    templateUrl: './crianca-delete-dialog.component.html'
})
export class CriancaDeleteDialogComponent {

    crianca: Crianca;

    constructor(
        private criancaService: CriancaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.criancaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'criancaListModification',
                content: 'Deleted an crianca'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-crianca-delete-popup',
    template: ''
})
export class CriancaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private criancaPopupService: CriancaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.criancaPopupService
                .open(CriancaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
