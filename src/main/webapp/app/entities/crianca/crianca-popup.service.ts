import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Crianca } from './crianca.model';
import { CriancaService } from './crianca.service';

@Injectable()
export class CriancaPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private criancaService: CriancaService

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
                this.criancaService.find(id).subscribe((crianca) => {
                    if (crianca.dataNascimento) {
                        crianca.dataNascimento = {
                            year: crianca.dataNascimento.getFullYear(),
                            month: crianca.dataNascimento.getMonth() + 1,
                            day: crianca.dataNascimento.getDate()
                        };
                    }
                    this.ngbModalRef = this.criancaModalRef(component, crianca);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.criancaModalRef(component, new Crianca());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    criancaModalRef(component: Component, crianca: Crianca): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.crianca = crianca;
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
