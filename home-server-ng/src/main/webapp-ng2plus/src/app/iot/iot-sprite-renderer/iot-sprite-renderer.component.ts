import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MiniSprite } from '../models/mini-sprite.modele';
import { VisualItem } from 'app/shared/visual-item.modele';

@Component({
    selector: 'homeserver-iot-sprite-renderer',
    templateUrl: 'iot-sprite-renderer.component.html',
    styleUrls: ['iot-sprite-renderer.component.scss']
})

export class IotSpriteRendererComponent implements OnInit {


    @Input()
    spriteToDisplay:VisualItem<MiniSprite>;

    @Input()
    displayActions=true;

    @Output()
    public delete = new EventEmitter<VisualItem<MiniSprite>>();
    @Output()
    public edit = new EventEmitter<VisualItem<MiniSprite>>();


    constructor() { }

    ngOnInit() { }

    public innerDelete() {
        this.delete.emit(this.spriteToDisplay);
    }
    public innerEdit() {
        this.edit.emit(this.spriteToDisplay);
    }
}