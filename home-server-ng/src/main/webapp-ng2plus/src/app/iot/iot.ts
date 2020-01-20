import { NgModule } from "@angular/core";
import { CommonGUIModule } from "app/common-gui/common-gui";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { IotComponent } from "./iot.component";
import { IotService } from "./iot.service";
import { IotComponentFactory } from "./iot-component-factory.service";
import { IotBoardComponent } from "./iot-board/iot-board.component";
import { IotManagementComponent } from "./iot-management/iot-management.component";
import { IotSpritesComponent } from "./iot-sprites/iot-sprites.component";
import { IotSpriteEditorComponent } from "./iot-sprite-editor/iot-sprite-editor.component";
import { IotSpriteRendererComponent } from "./iot-sprite-renderer/iot-sprite-renderer.component";

@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [IotComponent],
    declarations: [IotComponent, IotBoardComponent, 
        IotManagementComponent, IotSpritesComponent,
        IotSpriteEditorComponent, IotSpriteRendererComponent
    ],
    providers: [IotService, IotComponentFactory]
})
export class IotModule { }