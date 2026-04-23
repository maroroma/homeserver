export class CreateAlbumProjectRequest {
    constructor(public artistId: string, public albumArtAsBase64File: string, public albumName: string) { }
}