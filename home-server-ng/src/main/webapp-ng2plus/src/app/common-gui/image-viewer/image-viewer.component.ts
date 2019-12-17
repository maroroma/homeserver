import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import {
    trigger,
    state,
    style,
    animate,
    transition, keyframes
} from '@angular/animations';
import { HostListener } from '@angular/core';

/**
 * PErmet d'afficher une gallerie d'images
 * @export
 * @class ImageViewerComponent
 * @implements {OnInit}
 */
@Component({
    moduleId: module.id,
    selector: 'homeserver-image-viewer',
    templateUrl: 'image-viewer.component.html',
    styleUrls: ['image-viewer.component.scss'],
    animations: [trigger('flyInOut', [
        state('display', style({ opacity: 1, display: 'block' })),
        state('hide', style({ opacity: 0, display: 'none' })),
        transition('hide => display', animate('300ms ease-in')),
        transition('display => hide', animate('100ms ease-out'))
    ])]
})
export class ImageViewerComponent implements OnInit {

    /**
         * pilotage de l'animation d'affichage
         * @memberOf PopupComponent
         */
    public currentState = 'hide';

    public isVisible = false;

    public imageList: Array<FileDescriptor>;

    public currentIndex = 0;

    public urlResolver: (FileDescriptor) => string;

    constructor() { }

    ngOnInit() { }

    public display(imageList: Array<FileDescriptor>, urlResolver: (FileDescriptor) => string) {
        this.imageList = imageList;
        this.urlResolver = urlResolver;
        this.isVisible = true;
        this.currentIndex = 0;
        this.currentState = 'display';
    }

    /**
     * ON peut masquer le composant si la touche ESC est levée
     */
    public hide(): void {
        this.isVisible = false;
        this.currentState = 'hide';
    }

    @HostListener('window:keydown', ['$event'])
    public handleKeyBoard(event?: KeyboardEvent): void {
        if (event.key === 'Escape') {
            this.hide();
        }

        if (event.key === 'ArrowRight') {
            this.next();
        }

        if (event.key === 'ArrowLeft') {
            this.previous();
        }

    }

    public next(): void {
        this.currentIndex++;
        if (this.currentIndex >= this.imageList.length) {
            this.currentIndex = 0;
        }
    }

    public previous(): void {
        this.currentIndex--;
        if (this.currentIndex < 0) {
            this.currentIndex = this.imageList.length - 1;
        }
    }

    public onKeyUp(): void {
        console.log('onkeyup');
    }

    public blur(): void {
        console.log('hello world');
    }


}
