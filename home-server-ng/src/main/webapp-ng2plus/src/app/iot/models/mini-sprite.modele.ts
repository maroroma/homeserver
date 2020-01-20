import { MiniPixel } from "./mini-pixel.modele";

export class MiniSprite {
    public name:string;
    public description:string;
    public lines:Array<Array<MiniPixel>>;

    public static emptySprite() : MiniSprite {
        const returnValue = new MiniSprite();
        returnValue.name = "new sprite";
        returnValue.description = "describe me";
        returnValue.lines = new Array<Array<MiniPixel>>();

        for (let indexLine = 0; indexLine < 8; indexLine++) {
            const newLine = new Array<MiniPixel>();
            for (let indexColumn = 0; indexColumn < 8; indexColumn++) {
                newLine.push(MiniPixel.offPixel());
            }
            returnValue.lines.push(newLine);
        }

        return returnValue;
    }

    public static fromRaw(rawData:any) : MiniSprite {
        const returnValue = new MiniSprite();
        returnValue.name = rawData.name;
        returnValue.description = rawData.description;
        returnValue.lines = rawData.lines.map(oneLine => 
            oneLine.map(onePixel => 
                MiniPixel.fromRaw(onePixel)));

        return returnValue;
    }
}