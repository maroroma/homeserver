import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { CompletedFile } from './completed-file.modele';
export class MoveRequest {
    public filesToMove: Array<CompletedFile>;
    public target: FileDescriptor;
}
