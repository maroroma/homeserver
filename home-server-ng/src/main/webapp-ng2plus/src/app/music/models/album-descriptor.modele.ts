import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';

export class AlbumDescriptor {
    public albumName: string;
    public artistName: string;
    public directoryDescriptor: DirectoryDescriptor;
    public albumart: FileDescriptor;

    public static dfFromRaw(rawFile: any): AlbumDescriptor {
        const returnVAlue = new AlbumDescriptor();
        returnVAlue.albumName = rawFile.albumName;
        returnVAlue.artistName = rawFile.artistName;
        returnVAlue.directoryDescriptor = DirectoryDescriptor.dfFromRaw(rawFile.directoryDescriptor);
        if (rawFile.albumart !== null) {
            returnVAlue.albumart = FileDescriptor.fromRaw(rawFile.albumart);
        }
        return returnVAlue;
    }
}
