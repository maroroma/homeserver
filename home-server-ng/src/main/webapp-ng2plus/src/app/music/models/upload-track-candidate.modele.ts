export class UploadTrackCandidate {
    public basicFile: File;
    public isMp3 = false;

    constructor(oneFile: File) {
        this.basicFile = oneFile;
        this.isMp3 = oneFile.name.endsWith('mp3');
    }
}