import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CadastroItemComponent } from './cadastro-item/cadastro-item.component';
import { ListagemItensComponent } from './listagem-itens/listagem-itens.component';

const routes: Routes = [
    { path: '', component: ListagemItensComponent },
    { path: 'cadastro', component: CadastroItemComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ItemRoutingModule {
}
