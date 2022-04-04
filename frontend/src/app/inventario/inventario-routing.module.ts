import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListarInventarioComponent } from './listar-inventario/listar-inventario.component';


const routes: Routes = [
    { path: '', component: ListarInventarioComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class InventarioRoutingModule {
}
