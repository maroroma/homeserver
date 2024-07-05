import {Artist} from "../api/model/library/Artist";
import {AddAlbumSubState} from "./AddAlbumSubState";
import {AddArtistSubState} from "./AddArtistSubState";
import {AllArtistsSubState} from "./AllArtistsSubState";
import {AlbumWithTracksSubState} from "./AlbumWithTracksSubState";
import {ArtistViewSubState} from "./ArtistViewSubState";
import {ToastState} from "./ToastState";
import {ViewState} from "./ViewState";
import {MusicPlayerContextAction} from "./actions/MusicPlayerContextActions";
import {AllTracksForArtistSubState} from "./AllTracksForArtistSubState";
import {PlayerSubState} from "./PlayerSubState";


export type MusicPlayerState = {
    helloWorld: string;
    artists: Artist[];
    dispatch: React.Dispatch<MusicPlayerContextAction>;
    allArtistsSubState: AllArtistsSubState,
    addArtistState: AddArtistSubState,
    toastState: ToastState,
    artistViewState: ArtistViewSubState,
    viewState: ViewState,
    albumWithTracksSubState: AlbumWithTracksSubState,
    addAlbumSubState: AddAlbumSubState,
    allTracksForArtistSubState: AllTracksForArtistSubState,
    playerSubState: PlayerSubState
}

