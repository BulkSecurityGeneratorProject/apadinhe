import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ProcessoApadinhamento } from './processo-apadinhamento.model';
import { ProcessoApadinhamentoService } from './processo-apadinhamento.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-processo-apadinhamento',
    templateUrl: './processo-apadinhamento.component.html'
})
export class ProcessoApadinhamentoComponent implements OnInit, OnDestroy {
processoApadinhamentos: ProcessoApadinhamento[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private processoApadinhamentoService: ProcessoApadinhamentoService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.processoApadinhamentoService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.processoApadinhamentos = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.processoApadinhamentoService.query().subscribe(
            (res: ResponseWrapper) => {
                this.processoApadinhamentos = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProcessoApadinhamentos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProcessoApadinhamento) {
        return item.id;
    }
    registerChangeInProcessoApadinhamentos() {
        this.eventSubscriber = this.eventManager.subscribe('processoApadinhamentoListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
