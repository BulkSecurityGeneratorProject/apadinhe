import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Apadinhamento } from './apadinhamento.model';
import { ApadinhamentoService } from './apadinhamento.service';

@Component({
    selector: 'jhi-apadinhamento-detail',
    templateUrl: './apadinhamento-detail.component.html'
})
export class ApadinhamentoDetailComponent implements OnInit, OnDestroy {

    apadinhamento: Apadinhamento;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private apadinhamentoService: ApadinhamentoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApadinhamentos();
    }

    load(id) {
        this.apadinhamentoService.find(id).subscribe((apadinhamento) => {
            this.apadinhamento = apadinhamento;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApadinhamentos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'apadinhamentoListModification',
            (response) => this.load(this.apadinhamento.id)
        );
    }
}
