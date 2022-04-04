import { CommonModule } from '@angular/common';

import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ItemModule } from '../item/item.module';
import { SharedModule } from './../shared/shared.module';
import { AlterarItensComponent } from './alterar-itens/alterar-itens.component';
import { InventarioRoutingModule } from './inventario-routing.module';
import { ListarInventarioComponent } from './listar-inventario/listar-inventario.component';


@NgModule({
    declarations: [
        AlterarItensComponent,
        ListarInventarioComponent
    ],
    imports: [
        CommonModule,
        InventarioRoutingModule,
        ReactiveFormsModule,
        SharedModule,
        ItemModule
    ]
})
export class InventarioModule {
}
