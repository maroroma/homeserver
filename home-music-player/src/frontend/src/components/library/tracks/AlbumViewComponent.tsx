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
import MenuDeleteButton from "../../common/menu/MenuDeleteButton";
import TrackItemRenderer from "../../common/tracks/TrackItemRenderer";
import {PlayerRequester} from "../../../api/requesters/PlayerRequester";
import {Track} from "../../../api/model/library/Track";
import {DisplayToastAction} from "../../../state/actions/toastActions/DisplayToastAction";


const AlbumViewComponent: FC = () => {


    const { albumWithTracksSubState, artistViewState, dispatch } = useMusicPlayerContext();

    const askForDeletion = () => {
        dispatch(SimpleViewChangeAction.of(ViewState.DeleteAlbum));
    }

    const startPlayer = (track: Track) => {

        if (albumWithTracksSubState.album) {
            PlayerRequester.startPlayer(albumWithTracksSubState.album, track)
            // .then(response => dispatch(DisplayToastAction.info("Lecture en cours de démarrage")))
            .catch(error => dispatch(DisplayToastAction.error("Erreur rencontrée lors du lancement de la lecture")))
        }
    }

    const addAlbumToPlayList = () => {
        if (albumWithTracksSubState.album) {
            PlayerRequester.addAlbumToPlayList(albumWithTracksSubState.album)
            .then(response => dispatch(DisplayToastAction.info(`${albumWithTracksSubState.album?.name} ajouté à la playlist`)))
        }
    }



    return <div>
        <HeaderMenuComponent>
            <MenuBackButton onClick={() => dispatch(SimpleViewChangeAction.of(ViewState.Artist))} />
            <MenuPlayAllButton onClick={() => addAlbumToPlayList()} disabled={albumWithTracksSubState.tracksToDisplay.length === 0}/>
            <MenuDeleteButton onClick={() => askForDeletion()} />
        </HeaderMenuComponent>
        <DescriptionPanelComponent libraryItemArts={albumWithTracksSubState.album?.libraryItemArts}>
            <h1>{albumWithTracksSubState.album?.name.toUpperCase()}</h1>
            <h4>({artistViewState.selectedArtist?.name})</h4>
            <h3>{`${albumWithTracksSubState.tracksToDisplay.length} morceaux`}</h3>
        </DescriptionPanelComponent>
        <ContentPanelComponents>
            {albumWithTracksSubState.tracksToDisplay.map((aTrack, trackIndex) =>
                <TrackItemRenderer key={`track-${trackIndex}`} trackToDisplay={aTrack} onClick={(track => startPlayer(track))} />
            )}
        </ContentPanelComponents>
        <FanartPanelComponent fanart={artistViewState.selectedArtist?.libraryItemArts} />

    </div>
}

export default AlbumViewComponent;
