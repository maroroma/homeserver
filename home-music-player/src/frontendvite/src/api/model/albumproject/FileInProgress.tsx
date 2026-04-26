import type { FileWithNewName } from "../../../components/uploaders/FilesUploadComponent";
import type { AlbumProject } from "./AlbumProject";

export class FileInProgress {

    public static fromAlbum(albumProject: AlbumProject): FileInProgress {
        return new FileInProgress({
            file: undefined,
            newName: albumProject.albumName
        },
            false,
            false,
            false,
            false,
            false);
    }

    public static fromFile(file: FileWithNewName): FileInProgress {
        return new FileInProgress(file, false, false, false, false, false);
    }

    public static albumSuccessFull(albumProject: AlbumProject): FileInProgress {
        return this.fromAlbum(albumProject).successfull();
    }

    constructor(public fileWithNewName: FileWithNewName,
        public uploaded: boolean,
        public renamed: boolean,
        public tagged: boolean,
        public copied: boolean,
        public inError: boolean) { }

    public successfull(): FileInProgress {
        return new FileInProgress(
            this.fileWithNewName,
            true, true, true, true, false);
    }

    public failed(): FileInProgress {
        return new FileInProgress(
            this.fileWithNewName,
            false, false, false, false, true);
    }

    public asUploaded(): FileInProgress {
        return new FileInProgress(
            this.fileWithNewName, true, this.renamed, this.tagged, this.copied, this.inError);
    }

    public asRenamed(): FileInProgress {
        return new FileInProgress(
            this.fileWithNewName, this.uploaded, true, this.tagged, this.copied, this.inError);
    }

    public asTagged(): FileInProgress {
       return new FileInProgress(
            this.fileWithNewName, this.uploaded, this.renamed, true, this.copied, this.inError);
    }

    public asCopied(): FileInProgress {
       return new FileInProgress(
            this.fileWithNewName, this.uploaded, this.renamed, this.tagged, true, this.inError);
    }
}