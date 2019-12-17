import { SubMenuBuilder } from './../common-gui/page-header/sub-menu-builder.modele';
import { SubMenuItem } from './../common-gui/page-header/sub-menu-item.modele';
import { Component, OnInit } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-seedbox',
    templateUrl: 'seedbox.component.html'
})
export class SeedboxComponent implements OnInit {

    public subMenuItems: Array<SubMenuItem>;

    constructor() { }

    ngOnInit() {
        this.subMenuItems = SubMenuBuilder
            .item('TODO')
            .item('Downloads').selected()
            .buildSubMenu();
    }
}
