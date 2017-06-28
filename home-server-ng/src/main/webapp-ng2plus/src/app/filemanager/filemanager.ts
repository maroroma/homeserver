import { FileManagerService } from './filemanager.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonGUIModule } from './../common-gui/common-gui';
import { FileManagerComponent } from './filemanager.component';
import { NgModule } from '@angular/core';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [FileManagerComponent],
    declarations: [FileManagerComponent],
    providers: [FileManagerService],
})
export class FileBrowserModule { }
