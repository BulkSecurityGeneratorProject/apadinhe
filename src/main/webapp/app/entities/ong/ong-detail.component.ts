import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Ong } from './ong.model';
import { OngService } from './ong.service';

@Component({
    selector: 'jhi-ong-detail',
    templateUrl: './ong-detail.component.html'
})
export class OngDetailComponent implements OnInit, OnDestroy {

    ong: Ong;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ongService: OngService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOngs();
    }

    load(id) {
        this.ongService.find(id).subscribe((ong) => {
            this.ong = ong;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOngs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ongListModification',
            (response) => this.load(this.ong.id)
        );
    }
}
