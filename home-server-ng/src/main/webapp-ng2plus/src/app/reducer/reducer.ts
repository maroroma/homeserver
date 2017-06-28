import { ReducerSendMailComponent } from './reducer-send-mail/reducer-send-mail.component';
import { ReducerImageListComponent } from './reducer-image-list/reducer-image-list.component';
import { ReducerUploadComponent } from './reducer-upload/reducer-upload.component';
import { ReducerService } from './reducer.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CommonGUIModule } from './../common-gui/common-gui';
import { ReducerComponent } from './reducer.component';
import { NgModule } from '@angular/core';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [ReducerComponent],
    declarations: [ReducerComponent, ReducerUploadComponent, ReducerImageListComponent, ReducerSendMailComponent],
    providers: [ReducerService],
})
export class ReducerModule { }
