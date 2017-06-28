import { VisualItem } from './../../shared/visual-item.modele';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-edit-collapsable-panel',
    templateUrl: 'edit-collapsable-panel.component.html',
    styleUrls: ['edit-collapsable-panel.component.scss']
})
export class EditCollapsablePanelComponent implements OnInit {

    @Input()
    public title: String;

    @Input()
    public item: VisualItem<any>;

    @Input()
    public displaySaveButton: boolean;
    @Input()
    public displayDeleteButton: boolean;

    @Output()
    expand = new EventEmitter<VisualItem<any>>();
    @Output()
    collapse = new EventEmitter<VisualItem<any>>();
    @Output()
    save = new EventEmitter<VisualItem<any>>();
    @Output()
    delete = new EventEmitter<VisualItem<any>>();

    constructor() { }

    ngOnInit() { }

    public innerExpand() {
        this.item.selected = true;
        this.expand.emit(this.item);
    }

    public innerCollapse() {
        this.item.selected = false;
        this.collapse.emit(this.item);
    }

    public innerSave() {
        this.save.emit(this.item);
    }
    public innerDelete() {
        this.delete.emit(this.item);
    }
}
