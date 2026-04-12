export class PlayList {

    public static empty(): PlayList {
        return new PlayList("", "", []);
    }

    constructor(public playListId: string, public name: string, public tracks: string[]) { }

}