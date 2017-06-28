import { FileDescriptor } from './../../shared/file-descriptor.modele';

/**
 * Correspond au résultat d'une suppresssion
 * @export
 * @class FileDeletionResult
 */
export class FileDeletionResult {
    public initialFile: FileDescriptor;
    public deleted: boolean;

    public static fromRaw(raw: any): FileDeletionResult {
        const returnValue = new FileDeletionResult();

        returnValue.initialFile = FileDescriptor.fromRaw(raw.initialFile);
        returnValue.deleted = raw.deleted;

        return returnValue;
    }

}