import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Visita } from './visita.model';
import { VisitaPopupService } from './visita-popup.service';
import { VisitaService } from './visita.service';

@Component({
    selector: 'jhi-visita-delete-dialog',
    templateUrl: './visita-delete-dialog.component.html'
})
export class VisitaDeleteDialogComponent {

    visita: Visita;

    constructor(
        private visitaService: VisitaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.visitaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'visitaListModification',
                content: 'Deleted an visita'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-visita-delete-popup',
    template: ''
})
export class VisitaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private visitaPopupService: VisitaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.visitaPopupService
                .open(VisitaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
