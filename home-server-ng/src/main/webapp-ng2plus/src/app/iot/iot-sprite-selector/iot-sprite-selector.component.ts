import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { IotService } from '../iot.service';
import { VisualItemDataSource } from 'app/shared/visual-item-datasource.modele';
import { MiniSprite } from '../models/mini-sprite.modele';
import { VisualItem } from 'app/shared/visual-item.modele';

@Component({
    selector: 'homeserver-iot-sprite-selector',
    templateUrl: 'iot-sprite-selector.component.html',
    styleUrls: ['iot-sprite-selector.component.scss']
})

export class IotSpriteSelectorComponent implements OnInit {


    allSprites = new VisualItemDataSource<MiniSprite>();

    @Output()
    select = new EventEmitter<VisualItem<MiniSprite>>();

    constructor(private iotService:IotService) { }

    ngOnInit() { 
        this.iotService.getAllMiniSprites().subscribe(res => this.allSprites.updateSourceList(res));
    }

    innerSelect(selectedItem:VisualItem<MiniSprite>) {
        this.select.emit(selectedItem);
    }
}