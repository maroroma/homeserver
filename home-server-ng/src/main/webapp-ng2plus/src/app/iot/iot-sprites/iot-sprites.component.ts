import { Component, OnInit, ViewChild } from '@angular/core';
import { IotService } from '../iot.service';
import { PopupComponent } from 'app/common-gui/popup/popup.component';
import { MiniSprite } from '../models/mini-sprite.modele';
import { VisualItem } from 'app/shared/visual-item.modele';
import { IotSpriteEditorComponent } from '../iot-sprite-editor/iot-sprite-editor.component';
import { NotifyerService } from 'app/common-gui/notifyer/notifyer.service';
import { VisualItemDataSource } from 'app/shared/visual-item-datasource.modele';
import { Subscription } from 'rxjs';

@Component({
    selector: 'homeserver-iot-sprites',
    templateUrl: 'iot-sprites.component.html',
    styleUrls: ['../../common-gui/styles/shared.scss', '../../common-gui/styles/edit-zone.scss', 'iot-sprites.component.scss']
})

export class IotSpritesComponent implements OnInit {


    public allSprites = new VisualItemDataSource<MiniSprite>();

    constructor(private iotService:IotService, private notifyer:NotifyerService) { }


    @ViewChild('popupSpriteEditor')
    popupSpriteEditor: PopupComponent;
    @ViewChild('spriteEditor')
    spriteEditor: IotSpriteEditorComponent;

    popupSubscription:Subscription;

    ngOnInit() { 

        // tri par défaut sur le nom du sprite
        this.allSprites.sortByField('item.name');

        this.iotService.getAllMiniSprites()
        .subscribe(res => {
            this.allSprites.updateSourceList(res);
            this.notifyer.showInfo('liste de sprites mise à jour');
            console.log(this.allSprites.displayList);
        });
    }

    displayPopupSpriteCreation() {
        if(this.popupSubscription) {
            this.popupSubscription.unsubscribe();
        }
        const spriteToBeCreated = new VisualItem<MiniSprite>(MiniSprite.emptySprite());
        
        this.spriteEditor.spriteItem = spriteToBeCreated;
        this.spriteEditor.creationMode = true;

        this.popupSpriteEditor.title = 'Creer un nouveau sprite';
        this.popupSubscription = this.popupSpriteEditor.ok
            .flatMap(res => this.iotService.saveNewMiniSprite(spriteToBeCreated.item))
            .subscribe(createdRes => {
                this.allSprites.updateSourceList(createdRes);
                this.notifyer.showSuccess('sprite créé');
            });
        this.popupSpriteEditor.display();
    }

    displayPopupSpriteModification(spriteToUpdate:VisualItem<MiniSprite>) {
        if(this.popupSubscription) {
            this.popupSubscription.unsubscribe();
        }
        this.spriteEditor.spriteItem = spriteToUpdate;
        this.spriteEditor.creationMode = false;
        this.popupSpriteEditor.title = 'Modification du sprite ' + spriteToUpdate.item.name;
        this.popupSubscription = this.popupSpriteEditor.ok
            .flatMap(res => this.iotService.updateMiniSprite(spriteToUpdate.item))
            .subscribe(createdRes => {
                this.allSprites.updateSourceList(createdRes);
                this.notifyer.showSuccess('sprite modifié');
            });
        this.popupSpriteEditor.display();
    }

    deleteSprite(spriteToDelete:VisualItem<MiniSprite>) {
        this.iotService.deleteMiniSprite(spriteToDelete.item).subscribe(res => {
            this.allSprites.updateSourceList(res);
            this.notifyer.showSuccess('sprite supprimé');
        });
    }
}