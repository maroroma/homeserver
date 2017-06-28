import { SubMenuBuilder } from './../common-gui/page-header/sub-menu-builder.modele';
import { SubMenuItem } from './../common-gui/page-header/sub-menu-item.modele';
import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'homeserver-administration',
    templateUrl: 'administration.component.html'
})
export class AdministrationComponent implements OnInit {

    public subMenuItems: Array<SubMenuItem>;

    constructor() { }

    ngOnInit() {
        this.subMenuItems = SubMenuBuilder
            .item('plugins').selected().search()
            .item('properties').search()
            .item('repositories').search()
            .item('caches').search()
            .item('network').search()
            .item('power')
            .buildSubMenu();
    }

}

