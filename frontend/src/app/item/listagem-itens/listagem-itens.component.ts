import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { SelectItem } from 'primeng';
import { finalize } from 'rxjs/operators';

import { ItemService } from 'src/app/services/item.service';
import { Item } from 'src/app/shared/models/item.model';
import { Oferta } from './../../shared/models/oferta.model';
import { Usuario } from './../../shared/models/usuario.model';

@Component({
    selector: 'app-listagem-itens',
    templateUrl: './listagem-itens.component.html',
    styleUrls: ['./listagem-itens.component.css']
})
export class ListagemItensComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    itens: Item[];
    form: FormGroup;
    sortOptions: SelectItem[];
    sortKey: string;
    sortField: string;
    sortOrder: number;
    displayOferta: boolean = false;
    itemSource: Item[];
    itemTarget: Item[];
    novaOferta: Oferta = new Oferta;
    usuarioLogado: Usuario;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private itemService: ItemService,
        private fb: FormBuilder
    ) {
    }

    buscarTodos() {
        this.blockUI.start(this._mensagemBlockUi);
        this.itemService.listar().pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (itens) => {
                this.itens = itens;
                this.itens = this.montarImagens(this.itens);
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

    montarImagens(itens: Item[]) {
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

        this.sortOptions = [
            { label: 'Nome A->Z', value: 'nome' },
            { label: 'Nome Z->A', value: '!nome' },
            { label: 'Categoria A->Z', value: 'categoriaId' },
            { label: 'Categoria Z->A', value: '!categoriaId' }
        ];
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
