import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OngComponent } from './ong.component';
import { OngDetailComponent } from './ong-detail.component';
import { OngPopupComponent } from './ong-dialog.component';
import { OngDeletePopupComponent } from './ong-delete-dialog.component';

export const ongRoute: Routes = [
    {
        path: 'ong',
        component: OngComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.ong.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ong/:id',
        component: OngDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.ong.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ongPopupRoute: Routes = [
    {
        path: 'ong-new',
        component: OngPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.ong.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ong/:id/edit',
        component: OngPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.ong.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ong/:id/delete',
        component: OngDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apadinheApp.ong.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
