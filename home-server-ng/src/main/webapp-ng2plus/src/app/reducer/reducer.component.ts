import { ReducerImageListComponent } from './reducer-image-list/reducer-image-list.component';
import { ReducedImage } from './models/reduced-image.modele';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-reducer',
    templateUrl: 'reducer.component.html'
})
export class ReducerComponent implements OnInit {

    public displayMailComponent = false;

    public imagesToSend: Array<ReducedImage>;

    @ViewChild('reducedImageList')
    reducedImageList: ReducerImageListComponent;

    constructor() { }

    ngOnInit() { }

    public displaySendMailComponent(imagesToSend: Array<ReducedImage>): void {
        this.imagesToSend = imagesToSend;
        this.displayMailComponent = true;
    }

    public cancelSendMail(): void {
        this.displayMailComponent = false;
    }
}