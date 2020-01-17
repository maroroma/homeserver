import { NgModule } from "@angular/core";
import { CommonGUIModule } from "app/common-gui/common-gui";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { IotComponent } from "./iot-component";

@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [IotComponent],
    declarations: [IotComponent],
    providers: []
})
export class IotModule { }