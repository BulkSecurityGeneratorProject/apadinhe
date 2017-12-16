import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProcessoApadinhamentoComponent } from './processo-apadinhamento.component';
import { ProcessoApadinhamentoDetailComponent } from './processo-apadinhamento-detail.component';
import { ProcessoApadinhamentoPopupComponent } from './processo-apadinhamento-dialog.component';
import { ProcessoApadinhamentoDeletePopupComponent } from './processo-apadinhamento-delete-dialog.component';

export const processoApadinhamentoRoute: Routes = [
    {
        path: 'processo-apadinhamento',
        component: ProcessoApadinhamentoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.processoApadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'processo-apadinhamento/:id',
        component: ProcessoApadinhamentoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.processoApadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const processoApadinhamentoPopupRoute: Routes = [
    {
        path: 'processo-apadinhamento-new',
        component: ProcessoApadinhamentoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.processoApadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'processo-apadinhamento/:id/edit',
        component: ProcessoApadinhamentoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.processoApadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'processo-apadinhamento/:id/delete',
        component: ProcessoApadinhamentoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.processoApadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
