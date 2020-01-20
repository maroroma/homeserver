import { Component, OnInit } from "@angular/core";
import { SubMenuItem } from "app/common-gui/page-header/sub-menu-item.modele";
import { SubMenuBuilder } from "app/common-gui/page-header/sub-menu-builder.modele";

@Component({
    selector: 'homeserver-iot',
    templateUrl: 'iot.component.html'
})
export class IotComponent implements OnInit {

    public subMenuItems: Array<SubMenuItem>;

    constructor() { }


    ngOnInit() {
        this.subMenuItems = SubMenuBuilder
            .item('board').selected().search()
            .item('sprites').search()
            .item('manage').search()
            .buildSubMenu();
    }
}
