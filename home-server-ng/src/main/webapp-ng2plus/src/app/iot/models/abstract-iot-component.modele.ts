import { IotComponentDescriptor } from "./iot-component-descriptor.modele";

export abstract class AbstractIotComponent {

    public static readonly BUZZER_COMPONENT_TYPE = 'BUZZER';

    public componentDescriptor:IotComponentDescriptor;
    public glyphicon:string;
    public available:boolean;

    public static basicMapping<T extends AbstractIotComponent>(instanceToPopulate:T, rawFile: any): T {

        instanceToPopulate.componentDescriptor = IotComponentDescriptor.dfFromRaw(rawFile.componentDescriptor);
        instanceToPopulate.glyphicon = rawFile.glyphicon;
        instanceToPopulate.available = rawFile.available;

        return instanceToPopulate;
    }
}