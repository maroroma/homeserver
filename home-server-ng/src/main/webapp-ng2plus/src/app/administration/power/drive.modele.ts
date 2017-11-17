export class Drive {
    public name: string;
    public totalSpace: number;
    public freeSpace: number;
    public percentageUsed: number;
    public usedSpace: number;


    public static fromRaw(rawJson: any): Drive {
        const returnValue = new Drive();

        returnValue.name = rawJson.name;
        returnValue.totalSpace = rawJson.totalSpace;
        returnValue.freeSpace = rawJson.freeSpace;
        returnValue.percentageUsed = rawJson.percentageUsed;
        returnValue.usedSpace = rawJson.usedSpace;

        return returnValue;
    }
}