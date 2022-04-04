import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './../guard/auth.guard';

import { AdminComponent } from './admin.component';

const routes: Routes = [
    {
        path: '', component: AdminComponent, children: [
            {
                path: 'inventario',
                loadChildren: () => import('../inventario/inventario.module').then(m => m.InventarioModule),
                canLoad: [AuthGuard]
            },
            {
                path: 'catalogo',
                loadChildren: () => import('../catalogo/catalogo.module').then(m => m.CatalogoModule),
                canLoad: [AuthGuard]
            },
            { path: 'usuarios', loadChildren: () => import('../usuario/usuario.module').then(m => m.UsuarioModule), canLoad: [AuthGuard] },
            { path: 'itens', loadChildren: () => import('../item/item.module').then(m => m.ItemModule), canLoad: [AuthGuard] },
            {
                path: 'minhas-ofertas',
                loadChildren: () => import('../minhas-ofertas/minhas-ofertas.module').then(m => m.MinhasOfertasModule),
                canLoad: [AuthGuard]
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdminRoutingModule {
}
