import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Ong } from './ong.model';
import { OngPopupService } from './ong-popup.service';
import { OngService } from './ong.service';

@Component({
    selector: 'jhi-ong-delete-dialog',
    templateUrl: './ong-delete-dialog.component.html'
})
export class OngDeleteDialogComponent {

    ong: Ong;

    constructor(
        private ongService: OngService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ongService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ongListModification',
                content: 'Deleted an ong'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ong-delete-popup',
    template: ''
})
export class OngDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ongPopupService: OngPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ongPopupService
                .open(OngDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
