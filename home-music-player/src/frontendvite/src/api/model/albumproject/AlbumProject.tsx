export class AlbumProject {

    static empty(): AlbumProject {
        return new AlbumProject("", "", "", "", "", "", false);
    }

    constructor(public projectId: string,
        public artistId: string,
        public albumName: string,
        public albumArtBase64Path: string,
        public projectPath: string,
        public albumId: string,
        public fromExistingAlbum: boolean) {
    }
}