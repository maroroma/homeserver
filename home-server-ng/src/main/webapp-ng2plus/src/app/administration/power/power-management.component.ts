import { HomeServerStatus } from './homeserver-status.modele';
import { AdministrationService } from './../administration.service';
import { PopupComponent } from './../../common-gui/popup/popup.component';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-power-management',
    templateUrl: 'power-management.component.html',
    styleUrls: ['../../common-gui/styles/progress-bar.scss']
})
export class PowerManagementComponent implements OnInit {
    @ViewChild('popupConfirmation')
    popupConfirmation: PopupComponent;
    @ViewChild('popupStopping')
    popupStopping: PopupComponent;

    public serverStatus: HomeServerStatus;


    constructor(private adminService: AdministrationService) { }


    ngOnInit() {
        this.adminService.getServerStatus().subscribe(res => {
            this.serverStatus = res;
        });
    }

    shutdown() {
        this.popupConfirmation.display();
    }

    public confirmShutdown() {
        this.adminService.shutdown().subscribe(res => {
            this.popupStopping.display();
        });
    }
}
