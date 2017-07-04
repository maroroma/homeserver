import { FileDescriptor } from './../../shared/file-descriptor.modele';
export class RenameFileDescriptor {
    public newName: String;
    public originalFile: FileDescriptor;
    constructor(originalFile: FileDescriptor) {
        this.originalFile = originalFile;
        this.newName = this.originalFile.name;
    }
}