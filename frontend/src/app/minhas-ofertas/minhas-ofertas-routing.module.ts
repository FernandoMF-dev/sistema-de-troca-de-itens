import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MinhasOfertasListagemComponent } from './minhas-ofertas-listagem/minhas-ofertas-listagem.component';

const routes: Routes = [
    { path: '', component: MinhasOfertasListagemComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MinhasOfertasRoutingModule {
}
