import { NgModule } from "@angular/core";
import { CommonGUIModule } from "app/common-gui/common-gui";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { IotComponent } from "./iot.component";
import { IotService } from "./iot.service";
import { IotComponentFactory } from "./iot-component-factory.service";
import { IotBoardComponent } from "./iot-board/iot-board.component";

@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [IotComponent],
    declarations: [IotComponent, IotBoardComponent],
    providers: [IotService, IotComponentFactory]
})
export class IotModule { }