import { FileBrowserResolver } from './../file-browser-resolver.modele';
import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'homeserver-download-file-list',
    templateUrl: 'download-file-list.component.html',
    styleUrls: ['download-file-list.component.scss']
})

export class DownloadFileListComponent implements OnInit {


    @Input()
    public fileList: Array<FileDescriptor>;

    @Input()
    public resolver: FileBrowserResolver;

    constructor() { }

    ngOnInit() { }
}
