import { Component, Input, OnChanges, OnInit } from '@angular/core';

import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { finalize } from 'rxjs/operators';
import { ItemService } from 'src/app/services/item.service';

import { Item } from './../../shared/models/item.model';

@Component({
    selector: 'app-ficha-item',
    templateUrl: './ficha-item.component.html',
    styleUrls: ['./ficha-item.component.css']
})
export class FichaItemComponent implements OnInit, OnChanges {

    @BlockUI() blockUI: NgBlockUI;
    @Input('id') itemId;
    item: Item;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private itemService: ItemService
    ) {
    }

    iniciarItem() {
        this.blockUI.start(this._mensagemBlockUi);
        this.itemService.obterPorId(this.itemId).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (data) => {
                this.item = data;
                this.item = this.montarImagem(this.item);
            }
        );
    }

    montarImagem(item: Item) {
        let formatoImagem = 'data:image/jpg;base64,';
        let imagem = formatoImagem.concat(this.item.imagem);
        item.imagem = imagem;
        return item;
    }

    ngOnChanges(changes): void {
        if (this.itemId) {
            this.iniciarItem();
        }
    }

    ngOnInit(): void {

    }

}
