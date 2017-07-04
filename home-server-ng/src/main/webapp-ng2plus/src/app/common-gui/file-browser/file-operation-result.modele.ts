import { FileDescriptor } from './../../shared/file-descriptor.modele';

/**
 * Correspond au résultat d'une suppresssion
 * @export
 * @class FileDeletionResult
 */
export class FileOperationResult {
    public initialFile: FileDescriptor;
    public completed: boolean;

    public static fromRaw(raw: any): FileOperationResult {
        const returnValue = new FileOperationResult();

        returnValue.initialFile = FileDescriptor.fromRaw(raw.initialFile);
        returnValue.completed = raw.completed;

        return returnValue;
    }

}
