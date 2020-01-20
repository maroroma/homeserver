export class MiniPixel {
    public on:boolean;

    public static fromRaw(rawData:any) : MiniPixel {
        const returnValue = new MiniPixel();
        returnValue.on = rawData.on;
        return returnValue;
    }

    public static offPixel() {
        const returnValue = new MiniPixel();
        returnValue.on = false;
        return returnValue;
    }
}