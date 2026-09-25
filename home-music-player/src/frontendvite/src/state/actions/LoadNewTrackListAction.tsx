import { TrackList } from "../../api/model/embedded/TrackList";
import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class LoadNewTrackListAction implements MusicPlayerContextAction {

    constructor(private newTrackList: TrackList, private preselectedTrackId: string) {

    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const sortedTrackList = TrackList.byTrackName(this.newTrackList.tracks);
        const trackIndex = sortedTrackList.findIndexOfTrack(this.preselectedTrackId);
        const currentTrack = sortedTrackList.tracks[trackIndex];

        return {
            ...previousState,
            embeddedPlayerState: {
                ...previousState.embeddedPlayerState,
                trackList: sortedTrackList,
                currentIndex: trackIndex
            },
            playerStatus: {
                ...previousState.playerStatus,
                playerStatus: "PLAYING",
                track: currentTrack
            }
        }
    }

}