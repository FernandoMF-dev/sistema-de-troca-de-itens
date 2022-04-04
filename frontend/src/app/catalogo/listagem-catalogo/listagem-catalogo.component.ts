import { Component, OnInit, ViewChild } from '@angular/core';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { SelectItem } from 'primeng';
import { CategoriaService } from './../../services/categoria.service';

import { ItemService } from './../../services/item.service';
import { UsuarioService } from './../../services/usuario.service';
import { ItemAmostra } from './../../shared/models/item-amostra.model';
import { Item } from './../../shared/models/item.model';
import { Oferta } from './../../shared/models/oferta.model';
import { Usuario } from './../../shared/models/usuario.model';
import { CriarOfertaComponent } from './../criar-oferta/criar-oferta.component';

@Component({
    selector: 'app-listagem-catalogo',
    templateUrl: './listagem-catalogo.component.html',
    styleUrls: ['./listagem-catalogo.component.css']
})
export class ListagemCatalogoComponent implements OnInit {
    @BlockUI() blockUI: NgBlockUI;
    @ViewChild('dialogOferta') dialogOferta: CriarOfertaComponent;
    itens: Item[] = [];
    itensAmostra: ItemAmostra[] = [];
    itemSuporte: ItemAmostra = new ItemAmostra();
    sortOptions: SelectItem[];
    sortKey: string;
    sortField: string;
    sortOrder: number;
    displayOferta: boolean = false;
    itemSource: Item[];
    itemTarget: Item[];
    novaOferta: Oferta = new Oferta();
    usuarioLogado: Usuario;
    contador: number = 0;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private itemService: ItemService,
        private usuarioService: UsuarioService,
        private categoriaService: CategoriaService
    ) {
    }

    buscarTodos() {
        this.usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
        this.blockUI.start(this._mensagemBlockUi);
        this.itemService.listarDisponivelExcetoUsuario(this.usuarioLogado.id).subscribe(
            (itens) => {
                this.itens = itens;
                this.itens = this.montarImagens(this.itens);
                this.obterDetalhesItem();
            }
        );
    }

    montarCategoria(base: Item) {
        this.categoriaService.obterPorId(base.categoriaId).subscribe(
            (data) => {
                this.itemSuporte.categoria = data;
                this.itensAmostra.push(this.itemSuporte);
                this.contador++;
                this.obterDetalhesItem();
            }
        );
    }

    montarImagens(itens: Item[]) {
        itens.forEach(element => {
            let formatoImagem = 'data:image/jpg;base64,';
            let imagem = formatoImagem.concat(element.imagem);
            element.imagem = imagem;
        });
        return itens;
    }

    montarUsuarios(base: Item) {
        this.usuarioService.obterPorId(base.usuarioId).subscribe(
            (data) => {
                this.itemSuporte.usuario = data;
                this.montarCategoria(base);
            }
        );
    }

    ngOnInit() {
        this.buscarTodos();

        this.sortOptions = [
            { label: 'Nome A->Z', value: 'nome' },
            { label: 'Nome Z->A', value: '!nome' },
            { label: 'Categoria A->Z', value: 'categoria.descricao' },
            { label: 'Categoria Z->A', value: '!categoria.descricao' },
            { label: 'Dono A->Z', value: 'usuario.nome' },
            { label: 'Dono Z->A', value: '!usuario.nome' }
        ];
    }

    obterDetalhesItem() {
        if (this.contador < this.itens.length) {
            this.itemSuporte = new ItemAmostra();

            this.itemSuporte = {
                ...this.itens[this.contador],
                usuario: null,
                categoria: null
            };

            this.montarUsuarios(this.itens[this.contador]);
        } else {
            this.blockUI.stop();
        }
    }

    ofertar(itemDesejadoId) {
        this.dialogOferta.showOfertaDialog(itemDesejadoId);
    }

    onSortChange(event) {
        let value = event.value;

        if (value.indexOf('!') === 0) {
            this.sortOrder = -1;
            this.sortField = value.substring(1, value.length);
        } else {
            this.sortOrder = 1;
            this.sortField = value;
        }
    }

}
