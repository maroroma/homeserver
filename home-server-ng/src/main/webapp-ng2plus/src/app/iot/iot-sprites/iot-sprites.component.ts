import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { IotService } from '../iot.service';
import { PopupComponent } from 'app/common-gui/popup/popup.component';
import { MiniSprite } from '../models/mini-sprite.modele';
import { VisualItem } from 'app/shared/visual-item.modele';
import { IotSpriteEditorComponent } from '../iot-sprite-editor/iot-sprite-editor.component';
import { NotifyerService } from 'app/common-gui/notifyer/notifyer.service';
import { VisualItemDataSource } from 'app/shared/visual-item-datasource.modele';
import { Subscription } from 'rxjs';
import { PageHeaderSearchService } from 'app/common-gui/page-header/page-header-search.service';
import { FilterTools } from 'app/shared/filter-tools.service';

@Component({
    selector: 'homeserver-iot-sprites',
    templateUrl: 'iot-sprites.component.html',
    styleUrls: ['../../common-gui/styles/shared.scss', '../../common-gui/styles/edit-zone.scss', 'iot-sprites.component.scss']
})

export class IotSpritesComponent implements OnInit, OnDestroy {


    public allSprites = new VisualItemDataSource<MiniSprite>();

    constructor(private iotService:IotService, private notifyer:NotifyerService, private searchService: PageHeaderSearchService) { }


    @ViewChild('popupSpriteEditor')
    popupSpriteEditor: PopupComponent;
    @ViewChild('spriteEditor')
    spriteEditor: IotSpriteEditorComponent;

    popupEditorOkSubscription:Subscription;
    popupEditorCancelSubscription:Subscription;
    public searchSubscription: Subscription;

    ngOnInit() { 

        // tri par défaut sur le nom du sprite
        this.allSprites.sortByField('item.name');

        this.iotService.getAllMiniSprites()
        .subscribe(res => {
            this.allSprites.updateSourceList(res);
            this.notifyer.showInfo('liste de sprites mise à jour');
            console.log(this.allSprites.displayList);
        });

        this.searchSubscription = this.searchService.searchChanged
        .subscribe(res => this.allSprites.filterBy(FilterTools.simpleFilter(res, 'name')));
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }

    displayPopupSpriteCreation() {
        if(this.popupEditorOkSubscription) {
            this.popupEditorOkSubscription.unsubscribe();
        }
        if(this.popupEditorCancelSubscription) {
            this.popupEditorCancelSubscription.unsubscribe();
        }
        const spriteToBeCreated = new VisualItem<MiniSprite>(MiniSprite.emptySprite());
        
        this.spriteEditor.spriteItem = spriteToBeCreated;
        this.spriteEditor.creationMode = true;

        this.popupSpriteEditor.title = 'Creer un nouveau sprite';
        this.popupEditorOkSubscription = this.popupSpriteEditor.ok
            .flatMap(res => this.iotService.saveNewMiniSprite(spriteToBeCreated.item))
            .subscribe(createdRes => {
                this.allSprites.updateSourceList(createdRes);
                this.notifyer.showSuccess('sprite créé');
            });
        
        this.popupSpriteEditor.display();
    }

    displayPopupSpriteModification(spriteToUpdate:VisualItem<MiniSprite>) {
        if(this.popupEditorOkSubscription) {
            this.popupEditorOkSubscription.unsubscribe();
        }
        if(this.popupEditorCancelSubscription) {
            this.popupEditorCancelSubscription.unsubscribe();
        }
        this.spriteEditor.spriteItem = spriteToUpdate;
        this.spriteEditor.creationMode = false;
        this.popupSpriteEditor.title = 'Modification du sprite ' + spriteToUpdate.item.name;
        this.popupEditorOkSubscription = this.popupSpriteEditor.ok
            .flatMap(res => this.iotService.updateMiniSprite(spriteToUpdate.item))
            .subscribe(createdRes => {
                this.allSprites.updateSourceList(createdRes);
                this.notifyer.showSuccess('sprite modifié');
            });

        this.popupEditorCancelSubscription = this.popupSpriteEditor.ko
        .subscribe(res => spriteToUpdate.autoRestaure());
        this.popupSpriteEditor.display();
    }

    deleteSprite(spriteToDelete:VisualItem<MiniSprite>) {
        this.iotService.deleteMiniSprite(spriteToDelete.item).subscribe(res => {
            this.allSprites.updateSourceList(res);
            this.notifyer.showSuccess('sprite supprimé');
        });
    }
}