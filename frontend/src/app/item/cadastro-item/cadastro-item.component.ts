import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PageNotificationService } from '@nuvem/primeng-components';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

import { finalize } from 'rxjs/operators';
import { Categoria } from 'src/app/shared/models/categoria.model';
import { CategoriaService } from './../../services/categoria.service';
import { ItemService } from './../../services/item.service';


@Component({
    selector: 'app-cadastro-item',
    templateUrl: './cadastro-item.component.html',
    styleUrls: ['./cadastro-item.component.css']
})

export class CadastroItemComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    private _mensagemBlockUi: String = 'Carregando...';

    private form: FormGroup;
    private categorias: Categoria[] = [];
    private imagem: any;
    private usuarioLogado: any;

    constructor(
        private itensServices: ItemService,
        private categoriaService: CategoriaService,
        private formBuilder: FormBuilder,
        private notification: PageNotificationService,
        private router: Router
    ) {
    }

    buscarCategorias() {
        this.blockUI.start(this._mensagemBlockUi);
        this.categoriaService.buscarTodos().pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (categorias) => {
                this.categorias = categorias;
                console.log(this.categorias);
            }
        );
    }

    iniciarForm() {
        this.form = this.formBuilder.group({
            id: [null],
            descricao: [null, [Validators.required]],
            disponibilidade: [null, [Validators.required]],
            nome: [null, [Validators.required]],
            imagem: [null, [Validators.required]],
            usuarioId: [null, [Validators.required]],
            categoriaId: [null, [Validators.required]]
        });
    }

    ngOnInit(): void {
        this.iniciarForm();
        this.buscarCategorias();
    }

    salvar() {
        this.usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
        this.form.controls['usuarioId'].setValue(this.usuarioLogado.id);
        this.blockUI.start(this._mensagemBlockUi);
        this.itensServices.salvar(this.form.value).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            () => {
                this.notification.addSuccessMessage('Item cadastrado com sucesso');
                this.router.navigate(['..']);
            },
            (error) => {
                this.notification.addErrorMessage('Erro ao cadastrar item');
            }
        );

    }

    uploadImagem(event) {
        let fileReader = new FileReader();

        let file = event.currentFiles[0];

        fileReader.onloadend = () => {
            this.imagem = fileReader.result;

            let blob = this.imagem.split(',');

            this.form.controls['imagem'].setValue(blob[1]);
        };
        fileReader.readAsDataURL(file);
    }
}
