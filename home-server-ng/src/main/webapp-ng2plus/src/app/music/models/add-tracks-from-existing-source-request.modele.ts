import { FileDescriptor } from "../../shared/file-descriptor.modele";

export class AddTracksFromExistingSourceRequest {
    constructor(private fileIdsForWorkingDirectory: Array<string>) {

    }
}