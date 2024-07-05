import {SimpleFile} from "../api/model/files/SimpleFile";
import {Album} from "../api/model/library/Album";
import {Artist} from "../api/model/library/Artist";
import {Track} from "../api/model/library/Track";

export class Sorts {

    public static sortBySimpleFileName = (): (a1: SimpleFile, a2: SimpleFile) => number => {

        return (simpleFile1: SimpleFile, simpleFile2: SimpleFile) => {
            return simpleFile1.name.toLocaleLowerCase().localeCompare(simpleFile2.name.toLocaleLowerCase());
        };
    }

    public static sortByArtistName = (): (a1: Artist, a2: Artist) => number => {

        return (artist1: Artist, artist2: Artist) => {
            return artist1.name.toLocaleLowerCase().localeCompare(artist2.name.toLocaleLowerCase());
        };
    }

    public static sortByAlbumName = (): (a1: Album, a2: Album) => number => {

        return (album1: Album, album2: Album) => {
            return album1.name.toLocaleLowerCase().localeCompare(album2.name.toLocaleLowerCase());
        };
    }

    public static sortByTrackName = (): (a1: Track, a2: Track) => number => {

        return (track1: Track, track2: Track) => {


   



            var resolvedTrack1 = `${track1.trackNumber?.padStart(2, "0")} - ${track1.name}`;
            var resolvedTrack2 = `${track2.trackNumber?.padStart(2, "0")} - ${track2.name}`;


            return resolvedTrack1.toLocaleLowerCase().localeCompare(resolvedTrack2.toLocaleLowerCase());
        };
    }
}