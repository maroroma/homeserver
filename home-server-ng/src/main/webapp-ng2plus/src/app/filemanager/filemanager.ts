import { FileManagerService } from './filemanager.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonGUIModule } from './../common-gui/common-gui';
import { FileManagerComponent } from './filemanager.component';
import { NgModule } from '@angular/core';
import { DirectDownloadComponent } from './direct-download/direct-download.component';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [FileManagerComponent, DirectDownloadComponent],
    declarations: [FileManagerComponent, DirectDownloadComponent],
    providers: [FileManagerService],
})
export class FileBrowserModule { }
