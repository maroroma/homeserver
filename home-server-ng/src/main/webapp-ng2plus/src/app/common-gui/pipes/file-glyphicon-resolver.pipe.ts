import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { Pipe, PipeTransform } from '@angular/core';

/**
 * Permet de retourner un glyphicon en fonction du type de fichier.
 * @export
 * @class FileGlyphiconResolverPipe
 * @implements {PipeTransform}
 */
@Pipe({
    name: 'fileGlyphiconResolver'
})
export class FileGlyphiconResolverPipe implements PipeTransform {
    transform(value: FileDescriptor | string): string {
        const returnValue = 'glyphicon glyphicon-';
        if (value instanceof FileDescriptor) {
            const file = value as FileDescriptor;
            if (FileDescriptor.isVideoFile(value)) {
                return returnValue + 'film';
            }
            if (FileDescriptor.isMusicFile(value)) {
                return returnValue + 'music';
            }
            if (FileDescriptor.isCommonFile(value)) {
                return returnValue + 'file';
            }
            if (FileDescriptor.isSubtitleFile(value)) {
                return returnValue + 'text-background';
            }
            if (FileDescriptor.isImageFile(value)) {
                return returnValue + 'picture';
            }


            return returnValue + 'question-sign';
        } else {
            return returnValue + value;
        }
    }
}
