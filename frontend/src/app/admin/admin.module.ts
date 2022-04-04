import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { VersionTagModule } from '@nuvem/angular-base';
import { BreadcrumbModule, MenuModule } from '@nuvem/primeng-components';
import { BlockUIModule } from 'ng-block-ui';

import { AppFooterComponent } from './../components/footer/app.footer.component';
import { AppTopbarComponent } from './../components/topbar/app.topbar.component';
import { SharedModule } from './../shared/shared.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';

@NgModule({
    declarations: [
        AdminComponent,
        AppTopbarComponent,
        AppFooterComponent
    ],
    imports: [
        BlockUIModule.forRoot({
            message: 'Carregando...'
        }),
        CommonModule,
        AdminRoutingModule,
        SharedModule,
        BreadcrumbModule,
        MenuModule,
        VersionTagModule
    ]
})
export class AdminModule {
}
