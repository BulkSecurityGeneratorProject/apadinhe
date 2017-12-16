import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ProcessoApadinhamento } from './processo-apadinhamento.model';
import { ProcessoApadinhamentoService } from './processo-apadinhamento.service';

@Component({
    selector: 'jhi-processo-apadinhamento-detail',
    templateUrl: './processo-apadinhamento-detail.component.html'
})
export class ProcessoApadinhamentoDetailComponent implements OnInit, OnDestroy {

    processoApadinhamento: ProcessoApadinhamento;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private processoApadinhamentoService: ProcessoApadinhamentoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProcessoApadinhamentos();
    }

    load(id) {
        this.processoApadinhamentoService.find(id).subscribe((processoApadinhamento) => {
            this.processoApadinhamento = processoApadinhamento;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProcessoApadinhamentos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'processoApadinhamentoListModification',
            (response) => this.load(this.processoApadinhamento.id)
        );
    }
}
