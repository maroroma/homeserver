import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';


/**
 * PErmet de fournir les url des apis utilisées en sous main pour la manipulation des dossiers.
 * @export
 * @interface FileBrowserResolver
 */
export interface FileBrowserResolver {
    directoryDetailsUri(directory: DirectoryDescriptor): string;
    directoryCreationUri(): string;
    fileDeletionUri(file: FileDescriptor): string;
    fileRenameUri(): string;
    fileDownloadUri(file: FileDescriptor): string;
    fileUploadUri(directory: DirectoryDescriptor): string;
}
