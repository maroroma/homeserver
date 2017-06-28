import { CreateServerComponent } from './network/create-server.component';
import { ServerDetailComponent } from './network/server-detail.component';
import { NetworkService } from './network/network.service';
import { NetworkComponent } from './network/network.component';
import { PowerManagementComponent } from './power/power-management.component';
import { CacheListComponent } from './caches/cache-list.component';
import { RepositoryListComponent } from './repositories/repository-list.components';
import { PropertyListComponent } from './properties/property-list.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PluginListComponent } from './plugins/plugin-list.component';
import { CommonGUIModule } from './../common-gui/common-gui';
import { AdministrationComponent } from './administration.component';
import { AdministrationService } from './administration.service';
import { NgModule } from '@angular/core';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    // exports: [AdministrationComponent, PluginListComponent, RepositoryListComponent, CacheListComponent],
    declarations: [AdministrationComponent, PluginListComponent, PropertyListComponent,
        RepositoryListComponent, CacheListComponent, PowerManagementComponent, NetworkComponent,
        ServerDetailComponent, CreateServerComponent],
    providers: [AdministrationService, NetworkService]
})
export class AdministrationModule { }
