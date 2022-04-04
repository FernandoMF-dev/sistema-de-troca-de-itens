import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SharedModule } from './../shared/shared.module';
import { ListagemPageComponent } from './listagem-page/listagem-page.component';
import { UsuarioRoutingModule } from './usuario-routing.module';


@NgModule({
    declarations: [
        ListagemPageComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        UsuarioRoutingModule,
        SharedModule,
        ReactiveFormsModule
    ]
})
export class UsuarioModule {
}
