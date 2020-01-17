/**
 * Descripteur de base d'un composant IOT
 */
export class IotComponentDescriptor {
    public id:string;
    public componentType:string;
    public ipAddress:string;
    public name:string;

    public static dfFromRaw(rawFile: any): IotComponentDescriptor {
        const returnVAlue = new IotComponentDescriptor();
        returnVAlue.id = rawFile.id;
        returnVAlue.componentType = rawFile.componentType;
        returnVAlue.ipAddress = rawFile.ipAddress;
        returnVAlue.name = rawFile.name;
        return returnVAlue;
    }
}