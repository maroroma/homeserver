import { Injectable } from "@angular/core";
import { AbstractIotComponent } from "./models/abstract-iot-component.modele";
import { BuzzerIotComponent } from "./models/buzzer-iot-component.modele";
import { JsonTools } from './../shared/json-tools.service';


/**
 * SErvice dédié à la désérialisation des components.
 * CEci pour régler les pb de polymorphisme sur les iot-components
 */
@Injectable()
export class IotComponentFactory {

    constructorsByComponentType:Map<String, (rawData: any) => AbstractIotComponent>;

    constructor() {
        this.constructorsByComponentType = new Map();
        this.constructorsByComponentType.set(AbstractIotComponent.BUZZER_COMPONENT_TYPE, rawData => BuzzerIotComponent.dfFromRaw(rawData));
    }

    /**
     * Permet de produire un iotComponent dans sa bonne instance
     * @param rawData 
     */
    deserializeIotComponent(rawData:any):AbstractIotComponent {
        const componentType = rawData.componentDescriptor.componentType;
        if(componentType && this.constructorsByComponentType.has(componentType)) {
            return this.constructorsByComponentType.get(componentType)(rawData);
        } else {
            return null;
        }
    }

    // deserializeIotComponents(rawData:any):Array<AbstratIotComponent> {
    //     return JsonTools.map(rawData, this.deserializeIotComponent);
    // }



}