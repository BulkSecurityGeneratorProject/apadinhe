import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ApadinhamentoComponent } from './apadinhamento.component';
import { ApadinhamentoDetailComponent } from './apadinhamento-detail.component';
import { ApadinhamentoPopupComponent } from './apadinhamento-dialog.component';
import { ApadinhamentoDeletePopupComponent } from './apadinhamento-delete-dialog.component';

export const apadinhamentoRoute: Routes = [
    {
        path: 'apadinhamento',
        component: ApadinhamentoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.apadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'apadinhamento/:id',
        component: ApadinhamentoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.apadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const apadinhamentoPopupRoute: Routes = [
    {
        path: 'apadinhamento-new',
        component: ApadinhamentoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.apadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'apadinhamento/:id/edit',
        component: ApadinhamentoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.apadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'apadinhamento/:id/delete',
        component: ApadinhamentoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.apadinhamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
