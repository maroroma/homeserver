import { MemoryStatus } from "../player/MemoryStatus";

export class ApplicationStats {

    public static empty(): ApplicationStats {
        return new ApplicationStats(
            MemoryStatus.empty(),
            0, 0, 0, 0, 0
        )
    }

    constructor(public memoryStatus: MemoryStatus,
        public nbArtists: number,
        public nbAlbums: number,
        public nbTracks: number,
        public nbLocalCacheItems: number,
        public nbMaxCacheItems: number) { }
}