import { NetworkService } from './network.service';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { ServerDescriptor } from './server-descriptor.modele';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-create-server',
    templateUrl: 'create-server.component.html',
    styleUrls: ['create-server.component.scss', '../../common-gui/styles/dropdown-menu.scss']
})
export class CreateServerComponent implements OnInit {


    public newServerDescriptor = new ServerDescriptor();
    public availableServers = new VisualItemDataSource<ServerDescriptor>();
    public isMenuOpen = false;

    @Output()
    public save = new EventEmitter<ServerDescriptor>();

    constructor(private networkService: NetworkService) { }

    ngOnInit() {
        this.newServerDescriptor = new ServerDescriptor();
        this.loadAvailableServers();
    }

    public innerSave() {
        this.save.emit(this.newServerDescriptor);
        this.ngOnInit();
    }

    public innerCancel() {
        this.ngOnInit();
    }

    public loadAvailableServers() {
        this.closeMenu();
        this.networkService.loadAvailableServers().subscribe(res => this.availableServers.updateSourceList(res));
    }

    public switchMenu(): void {
        this.isMenuOpen = !this.isMenuOpen;
    }

    public closeMenu(): void {
        this.isMenuOpen = false;
    }

    public reinit(): void {
        this.newServerDescriptor.description = '';
        this.newServerDescriptor.ip = '';
        this.newServerDescriptor.nom = '';
        this.closeMenu();
    }

    public preselectServer(serverItem: ServerDescriptor): void {
        this.newServerDescriptor = serverItem;
        this.closeMenu();
    }

}
