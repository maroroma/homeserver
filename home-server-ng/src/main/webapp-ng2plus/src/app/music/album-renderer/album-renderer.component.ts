import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-album-renderer',
    templateUrl: 'album-renderer.component.html',
    styleUrls: ['album-renderer.component.scss']
})

export class AlbumRendererComponent implements OnInit {


    @Input()
    public albumDescriptor: AlbumDescriptor;

    @Output()
    albumClicked = new EventEmitter<AlbumDescriptor>();


    constructor() { }

    ngOnInit() { }

    clickOnAlbum() {
        this.albumClicked.emit(this.albumDescriptor);
    }

}