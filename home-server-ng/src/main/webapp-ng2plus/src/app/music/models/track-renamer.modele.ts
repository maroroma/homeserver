import { TrackDescriptor } from './track-descriptor.modele';
export class TrackRenamer {


    public static renameFileFromTag(trackDescriptor: TrackDescriptor): TrackDescriptor {
        trackDescriptor.newFileName = TrackRenamer.pad(trackDescriptor.trackNumber, 2) + ' - ' + trackDescriptor.trackName + '.mp3';
        return trackDescriptor;
    }

    private static pad(n, width, z?): string {
        z = z || '0';
        n = n + '';
        return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
    }
}