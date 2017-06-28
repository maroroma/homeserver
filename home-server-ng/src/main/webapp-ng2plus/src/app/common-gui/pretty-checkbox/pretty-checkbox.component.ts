import { VisualItem } from './../../shared/visual-item.modele';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-pretty-checkbox',
    templateUrl: 'pretty-checkbox.component.html',
    styleUrls: ['pretty-checkbox.component.scss']
})
export class PrettyCheckboxComponent implements OnInit {

    @Input()
    activable: VisualItem<any>;

    @Output()
    change = new EventEmitter<VisualItem<any>>();

    uniqueId: string;

    constructor() {
        this.uniqueId = this.constructor.name + '_' + Math.ceil(Math.random() * 1000);
    }

    public update() {
        this.change.emit(this.activable);
    }

    ngOnInit() { }
}
