import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListagemCatalogoComponent } from './listagem-catalogo/listagem-catalogo.component';

const routes: Routes = [
    { path: '', component: ListagemCatalogoComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CatalogoRoutingModule {
}
