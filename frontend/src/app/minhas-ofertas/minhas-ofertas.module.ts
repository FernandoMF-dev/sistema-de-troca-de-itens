import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { SharedModule } from './../shared/shared.module';
import { ListagemItensOfertadosComponent } from './listagem-itens-ofertados/listagem-itens-ofertados.component';
import { MinhasOfertasListagemComponent } from './minhas-ofertas-listagem/minhas-ofertas-listagem.component';
import { MinhasOfertasRoutingModule } from './minhas-ofertas-routing.module';
import { ParaMimComponent } from './para-mim/para-mim.component';
import { PorMimComponent } from './por-mim/por-mim.component';


@NgModule({
    declarations: [MinhasOfertasListagemComponent, ListagemItensOfertadosComponent, PorMimComponent, ParaMimComponent],
    imports: [
        CommonModule,
        MinhasOfertasRoutingModule,
        SharedModule,
        ReactiveFormsModule
    ]
})
export class MinhasOfertasModule {
}
