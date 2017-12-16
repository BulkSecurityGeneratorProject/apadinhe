import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Doacao } from './doacao.model';
import { DoacaoService } from './doacao.service';

@Component({
    selector: 'jhi-doacao-detail',
    templateUrl: './doacao-detail.component.html'
})
export class DoacaoDetailComponent implements OnInit, OnDestroy {

    doacao: Doacao;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private doacaoService: DoacaoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDoacaos();
    }

    load(id) {
        this.doacaoService.find(id).subscribe((doacao) => {
            this.doacao = doacao;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDoacaos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'doacaoListModification',
            (response) => this.load(this.doacao.id)
        );
    }
}
