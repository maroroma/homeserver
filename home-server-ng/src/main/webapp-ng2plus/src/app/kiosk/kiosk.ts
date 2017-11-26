import { KioskService } from './kiosk.service';
import { KioskMainScreenComponent } from './kiosk-main-screen.component';
import { KioskContentComponent } from './kiosk-content/kiosk-content.component';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CommonGUIModule } from 'app/common-gui/common-gui';
import { FormsModule } from '@angular/forms';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [KioskMainScreenComponent, KioskContentComponent],
    declarations: [KioskMainScreenComponent, KioskContentComponent],
    providers: [KioskService],
})
export class KioskModule { }
