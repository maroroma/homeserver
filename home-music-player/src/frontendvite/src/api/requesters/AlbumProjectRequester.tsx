import type { AlbumProject } from "../model/albumproject/AlbumProject";
import type { CreateAlbumProjectRequest } from "../model/albumproject/CreateAlbumProjectRequest";
import type { FileInProgress } from "../model/albumproject/FileInProgress";
import type { Album } from "../model/library/Album";
import { RequesterUtils } from "./RequesterUtils";

export class AlbumProjectRequester {
  public static createProject(projectRequest: CreateAlbumProjectRequest): Promise<AlbumProject> {
    return RequesterUtils.post("/api/musicplayer/library/albums/projects", projectRequest);
  }

  public static createProjectFromExistingAlbum(existingAlbum: Album): Promise<AlbumProject> {
    return RequesterUtils.post(`/api/musicplayer/library/albums/projects/${existingAlbum.id}`);
  }

  public static getOneProject(projectId: string): Promise<AlbumProject> {
    return RequesterUtils.get(`/api/musicplayer/library/albums/projects/${projectId}`)
  }

  public static deleteProject(projectId: string): Promise<AlbumProject> {
    return RequesterUtils.delete(`/api/musicplayer/library/albums/projects/${projectId}`)
  }

    public static deleteAllProject(): Promise<AlbumProject> {
    return RequesterUtils.delete(`/api/musicplayer/library/albums/projects`)
  }

  public static uploadTrackToAlbumProject(
    projectId: string,
    fileToUpload: FileInProgress
  ): Promise<FileInProgress> {

    if (fileToUpload.fileWithNewName.file) {
      const request = new FormData();
      request.append("file", fileToUpload.fileWithNewName.file);

      return RequesterUtils.upload(
        `/api/musicplayer/library/albums/projects/${projectId}/tracks`,
        request
      ).then(() => fileToUpload.asUploaded());
    } else {
      return Promise.resolve(fileToUpload);
    }


  }

  public static createAlbumOnMusicSource(projectId: string): Promise<AlbumProject> {
    return RequesterUtils.update(`/api/musicplayer/library/albums/projects/${projectId}/musicsource`);
  }

  public static renameOneFile(projectId: string, fileToUpload: FileInProgress): Promise<FileInProgress> {
    return RequesterUtils.update(`/api/musicplayer/library/albums/projects/${projectId}/tracks/name`, {
      initialFileName: fileToUpload.fileWithNewName.file?.name,
      newFileName: fileToUpload.fileWithNewName.newName
    })
      .then(() => fileToUpload.asRenamed())
  }

  public static applyMp3Tags(projectId: string, fileToUpload: FileInProgress): Promise<FileInProgress> {
    return RequesterUtils.update(`/api/musicplayer/library/albums/projects/${projectId}/tracks/tags`, {
      fileName: fileToUpload.fileWithNewName.newName
    })
      .then(() => fileToUpload.asTagged())
  }

  public static copyFileToMusicSource(projectId: string, fileToUpload: FileInProgress): Promise<FileInProgress> {
    return RequesterUtils.update(`/api/musicplayer/library/albums/projects/${projectId}/musicsource/tracks`, {
      fileName: fileToUpload.fileWithNewName.newName
    })
      .then(() => fileToUpload.asCopied())
  }


  // public static addTrackToPlayList(playListId: string, trackId: string): Promise<PlayList> {
  //     return RequesterUtils.put(`/api/musicplayer/playlists/${playListId}/tracks/${trackId}`, {})
  // }

  // public static removeTrackFromPlayList(playList: PlayList, trackId: string): Promise<PlayList> {
  //     return RequesterUtils.delete(`/api/musicplayer/playlists/${playList.playListId}/tracks/${trackId}`);
  // }

  // public static getAllPlayList(): Promise<PlayList[]> {
  //     return RequesterUtils.get("/api/musicplayer/playlists");
  // }



  // public static getTracksForPlayList(playList: PlayList): Promise<Track[]> {
  //     return RequesterUtils.get(`/api/musicplayer/playlists/${playList.playListId}/tracks`)
  // }

  // public static deletePlayList(playList: PlayList): Promise<any> {
  //     return RequesterUtils.delete(`/api/musicplayer/playlists/${playList.playListId}`)
  // }
}