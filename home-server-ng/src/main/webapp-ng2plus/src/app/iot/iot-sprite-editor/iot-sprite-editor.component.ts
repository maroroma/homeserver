import { Component, OnInit, Input } from '@angular/core';
import { MiniSprite } from '../models/mini-sprite.modele';
import { MiniPixel } from '../models/mini-pixel.modele';
import { VisualItem } from 'app/shared/visual-item.modele';

@Component({
    selector: 'homeserver-iot-sprite-editor',
    templateUrl: 'iot-sprite-editor.component.html',
    styleUrls: ['iot-sprite-editor.component.scss']
})

export class IotSpriteEditorComponent implements OnInit {
    constructor() { }

    @Input()
    spriteItem:VisualItem<MiniSprite>;
    
    @Input()
    creationMode=true;

    ngOnInit() { }

    switchPixel(onePixel:MiniPixel) {
        onePixel.on = !onePixel.on;
    }
}