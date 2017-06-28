import { FileDescriptor } from './../../../shared/file-descriptor.modele';
/**
 * Description d'un fichier à trier.
 * @export
 * @class CompletedFile
 * @extends {FileDescriptor}
 */
export class CompletedFile extends FileDescriptor {
    public new: boolean;
    public newName: string;

    public static dfFromRaw(rawFile: any): CompletedFile {
        const returnVAlue = FileDescriptor.genericFromRaw(rawFile, CompletedFile);
        returnVAlue.new = rawFile.new;
        returnVAlue.newName = returnVAlue.name;
        return returnVAlue;
    }

}

