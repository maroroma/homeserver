import { ImageViewerComponent } from './../../common-gui/image-viewer/image-viewer.component';
import { PopupComponent } from './../../common-gui/popup/popup.component';
import { VisualItem } from './../../shared/visual-item.modele';
import { ReducedImage } from './../models/reduced-image.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { ReducerService } from './../reducer.service';
import { Component, OnInit, ViewChild, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-reducer-image-list',
    templateUrl: 'reducer-image-list.component.html',
    styleUrls: ['../../common-gui/styles/image-container.scss', '../../common-gui/styles/edit-zone.scss']
})
export class ReducerImageListComponent implements OnInit {

    public reducedImages = new VisualItemDataSource<ReducedImage>();

    public nbFileToDelete = 0;

    @Output()
    public sendMail = new EventEmitter<Array<ReducedImage>>();

    /**
         * Popup pour la gestion de la suppression
         * @type {PopupComponent}
         * @memberOf CompletedFileListComponent
         */
    @ViewChild('popupConfirmation')
    popupStopping: PopupComponent;

    @ViewChild('imageViewer')
    imageViewer: ImageViewerComponent;

    constructor(private reducerService: ReducerService) { }

    ngOnInit() {
        this.reducerService.loadReducedImages().subscribe(res => this.reducedImages.updateSourceList(res));
    }

    public toggleImage(image: VisualItem<ReducedImage>): void {
        this.reducedImages.toggleItemSelection(image);
    }


    deleteSelectedFiles(): void {
        this.nbFileToDelete = this.reducedImages.nbItemSelected;
        this.popupStopping.display();
    }

    confirmDeletion(): void {
        this.reducerService
            .deleteReducedImages(this.reducedImages.getSelectedItem().map(selected => selected.item))
            .subscribe(res => this.reducedImages.updateSourceList(res));
    }

    displayImageGallery(): void {
        this.imageViewer.display(this.reducedImages.sourceList
            .map(wrapped => wrapped.item), fd => 'api/reducer/reducedImages/' + fd.id);
    }

    public updateImageList(imageList: Array<ReducedImage>): void {
        this.reducedImages.updateSourceList(imageList);
    }

    public innerSendMail(): void {
        this.sendMail.emit(this.reducedImages.getSelectedItem().map(vi => vi.item));
    }


}
