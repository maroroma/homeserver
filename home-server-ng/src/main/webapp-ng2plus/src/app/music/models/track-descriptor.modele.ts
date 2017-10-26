import { FileDescriptor } from './../../shared/file-descriptor.modele';
export class TrackDescriptor {
    public albumName: string;
    public artistName: string;
    public trackName: string;
    public trackNumber: number;
    public file: FileDescriptor;
    public newFileName: String;

    public static dfFromRaw(rawFile: any): TrackDescriptor {
        const returnVAlue = new TrackDescriptor();
        returnVAlue.albumName = rawFile.albumName;
        returnVAlue.artistName = rawFile.artistName;
        returnVAlue.trackName = rawFile.trackName;
        returnVAlue.trackNumber = rawFile.trackNumber;
        returnVAlue.newFileName = rawFile.newFileName;
        returnVAlue.file = FileDescriptor.fromRaw(rawFile.file);
        return returnVAlue;
    }
}
