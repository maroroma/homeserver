import { KioskModule } from './../kiosk/kiosk';
import { HomeComponent } from './home.component';
import { NgModule } from '@angular/core';


@NgModule({
    imports: [KioskModule],
    exports: [HomeComponent],
    declarations: [HomeComponent],
    providers: [],
})
export class HomeComponentModule { }
