import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Crianca } from './crianca.model';
import { CriancaService } from './crianca.service';

@Component({
    selector: 'jhi-crianca-detail',
    templateUrl: './crianca-detail.component.html'
})
export class CriancaDetailComponent implements OnInit, OnDestroy {

    crianca: Crianca;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private criancaService: CriancaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCriancas();
    }

    load(id) {
        this.criancaService.find(id).subscribe((crianca) => {
            this.crianca = crianca;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCriancas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'criancaListModification',
            (response) => this.load(this.crianca.id)
        );
    }
}
