import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CriancaComponent } from './crianca.component';
import { CriancaDetailComponent } from './crianca-detail.component';
import { CriancaPopupComponent } from './crianca-dialog.component';
import { CriancaDeletePopupComponent } from './crianca-delete-dialog.component';

export const criancaRoute: Routes = [
    {
        path: 'crianca',
        component: CriancaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.crianca.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'crianca/:id',
        component: CriancaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.crianca.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const criancaPopupRoute: Routes = [
    {
        path: 'crianca-new',
        component: CriancaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.crianca.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'crianca/:id/edit',
        component: CriancaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.crianca.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'crianca/:id/delete',
        component: CriancaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.crianca.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
