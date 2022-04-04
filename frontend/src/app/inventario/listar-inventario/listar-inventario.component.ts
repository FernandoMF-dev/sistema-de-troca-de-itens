import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { SelectItem } from 'primeng';
import { finalize } from 'rxjs/operators';
import { AlterarItensComponent } from 'src/app/inventario/alterar-itens/alterar-itens.component';
import { ItemService } from 'src/app/services/item.service';

import { CategoriaService } from './../../services/categoria.service';
import { Categoria } from './../../shared/models/categoria.model';
import { Item } from './../../shared/models/item.model';

@Component({
    selector: 'app-listar-inventario',
    templateUrl: './listar-inventario.component.html',
    styleUrls: ['./listar-inventario.component.css']
})
export class ListarInventarioComponent implements OnInit {


    @BlockUI() blockUI: NgBlockUI;
    @ViewChild('dialogItem') dialogItem: AlterarItensComponent;
    usuarioId: number;
    categoria: Categoria;
    categorias: Categoria[] = [];
    itens: Item[];
    itensOriginal: Item[];
    form: FormGroup;
    selectedItem: Item;
    itemSelecionado: Item;
    displayDialog: boolean;
    sortOptions: SelectItem[];
    sortKey: string;
    sortField: string;
    sortOrder: number;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private itemService: ItemService,
        private fb: FormBuilder,
        private categoriaService: CategoriaService,
        private router: Router
    ) {
    }

    alterarItem(event: Event, item: Item) {
        this.itemSelecionado = item;
        this.dialogItem.abrir(item);
        event.preventDefault();
    }

    buscarCategorias() {
        this.categoriaService.buscarTodos().pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (categorias) => {
                this.categorias = categorias;
            }
        );
    }

    buscarTodos() {
        this.usuarioId = JSON.parse(localStorage.getItem('usuario')).id;

        this.blockUI.start(this._mensagemBlockUi);
        this.itemService.inventarioListar(this.usuarioId).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (itens) => {
                this.itens = itens;
                this.itens = this.montarImagem(this.itens);
            }
        );
    }

    iniciarForm() {
        this.form = this.fb.group({
            id: [null],
            nome: [null, [Validators.required]],
            imagem: [null, [Validators.required]],
            descricao: [null, [Validators.required]],
            disponibilidade: [null, [Validators.required]],
            usuarioId: [null, [Validators.required]],
            categoriaId: [null, [Validators.required]]
        });
    }

    montarImagem(itens: Item[]) {
        itens.forEach(element => {
            let formatoImagem = 'data:image/jpg;base64,';
            let imagem = formatoImagem.concat(element.imagem);
            element.imagem = imagem;
        });
        return itens;
    }

    ngOnInit() {
        this.iniciarForm();
        this.buscarTodos();
        this.buscarCategorias();

        this.sortOptions = [
            { label: 'Nome A->Z', value: 'nome' },
            { label: 'Nome Z->A', value: '!nome' },
            { label: 'Categoria A->Z', value: 'categoriaId' },
            { label: 'Categoria Z->A', value: '!categoriaId' }
        ];
    }

    onAtualizou($event) {
        this.iniciarForm();
        this.buscarTodos();
        this.buscarCategorias();

        this.sortOptions = [
            { label: 'Nome A->Z', value: 'nome' },
            { label: 'Nome Z->A', value: '!nome' },
            { label: 'Categoria A->Z', value: 'categoriaId' },
            { label: 'Categoria Z->A', value: '!categoriaId' }
        ];
    }

    onDialogHide() {
        this.selectedItem = null;
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

    selectItem(event: Event, item: Item) {
        this.categorias.forEach(cat => {

            if (cat.id == item.categoriaId) {
            }
        });
        this.selectedItem = item;
        this.displayDialog = true;
        event.preventDefault();
    }

}
