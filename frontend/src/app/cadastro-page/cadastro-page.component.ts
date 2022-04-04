import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PageNotificationService } from '@nuvem/primeng-components';

import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { finalize } from 'rxjs/operators';

import { UsuarioService } from '../services/usuario.service';

@Component({
    selector: 'app-cadastro-page',
    templateUrl: './cadastro-page.component.html',
    styleUrls: ['./cadastro-page.component.css']
})
export class CadastroPageComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    displayModal: boolean = false;
    form: FormGroup;
    isEditing: boolean = false;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private usuarioService: UsuarioService,
        private fb: FormBuilder,
        private notification: PageNotificationService,
        private router: Router
    ) {
    }

    iniciarForm() {
        this.form = this.fb.group({
            nome: [null, [Validators.required]],
            cpf: [null, [Validators.required, Validators.maxLength(11), Validators.minLength(11)]],
            email: [null, [Validators.required, Validators.email]],
            data: [null, [Validators.required]]
        });
    }

    loginUsuario() {
        this.router.navigate(['login']);
    }

    ngOnInit(): void {
        this.iniciarForm();
    }

    salvar() {
        this.blockUI.start(this._mensagemBlockUi);
        this.usuarioService.salvar(this.form.value).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            () => {
                this.notification.addSuccessMessage('Usuario Cadastrado com sucesso');
                this.router.navigate(['login']);
            },
            () => {
                this.notification.addErrorMessage('Erro ao cadastrar usuario');
            }
        );
    }

}
