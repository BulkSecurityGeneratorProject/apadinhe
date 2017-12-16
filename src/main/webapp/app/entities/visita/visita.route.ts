import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VisitaComponent } from './visita.component';
import { VisitaDetailComponent } from './visita-detail.component';
import { VisitaPopupComponent } from './visita-dialog.component';
import { VisitaDeletePopupComponent } from './visita-delete-dialog.component';

export const visitaRoute: Routes = [
    {
        path: 'visita',
        component: VisitaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.visita.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'visita/:id',
        component: VisitaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.visita.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const visitaPopupRoute: Routes = [
    {
        path: 'visita-new',
        component: VisitaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.visita.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'visita/:id/edit',
        component: VisitaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.visita.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'visita/:id/delete',
        component: VisitaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.visita.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
