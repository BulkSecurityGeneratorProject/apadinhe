import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Visita } from './visita.model';
import { VisitaService } from './visita.service';

@Component({
    selector: 'jhi-visita-detail',
    templateUrl: './visita-detail.component.html'
})
export class VisitaDetailComponent implements OnInit, OnDestroy {

    visita: Visita;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private visitaService: VisitaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVisitas();
    }

    load(id) {
        this.visitaService.find(id).subscribe((visita) => {
            this.visita = visita;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVisitas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'visitaListModification',
            (response) => this.load(this.visita.id)
        );
    }
}
