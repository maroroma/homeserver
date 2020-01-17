import { AbstractIotComponent } from "./abstract-iot-component.modele";

export class BuzzerIotComponent extends AbstractIotComponent {
    public static dfFromRaw(rawFile: any): BuzzerIotComponent {
        const returnVAlue = new BuzzerIotComponent();
        AbstractIotComponent.basicMapping(returnVAlue, rawFile);
        return returnVAlue;
    }
}