import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { finalize } from 'rxjs/operators';

import { LoginService } from './../services/login.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;
    form: FormGroup;
    displayModal: boolean = false;
    private _mensagemBlockUi: String = 'Carregando...';

    constructor(
        private fb: FormBuilder,
        private loginService: LoginService,
        private router: Router
    ) {
    }

    cadastrarUsuario() {
        this.router.navigate(['cadastro']);
    }

    iniciarForm() {
        this.form = this.fb.group({
            email: [null, [Validators.required, Validators.email]],
            token: [null, [Validators.required]]
        });
    }

    login() {
        this.blockUI.start(this._mensagemBlockUi);
        this.loginService.login(this.form.value).pipe(
            finalize(() => {
                this.blockUI.stop();
            })
        ).subscribe(
            (data) => {
                localStorage.setItem('token', this.form.get('token').value);
                localStorage.setItem('usuario', JSON.stringify(data));
                this.router.navigate(['admin']);
            }
        );
    }

    ngOnInit(): void {
        this.iniciarForm();
    }

}
