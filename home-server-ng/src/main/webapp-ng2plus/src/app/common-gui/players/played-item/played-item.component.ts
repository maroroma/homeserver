import { Component, OnInit, Input } from '@angular/core';
import { FileDescriptor } from 'app/shared/file-descriptor.modele';

@Component({
    selector: 'homeserver-played-item',
    templateUrl: 'played-item.component.html',
    styleUrls: ['played-item.component.scss']
})

export class PlayedItemComponent implements OnInit {

    @Input()
    public fileToPlay: FileDescriptor;


    constructor() { }

    ngOnInit() { }
}