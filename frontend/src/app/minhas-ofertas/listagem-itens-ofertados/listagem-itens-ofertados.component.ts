import { Component, OnInit } from '@angular/core';

import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { finalize } from 'rxjs/operators';
import { ItemService } from 'src/app/services/item.service';
import { Item } from 'src/app/shared/models/item.model';
import { OfertaService } from './../../services/oferta.service';

import { Oferta } from './../../shared/models/oferta.model';

@Component({
    selector: 'app-listagem-itens-ofertados',
    templateUrl: './listagem-itens-ofertados.component.html'
})
export class ListagemItensOfertadosComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    itensOfertados: Item[];
    isVisible: boolean = false;
    oferta: Oferta = new Oferta();
    contador: number = 0;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private ofertaService: OfertaService,
        private itemService: ItemService
    ) {
    }

    montarImagem(base: string) {
        let formatoImagem = 'data:image/jpg;base64,';
        return formatoImagem.concat(base);
    }

    ngOnInit(): void {
    }

    obterItensOfertados() {
        if (this.contador < this.oferta.itensOfertados.length) {
            this.itemService.obterPorId(this.oferta.itensOfertados[this.contador]).subscribe(
                (data) => {
                    data.imagem = this.montarImagem(data.imagem);
                    this.itensOfertados.push(data);
                    this.contador++;
                    this.obterItensOfertados();
                }
            );
        } else {
            this.isVisible = true;
            this.blockUI.stop();
        }
    }

    showDisplay(id) {
        this.blockUI.start(this._mensagemBlockUi);
        this.itensOfertados = [];
        this.contador = 0;
        this.ofertaService.obterPorId(id).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (data) => {
                this.oferta = data;
                this.obterItensOfertados();
            }
        );
    }

}
