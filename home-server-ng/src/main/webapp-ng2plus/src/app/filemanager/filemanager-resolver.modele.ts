import { FileDescriptor } from './../shared/file-descriptor.modele';
import { ApiConstants } from './../shared/api-constants.modele';
import { DirectoryDescriptor } from './../shared/directory-descriptor.modele';
import { FileBrowserResolver } from './../common-gui/file-browser/file-browser-resolver.modele';

/**
 * Resolver d'url pour la seedbox
 * @export
 * @class TargetDirectoryResolver
 * @implements {FileBrowserResolver}
 */
export class FileManagerResolver implements FileBrowserResolver {
    public directoryDetailsUri(directory: DirectoryDescriptor): string {
        return ApiConstants.FILEMANAGER_DIRECTORIES_API + '/' + directory.id;
    }

    public directoryCreationUri(): string {
        return ApiConstants.FILEMANAGER_DIRECTORY_API;
    }

    public fileDeletionUri(file: FileDescriptor): string {
        return ApiConstants.FILEMANAGER_FILES_API + '/' + file.id;
    }

    public fileRenameUri(): string {
        return ApiConstants.FILEMANAGER_FILES_API;
    }

    public fileDownloadUri(file: FileDescriptor): string {
        return ApiConstants.FILEMANAGER_FILES_API + '/' + file.id;
    }

}
