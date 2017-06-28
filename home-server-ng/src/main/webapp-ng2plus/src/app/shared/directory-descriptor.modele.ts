import { JsonTools } from './json-tools.service';
import { FileDescriptor } from './file-descriptor.modele';
export class DirectoryDescriptor extends FileDescriptor {

    public files: Array<FileDescriptor>;
    public directories: Array<FileDescriptor>;

    public static dfFromRaw(rawFile: any): DirectoryDescriptor {
        const returnVAlue = FileDescriptor.genericFromRaw(rawFile, DirectoryDescriptor);
        returnVAlue.files = JsonTools.map(rawFile.files, FileDescriptor.fromRaw);
        returnVAlue.directories = JsonTools.map(rawFile.directories, FileDescriptor.fromRaw);
        return returnVAlue;
    }



}
