import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DoacaoComponent } from './doacao.component';
import { DoacaoDetailComponent } from './doacao-detail.component';
import { DoacaoPopupComponent } from './doacao-dialog.component';
import { DoacaoDeletePopupComponent } from './doacao-delete-dialog.component';

export const doacaoRoute: Routes = [
    {
        path: 'doacao',
        component: DoacaoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.doacao.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'doacao/:id',
        component: DoacaoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.doacao.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const doacaoPopupRoute: Routes = [
    {
        path: 'doacao-new',
        component: DoacaoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.doacao.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'doacao/:id/edit',
        component: DoacaoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.doacao.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'doacao/:id/delete',
        component: DoacaoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.doacao.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
