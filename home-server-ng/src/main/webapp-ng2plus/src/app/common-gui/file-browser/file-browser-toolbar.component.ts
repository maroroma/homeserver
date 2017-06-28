import { FileBrowserOptions } from './file-browser-options.modele';
import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'homeserver-file-browser-toolbar',
    templateUrl: 'file-browser-toolbar.component.html'
})

export class FileBrowserToolBarComponent implements OnInit {

    @Input()
    public options: FileBrowserOptions;

    constructor() { }
    ngOnInit() { }
}