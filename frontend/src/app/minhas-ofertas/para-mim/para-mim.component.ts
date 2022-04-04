import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { PageNotificationService } from '@nuvem/primeng-components';

import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { finalize } from 'rxjs/operators';
import { OfertaAmostra } from '../../shared/models/oferta-amostra.model';
import { ItemService } from './../../services/item.service';
import { OfertaService } from './../../services/oferta.service';
import { UsuarioService } from './../../services/usuario.service';
import { OfertaListagem } from './../../shared/models/oferta-listagem.model';

import { Oferta } from './../../shared/models/oferta.model';
import { Usuario } from './../../shared/models/usuario.model';
import { ListagemItensOfertadosComponent } from './../listagem-itens-ofertados/listagem-itens-ofertados.component';

@Component({
    selector: 'app-para-mim',
    templateUrl: './para-mim.component.html',
    styleUrls: ['./../listagem-itens-ofertados/listagem-itens-ofertados.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ParaMimComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    @ViewChild('itensOfertadosDisplay') itensOfertadosDisplay: ListagemItensOfertadosComponent;
    ofertasOfertado: OfertaAmostra[] = [];
    ofertasListagem: OfertaListagem[] = [];
    ofertaAmostra: OfertaAmostra;
    usuarioLogado: Usuario;
    contador: number = 0;
    responsiveOptions;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private ofertaService: OfertaService,
        private itemService: ItemService,
        private usuarioService: UsuarioService,
        private notification: PageNotificationService
    ) {
        this.responsiveOptions = [
            {
                breakpoint: '1024px',
                numVisible: 3,
                numScroll: 3
            },
            {
                breakpoint: '768px',
                numVisible: 2,
                numScroll: 2
            },
            {
                breakpoint: '560px',
                numVisible: 1,
                numScroll: 1
            }
        ];
    }

    aceitar(id) {
        this.blockUI.start(this._mensagemBlockUi);
        this.ofertaService.aceitar(id).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            () => {
                this.notification.addSuccessMessage('Oferta aceita com sucesso');
                this.buscarTodos();
            },
            () => {
                this.notification.addErrorMessage('Erro ao aceitar oferta');
            }
        );
    }

    buscarTodos() {
        this.blockUI.start(this._mensagemBlockUi);
        this.contador = 0;
        this.ofertasOfertado = [];
        this.usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
        this.ofertaService.listarPorOfertado(this.usuarioLogado.id).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (data) => {
                this.ofertasListagem = data;
                this.obterDetalhesOferta();
            }
        );
    }

    montarOfertaItem(base: Oferta) {
        this.itemService.obterPorId(base.itemId).subscribe(
            (data) => {
                this.ofertaAmostra.item = data;
                this.montarOfertaItemImagem();
                this.montarOfertaOfertante(base);
            }
        );
    }

    montarOfertaItemImagem() {
        let formatoImagem = 'data:image/jpg;base64,';
        formatoImagem = formatoImagem.concat(this.ofertaAmostra.item.imagem);
        this.ofertaAmostra.item.imagem = formatoImagem;
    }

    montarOfertaOfertante(base: Oferta) {
        this.usuarioService.obterPorId(base.usuarioOfertanteId).subscribe(
            (data) => {
                this.ofertaAmostra.usuarioOfertante = data;
                this.montarOfertaReceptor(base);
            }
        );
    }

    montarOfertaReceptor(base: Oferta) {
        this.usuarioService.obterPorId(this.ofertaAmostra.item.usuarioId).subscribe(
            (data) => {
                this.ofertaAmostra.usuarioDisponivel = data;
                this.ofertasOfertado.push(this.ofertaAmostra);
                this.contador++;
                this.obterDetalhesOferta();
            }
        );
    }

    ngOnInit() {
        this.buscarTodos();
    }

    obterDetalhesOferta() {
        if (this.contador < this.ofertasListagem.length) {
            this.ofertaAmostra = new OfertaAmostra();
            this.ofertaService.obterPorId(this.ofertasListagem[this.contador].id).subscribe(
                (data) => {
                    this.ofertaAmostra.id = data.id;
                    this.ofertaAmostra.itensOfertados = data.itensOfertados;
                    this.montarOfertaItem(data);
                }
            );
        } else {
            this.blockUI.stop();
        }
    }

    recusar(id) {
        this.blockUI.start(this._mensagemBlockUi);
        this.ofertaService.recusar(id).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            () => {
                this.notification.addSuccessMessage('Oferta recusada com sucesso');
                this.buscarTodos();
            },
            () => {
                this.notification.addErrorMessage('Erro ao recusar oferta');
            }
        );
    }

    showDisplay(id) {
        this.itensOfertadosDisplay.showDisplay(id);
    }

}
