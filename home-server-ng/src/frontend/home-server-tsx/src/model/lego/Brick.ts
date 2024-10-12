export default class Brick {


    static empty(): Brick {
        return new Brick("", "", 0, "", "")
    }

    static pictureUrlFromId(brick: Brick): string {
        return `/api/lego/bricks/${brick.id}/picture`;
    }

    static filter(searchString:string) : (brick:Brick) => boolean {
        return brick => brick.name.toLocaleLowerCase().includes(searchString.toLocaleLowerCase());
    }

    constructor(public id: string,
        public name: string,
        public drawerNumber: number,
        public pictureUrl: string,
        public pictureFileId: string) { }
}