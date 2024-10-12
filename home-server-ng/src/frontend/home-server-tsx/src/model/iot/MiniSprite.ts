import MiniPixel from "./MiniPixel";

export default class MiniSprite {

    static empty(): MiniSprite {

        const defaultGrid: MiniPixel[][] = [];


        for (let line = 0; line < 8; line++) {
            const newLine: MiniPixel[] = [];
            for (let column = 0; column < 8; column++) {
                newLine.push(new MiniPixel(false));
            }
            defaultGrid.push(newLine);
        }
        return new MiniSprite("", "", defaultGrid);
    }


    constructor(public name: string, public description: string, public lines: MiniPixel[][]) { }
}