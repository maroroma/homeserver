import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'homeserver-album-renderer',
    templateUrl: 'album-renderer.component.html',
    styleUrls: ['album-renderer.component.scss']
})

export class AlbumRendererComponent implements OnInit {


    @Input()
    public albumDescriptor: AlbumDescriptor;


    constructor() { }

    ngOnInit() { }
}