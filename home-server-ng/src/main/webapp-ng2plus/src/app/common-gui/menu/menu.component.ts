import { ModuleHandler } from './../../administration/plugins/module-handler.modele';
import { NavigationHelperService } from './../navigation-helper.service';
import { AdministrationService } from './../../administration/administration.service';
import { Component, OnInit } from '@angular/core';


@Component({
    selector: 'homeserver-menu',
    templateUrl: 'menu.component.html',
    styleUrls: ['menu.component.scss']
})
export class MenuComponent implements OnInit {

    public enabledModules: Array<ModuleHandler>;

    constructor(
        private adminService: AdministrationService
    ) { }

    ngOnInit() {
        this.enabledModules = this.adminService.displayedModules;
    }
}
