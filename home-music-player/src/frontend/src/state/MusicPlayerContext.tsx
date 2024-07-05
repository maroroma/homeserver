import React, {createContext, FC, useContext, useReducer} from "react";
import {MusicPlayerState} from "./MusicPlayerState";
import {MusicPlayerContextAction} from "./actions/MusicPlayerContextActions";
import {ViewState} from "./ViewState";

const initialState: MusicPlayerState = {
    viewState: ViewState.AllArtists,
    helloWorld: "test",
    artists: [],
    dispatch: () => { },
    allArtistsSubState: {
        displaySearchBar: false,
        searchText: "",
        displayedArtists: []
    },
    addArtistState: {
        candidates: []
    },
    toastState: {
        messages: []
    },
    artistViewState: {
        selectedArtist: undefined,
        albums: []
    },
    albumWithTracksSubState: {
        tracksToDisplay: [],
        album: undefined
    },
    addAlbumSubState: {
        candidates: []
    },
    allTracksForArtistSubState: {
        tracksToDisplay: [],
        rawTracks: [],
        searchText: "",
        displaySearchBar: false
    },
    playerSubState: {
        display: "none"
    }
}

const reducer = (previousState: MusicPlayerState, action: MusicPlayerContextAction) => {
    return action.applyToState(previousState);
}

const MusicPlayerContext = createContext<MusicPlayerState>(initialState);

type MusicPlayerProviderProps = {
    children: any
}

const MusicPlayerProvider: FC<MusicPlayerProviderProps> = (props) => {
    const [state, dispatch] = useReducer(reducer, initialState);




    return <MusicPlayerContext.Provider value={{ ...state, dispatch }}>
        {props.children}
    </MusicPlayerContext.Provider>
}

const useMusicPlayerContext: () => MusicPlayerState = () => useContext(MusicPlayerContext);

export {
    MusicPlayerProvider,
    useMusicPlayerContext
}