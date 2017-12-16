import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProcessoApadinhamento } from './processo-apadinhamento.model';
import { ProcessoApadinhamentoService } from './processo-apadinhamento.service';

@Injectable()
export class ProcessoApadinhamentoPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private processoApadinhamentoService: ProcessoApadinhamentoService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.processoApadinhamentoService.find(id).subscribe((processoApadinhamento) => {
                    this.ngbModalRef = this.processoApadinhamentoModalRef(component, processoApadinhamento);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.processoApadinhamentoModalRef(component, new ProcessoApadinhamento());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    processoApadinhamentoModalRef(component: Component, processoApadinhamento: ProcessoApadinhamento): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.processoApadinhamento = processoApadinhamento;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
