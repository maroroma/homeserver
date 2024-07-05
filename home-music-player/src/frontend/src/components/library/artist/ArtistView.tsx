import {FC, useEffect} from "react";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import HeaderMenuComponent from "../../common/menu/HeaderMenuComponent";
import MenuPlusButton from "../../common/menu/MenuPlusButton";
import MenuBackButton from "../../common/menu/MenuBackButton";
import MenuDeleteButton from "../../common/menu/MenuDeleteButton";

import "./ArtistView.css"
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import {LoadAlbumsForArtist} from "../../../state/actions/artistViewActions/LoadAlbumsForArtist";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import ThumbImage from "../../common/ThumbImage";
import MenuEditButton from "../../common/menu/MenuEditButton";
import AllFilesThumb from "../../common/AllFilesThumb";
import {Album} from "../../../api/model/library/Album";
import {DisplayAlbumAction} from "../../../state/actions/allTracksActions/DisplayAlbumAction";
import DescriptionPanelComponent from "../../common/panels/DescriptionPanelComponent";
import ContentPanelComponents from "../../common/panels/ContentPanelComponents";
import FanartPanelComponent from "../../common/panels/FanartPanelComponent";
import {DisplayAlbumCandidatesAction} from "../../../state/actions/addAlbumActions/DisplayAlbumCandidatesAction";
import {DisplayAllTracksForArtistAction} from "../../../state/actions/allTracksActions/DisplayAllTracksForArtistAction";
import {GoBackToAllArtistsAction} from "../../../state/actions/allArtistsActions/GoBackToAllArtistsAction";
import {DisplayToastAction} from "../../../state/actions/toastActions/DisplayToastAction";

const ArtistView: FC = () => {

    const { artistViewState, dispatch } = useMusicPlayerContext();

    useEffect(() => {
        if (artistViewState.selectedArtist) {
            LibraryRequester.getAlbumsForArtist(artistViewState.selectedArtist)
                .then(albums => dispatch(new LoadAlbumsForArtist(albums)))
                .catch(error => dispatch(DisplayToastAction.error("erreur rencontrées lors de la récupération des albums")))

        }

    }, [dispatch, artistViewState.selectedArtist]);


    const loadTracksForAlbum = (album: Album) => {
        LibraryRequester.getTracksForAlbum(album)
            .then(tracks => dispatch(new DisplayAlbumAction(album, tracks)));
    }

    const loadAllTracksForArtist = () => {
        if (artistViewState.selectedArtist) {
            LibraryRequester.getAllTracksForArtist(artistViewState.selectedArtist)
                .then(allTracks => dispatch(new DisplayAllTracksForArtistAction(allTracks)))
                .catch(error => dispatch(DisplayToastAction.error("erreur rencontrées lors de la récupération des morceaux")))
        }
    }

    const addAlbum = () => {
        if (artistViewState.selectedArtist) {
            LibraryRequester.getAlbumCandidates(artistViewState.selectedArtist)
                .then(albumCandidates => dispatch(new DisplayAlbumCandidatesAction(albumCandidates)))
                .catch(error => dispatch(DisplayToastAction.error("erreur rencontrées lors de la récupération des albums à ajouter")));

        }
    }


    return <div>
        <HeaderMenuComponent>
            <MenuBackButton onClick={() => dispatch(new GoBackToAllArtistsAction())}></MenuBackButton>
            <MenuPlusButton onClick={() => { addAlbum() }}></MenuPlusButton>
            <MenuEditButton onClick={() => { dispatch(SimpleViewChangeAction.of(ViewState.EditArtist)) }}></MenuEditButton>
            <MenuDeleteButton onClick={() => { dispatch(SimpleViewChangeAction.of(ViewState.DeleteArtist)) }}></MenuDeleteButton>
        </HeaderMenuComponent>

        <DescriptionPanelComponent libraryItemArts={artistViewState.selectedArtist?.libraryItemArts}>
            <h1>{artistViewState.selectedArtist?.name.toUpperCase()}</h1>
            <h3 className="hide-on-small-devices">{`${artistViewState.selectedArtist?.albums.length} albums`}</h3>
        </DescriptionPanelComponent>

        <ContentPanelComponents>
            <div className="artist-album" onClick={() => loadAllTracksForArtist()}>
                <AllFilesThumb className="artist-album-thumb" />
                <span className="artist-album-name">Toutes Les Musiques</span>
            </div>


            {artistViewState.albums.map((anAlbum, albumIndex) =>
                <div className="artist-album" key={`album-index${albumIndex}`} onClick={() => loadTracksForAlbum(anAlbum)}>
                    <ThumbImage rounded={true} libraryItemArts={anAlbum.libraryItemArts} className="artist-album-thumb" />
                    <span className="artist-album-name">{anAlbum.name}</span>
                </div>
            )}
        </ContentPanelComponents>

        <FanartPanelComponent fanart={artistViewState.selectedArtist?.libraryItemArts} />
    </div>
}

export default ArtistView;