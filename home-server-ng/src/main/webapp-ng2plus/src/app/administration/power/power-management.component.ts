import { AdministrationService } from './../administration.service';
import { PopupComponent } from './../../common-gui/popup/popup.component';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-power-management',
    templateUrl: 'power-management.component.html'
})
export class PowerManagementComponent implements OnInit {
    @ViewChild('popupConfirmation')
    popupConfirmation: PopupComponent;
    @ViewChild('popupStopping')
    popupStopping: PopupComponent;


    constructor(private adminService: AdministrationService) { }


    ngOnInit() { }

    shutdown() {
        this.popupConfirmation.display();
    }

    public confirmShutdown() {
        this.adminService.shutdown().subscribe(res => {
            this.popupStopping.display();
        });
    }
}
