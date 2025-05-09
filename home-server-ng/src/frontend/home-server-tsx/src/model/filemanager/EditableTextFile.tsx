import FileDescriptor from "./FileDescriptor";

export default class EditableTextFile {

    public static empty() :EditableTextFile {
        return new EditableTextFile(FileDescriptor.emptyFileDescriptor(), "");
    }

    constructor(public fileDescriptor:FileDescriptor, public content:string){}
}