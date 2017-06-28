import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { ApiConstants } from './../../../shared/api-constants.modele';
import { DirectoryDescriptor } from './../../../shared/directory-descriptor.modele';
import { FileBrowserResolver } from './../../../common-gui/file-browser/file-browser-resolver.modele';

/**
 * Resolver d'url pour la seedbox
 * @export
 * @class TargetDirectoryResolver
 * @implements {FileBrowserResolver}
 */
export class TargetDirectoryResolver implements FileBrowserResolver {
    public directoryDetailsUri(directory: DirectoryDescriptor): string {
        return ApiConstants.SEEDBOX_TARGETS_API + '/' + directory.id + '/files';
    }

    public directoryCreationUri(): string {
        return ApiConstants.FILEMANAGER_DIRECTORY_API;
    }

    public fileDeletionUri(file: FileDescriptor): string {
        throw new Error('not implemented');
    }
}
