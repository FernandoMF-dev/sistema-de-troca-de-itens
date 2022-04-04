import { Component, OnInit } from '@angular/core';
import { PageNotificationService } from '@nuvem/primeng-components';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

import { finalize } from 'rxjs/operators';
import { ItemService } from 'src/app/services/item.service';

import { OfertaService } from '../../services/oferta.service';
import { Item } from '../../shared/models/item.model';
import { Oferta } from '../../shared/models/oferta.model';
import { Usuario } from '../../shared/models/usuario.model';

@Component({
    selector: 'app-criar-oferta',
    templateUrl: './criar-oferta.component.html',
    styleUrls: ['./criar-oferta.component.css']
})
export class CriarOfertaComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    displayOferta: boolean = false;
    itemSource: Item[];
    itemTarget: Item[];
    novaOferta: Oferta = new Oferta;
    usuarioLogado: Usuario;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private itemService: ItemService,
        private ofertaService: OfertaService,
        private notification: PageNotificationService
    ) {
    }

    iniciarListasItensOfertados() {
        this.blockUI.start(this._mensagemBlockUi);
        this.itemService.listarPorDono(this.usuarioLogado.id).pipe(
            finalize(() => {
                this.displayOferta = true;
                this.blockUI.stop();
            })
        ).subscribe(
            (itens) => {
                this.itemSource = itens;
                this.itemSource = this.montarImagens(this.itemSource);
                this.itemTarget = [];
            }
        );
    }

    iniciarOferta(itemDesejadoId) {
        this.usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
        this.novaOferta.itemId = itemDesejadoId;
        this.novaOferta.usuarioOfertanteId = this.usuarioLogado.id;
    }

    montarImagens(itens: Item[]) {
        itens.forEach(element => {
            let formatoImagem = 'data:image/jpg;base64,';
            let imagem = formatoImagem.concat(element.imagem);
            element.imagem = imagem;
        });
        return itens;
    }

    ngOnInit(): void {
    }

    salvarOferta() {
        this.blockUI.start(this._mensagemBlockUi);

        let itensOfertadosId: number[] = [];

        this.itemTarget.map(element => {
            itensOfertadosId.push(element.id);
        });

        this.novaOferta.itensOfertados = itensOfertadosId;

        this.ofertaService.salvar(this.novaOferta).pipe(
            finalize(() => {
                this.displayOferta = false;
                this.blockUI.stop();
            })
        ).subscribe(
            () => {
                this.notification.addSuccessMessage('Oferta realizada com sucesso');
            },
            () => {
                this.notification.addErrorMessage('Erro ao realizar oferta');
            }
        );
    }

    showOfertaDialog(id) {
        this.iniciarOferta(id);
        this.iniciarListasItensOfertados();
    }
}
