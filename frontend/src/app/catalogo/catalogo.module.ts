import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ItemModule } from './../item/item.module';

import { SharedModule } from './../shared/shared.module';

import { CatalogoRoutingModule } from './catalogo-routing.module';
import { CriarOfertaComponent } from './criar-oferta/criar-oferta.component';
import { ListagemCatalogoComponent } from './listagem-catalogo/listagem-catalogo.component';


@NgModule({
    declarations: [
        ListagemCatalogoComponent,
        CriarOfertaComponent
    ],
    imports: [
        CommonModule,
        CatalogoRoutingModule,
        SharedModule,
        ReactiveFormsModule,
        ItemModule
    ]
})
export class CatalogoModule {
}
