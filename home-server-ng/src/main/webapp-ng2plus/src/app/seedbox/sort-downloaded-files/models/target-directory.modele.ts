import { FileDescriptor } from './../../../shared/file-descriptor.modele';
export class TargetDirectory extends FileDescriptor {
    public type: string;
    // public subDirectories: Array<FileDescriptor>;

    public static dfFromRaw(rawFile: any): TargetDirectory {
        const returnVAlue = FileDescriptor.genericFromRaw(rawFile, TargetDirectory);

        returnVAlue.type = rawFile.type;
        // returnVAlue.subDirectories = new Array<FileDescriptor>();

        // rawFile.subDirectories.forEach(subDir => {
        //     returnVAlue.subDirectories.push(FileDescriptor.fromRaw(subDir));
        // });

        return returnVAlue;
    }
}

