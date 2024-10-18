import FileDescriptor from "../filemanager/FileDescriptor";
import FileToMoveDescriptor from "./FileToMoveDescriptor";

export default class MoveRequest {
    constructor(public filesToMove:FileToMoveDescriptor[], public target:FileDescriptor) {}
}