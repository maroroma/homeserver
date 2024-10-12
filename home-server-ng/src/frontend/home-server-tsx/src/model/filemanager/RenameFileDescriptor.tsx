import FileDescriptor from "./FileDescriptor";

export default class RenameFileDescriptor {

    static empty(): RenameFileDescriptor {
        return new RenameFileDescriptor("", FileDescriptor.emptyFileDescriptor());
    }

    constructor(public newName: string, public originalFile: FileDescriptor) { }
}