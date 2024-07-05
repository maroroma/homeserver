import {FC} from "react";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import DescriptionPanelComponent from "../../common/panels/DescriptionPanelComponent";
import ContentPanelComponents from "../../common/panels/ContentPanelComponents";

import HeaderMenuComponent from "../../common/menu/HeaderMenuComponent";
import MenuBackButton from "../../common/menu/MenuBackButton";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import MenuPlayAllButton from "../../common/menu/MenuPlayAllButton";
import FanartPanelComponent from "../../common/panels/FanartPanelComponent";
import MenuSearchField from "../../common/menu/MenuSearchField";
import {SwitchSearchBarAllTracksForArtistAction} from "../../../state/actions/allTracksActions/SwitchSearchBarAllTracksForArtistAction";
import TrackItemRenderer from "../../common/tracks/TrackItemRenderer";
import {UpdateSearchTracksForArtistAction} from "../../../state/actions/allTracksActions/UpdateSearchTracksForArtistAction";
import {PlayerRequester} from "../../../api/requesters/PlayerRequester";
import {Track} from "../../../api/model/library/Track";
import {DisplayToastAction} from "../../../state/actions/toastActions/DisplayToastAction";


const AllTracksForArtistComponent: FC = () => {


    const { allTracksForArtistSubState, artistViewState, dispatch } = useMusicPlayerContext();



    const startPlayer = (track: Track) => {
        if (artistViewState.selectedArtist) {
            PlayerRequester.startPlayerForArtist(artistViewState.selectedArtist, track)
            .then(response => dispatch(DisplayToastAction.info("Lecture démarrée")))
        }
    }

    const addToPlayList = () => {
        if (artistViewState.selectedArtist) {
            PlayerRequester.addAllTracksFromArtistToPlayList(artistViewState.selectedArtist)
            .then(response => dispatch(DisplayToastAction.info("Morceaux ajoutés")))

        }
    }

    return <div>
        <HeaderMenuComponent>
            <MenuBackButton onClick={() => dispatch(SimpleViewChangeAction.of(ViewState.Artist))} />
            <MenuPlayAllButton onClick={() => { addToPlayList() }} />
            <MenuSearchField displaySearchField={allTracksForArtistSubState.displaySearchBar}
                placeHolder="Rechercher un morceau"
                onSwitch={() => { dispatch(new SwitchSearchBarAllTracksForArtistAction()) }}
                onChange={(searchText) => { dispatch(new UpdateSearchTracksForArtistAction(searchText)) }}
                searchText={allTracksForArtistSubState.searchText}
            ></MenuSearchField>
        </HeaderMenuComponent>
        <DescriptionPanelComponent libraryItemArts={artistViewState.selectedArtist?.libraryItemArts}>
            <h1>{artistViewState.selectedArtist?.name.toUpperCase()}</h1>
            <h3>{`${allTracksForArtistSubState.rawTracks.length} tracks`}</h3>
        </DescriptionPanelComponent>

        <ContentPanelComponents>
            {allTracksForArtistSubState.tracksToDisplay.map((aTrack, trackIndex) =>
                <TrackItemRenderer trackToDisplay={aTrack} key={`track-${trackIndex}`} onClick={() => startPlayer(aTrack)}/>
            )}
        </ContentPanelComponents>
        <FanartPanelComponent fanart={artistViewState.selectedArtist?.libraryItemArts} />

    </div>
}

export default AllTracksForArtistComponent;
