import {MusicPlayerContextAction} from "../../state/actions/MusicPlayerContextActions";
import {UpdateAllArtistsAction} from "../../state/actions/UpdateAllArtistsAction";
import {DisplayArtistCandidatesAction} from "../../state/actions/addArtistActions/DisplayArtistCandidatesAction";
import {DisplayToastAction} from "../../state/actions/toastActions/DisplayToastAction";
import {SimpleFile} from "../model/files/SimpleFile";
import {Album} from "../model/library/Album";
import {Artist} from "../model/library/Artist";
import {Track} from "../model/library/Track";
import {RequesterUtils} from "./RequesterUtils";

export class CreateArtistRequest {
    constructor(public artistDirectoryPath: string, public scanAlbums: boolean) { }
}

export class UpdateArtistRequest {
    constructor(public newName: string, public autoUpdateArts: boolean) { }
}

export class AddAlbumToArtistRequest {
    constructor(public albumToAddPath: string) { }
}




export class LibraryRequester {


    public static createArtist(selectedDirectory: SimpleFile): Promise<Artist[]> {
        console.log(JSON.stringify(new CreateArtistRequest(selectedDirectory.base64Path, true)));
        return fetch("musicplayer/library/artists",
            {
                method: 'POST',
                headers: RequesterUtils.defaultJsonHeaders(),
                body: JSON.stringify(new CreateArtistRequest(selectedDirectory.base64Path, true))
            })
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response);
    }

    public static deleteArtist(artistToDelete: Artist): Promise<Artist[]> {
        return RequesterUtils.delete(`musicplayer/library/artists/${artistToDelete.id}`);
    }

    public static getArtist(artistId: string): Promise<Artist> {
        return RequesterUtils.get(`musicplayer/library/artists/${artistId}`);
    }

    public static updateArtist(artistToUpdate: Artist, updateRequest: UpdateArtistRequest): Promise<Artist> {
        return RequesterUtils.update(`musicplayer/library/artists/${artistToUpdate.id}`, updateRequest);
    }

    public static addAlbumToArtist(albumDirectory: SimpleFile, artistForAlbum: Artist): Promise<Artist> {
        const addAlbumRequest = new AddAlbumToArtistRequest(albumDirectory.base64Path);
        return RequesterUtils.update(`musicplayer/library/artists/${artistForAlbum.id}/albums`, addAlbumRequest);
    }

    public static deleteAlbum(artistOwner: Artist, albumToDelete: Album): Promise<Artist> {
        return RequesterUtils.delete(`musicplayer/library/artists/${artistOwner.id}/albums/${albumToDelete.id}`);
    }

    public static getAllTracksForArtist(artistOwner: Artist): Promise<Track[]> {
        return fetch(`musicplayer/library/artists/${artistOwner.id}/tracks`)
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response)
    }



    public static getAllArtists(): Promise<Artist[]> {
        return fetch("musicplayer/library/artists")
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response)
    }

    public static getAlbumsForArtist(artist: Artist): Promise<Album[]> {
        return fetch(`musicplayer/library/artists/${artist.id}/albums`)
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response)
    }

    public static getArtistCandidates(): Promise<SimpleFile[]> {
        return fetch("musicplayer/library/artists/candidates")
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response)
    }

    public static getAlbumCandidates(artist: Artist): Promise<SimpleFile[]> {
        return fetch(`musicplayer/library/artists/${artist.id}/albums/candidates`)
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response)
    }

    public static getTracksForAlbum(album: Album): Promise<Track[]> {
        return fetch(`musicplayer/library/albums/${album.id}/tracks`)
            .then(response => RequesterUtils.handleErrors(response))
            .then(response => response);
    }

    public static getAllArtistsAndDispatchUpdate(dispatch: (action: MusicPlayerContextAction) => void) {
        LibraryRequester.getAllArtists()
            .then(allArtists => {
                if (allArtists.length > 0) {
                    dispatch(new UpdateAllArtistsAction(allArtists))
                }
            })
            .catch(error => {
                dispatch(DisplayToastAction.error("Erreur rencontrée lors de la récupération des artistes"))
            });
    }

    public static getArtistCandidatesAndDispatch(dispatch: (action: MusicPlayerContextAction) => void) {
        LibraryRequester.getArtistCandidates()
            .then(allCandidates => dispatch(new DisplayArtistCandidatesAction(allCandidates)))
            .catch(error => {
                dispatch(DisplayToastAction.error("Erreur rencontrée lors de la récupération des artistes à rajouter"))
            });
    }
}