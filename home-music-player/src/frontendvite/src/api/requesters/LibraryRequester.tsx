import type { SimpleFile } from "../model/files/SimpleFile";
import type { Album } from "../model/library/Album";
import type { Artist } from "../model/library/Artist";
import type { Track } from "../model/library/Track";
import { RequesterUtils } from "./RequesterUtils";

export class CreateArtistRequest {
  constructor(public artistDirectoryPath: string, public scanAlbums: boolean) { }
}

export class UpdateArtistRequest {
  constructor(public newName: string, public autoUpdateArts: boolean) { }
}

export class AddAlbumToArtistRequest {
  constructor(public albumToAddPath: string) { }
}

export class AddNewArtistFolderRequest {
  constructor(public artistName: string,
    public thumbAsBase64File: string,
    public fanartAsBase64File: string) { }
}

export class LibraryRequester {
  public static createArtist(selectedDirectory: SimpleFile): Promise<Artist[]> {
    console.log(
      JSON.stringify(
        new CreateArtistRequest(selectedDirectory.base64Path, true)
      )
    );
    return fetch("/api/musicplayer/library/artists", {
      method: "POST",
      headers: RequesterUtils.defaultJsonHeaders(),
      body: JSON.stringify(
        new CreateArtistRequest(selectedDirectory.base64Path, true)
      ),
    })
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static createArtistFolder(request: AddNewArtistFolderRequest): Promise<any> {
    return RequesterUtils.post("/api/musicplayer/library/artists/folder", request)
  }

  public static deleteArtist(artistToDelete: Artist): Promise<Artist[]> {
    return RequesterUtils.delete(
      `/api/musicplayer/library/artists/${artistToDelete.id}`
    );
  }

  public static getArtist(artistId: string): Promise<Artist> {
    return RequesterUtils.get(`/api/musicplayer/library/artists/${artistId}`);
  }

  public static getAlbum(albumId: string): Promise<Album> {
    return RequesterUtils.get(`/api/musicplayer/library/albums/${albumId}`);
  }

  public static updateArtist(
    artistToUpdate: Artist,
    updateRequest: UpdateArtistRequest
  ): Promise<Artist> {
    return RequesterUtils.update(
      `musicplayer/library/artists/${artistToUpdate.id}`,
      updateRequest
    );
  }

  public static addAlbumToArtist(
    albumDirectory: SimpleFile,
    artistForAlbum: Artist
  ): Promise<Artist> {
    const addAlbumRequest = new AddAlbumToArtistRequest(
      albumDirectory.base64Path
    );
    return RequesterUtils.update(
      `/api/musicplayer/library/artists/${artistForAlbum.id}/albums`,
      addAlbumRequest
    );
  }

  public static deleteAlbum(
    artistOwner: Artist,
    albumToDelete: Album
  ): Promise<Artist> {
    return RequesterUtils.delete(
      `/api/musicplayer/library/artists/${artistOwner.id}/albums/${albumToDelete.id}`
    );
  }

  public static addNewTrackToAlbum(
    albumToUpdate: Album,
    fileToUpload: File
  ): Promise<Track[]> {
    const request = new FormData();
    request.append("file", fileToUpload);

    return RequesterUtils.upload(
      `musicplayer/library/albums/${albumToUpdate.id}/tracks`,
      request
    );
  }

  public static addNewTrackToAlbumFromMusicSourceDirectory(
    albumToUpdate: string,
  ): Promise<Album> {
    return RequesterUtils.update(
      `/api/musicplayer/library/albums/${albumToUpdate}/tracks`);
  }

  public static getAllTracksForArtist(artistOwner: Artist): Promise<Track[]> {
    return fetch(`/api/musicplayer/library/artists/${artistOwner.id}/tracks`)
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static getTrack(trackId: string): Promise<Track> {
    return fetch(`/api/musicplayer/library/tracks/${trackId}`)
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static getAllArtists(): Promise<Artist[]> {
    return fetch("/api/musicplayer/library/artists")
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static getAlbumsForArtist(artist: Artist): Promise<Album[]> {
    return fetch(`/api/musicplayer/library/artists/${artist.id}/albums`)
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static getArtistCandidates(): Promise<SimpleFile[]> {
    return fetch("/api/musicplayer/library/artists/candidates")
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static getAlbumCandidates(artist: Artist): Promise<SimpleFile[]> {
    return fetch(`/api/musicplayer/library/artists/${artist.id}/albums/candidates`)
      .then((reponse) => RequesterUtils.handleErrors(reponse))
      .then((response) => response);
  }

  public static getTracksForAlbum(album: Album): Promise<Track[]> {
    return fetch(`/api/musicplayer/library/albums/${album.id}/tracks`)
      .then((response) => RequesterUtils.handleErrors(response))
      .then((response) => response);
  }

  // public static getAllArtistsAndDispatchUpdate(dispatch: (action: MusicPlayerContextAction) => void) {
  //     LibraryRequester.getAllArtists()
  //         .then(allArtists => {
  //             if (allArtists.length > 0) {
  //                 dispatch(new UpdateAllArtistsAction(allArtists))
  //             }
  //         })
  //         .catch(error => {
  //             dispatch(DisplayToastAction.error("Erreur rencontrée lors de la récupération des artistes"))
  //         });
  // }

  // public static getArtistCandidatesAndDispatch(dispatch: (action: MusicPlayerContextAction) => void) {
  //     LibraryRequester.getArtistCandidates()
  //         .then(allCandidates => dispatch(new DisplayArtistCandidatesAction(allCandidates)))
  //         .catch(error => {
  //             dispatch(DisplayToastAction.error("Erreur rencontrée lors de la récupération des artistes à rajouter"))
  //         });
  // }
}
