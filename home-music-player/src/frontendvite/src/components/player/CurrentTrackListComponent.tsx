import { useState, type FC } from "react";
import { useCustomNavigate } from "../hooks/CustomHooks";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import FadeInPage from "../FadeInPage";
import { List, Shuffle } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { SelectSpecificTrackAction } from "../../state/actions/SelectSpecificTrackAction";
import MenuItemComponent from "../menu/MenuItemComponent";
import { ShuffleTrackListAction } from "../../state/actions/ShuffleTrackListAction";
import MenuItemSwitchDownloadComponent from "../menu/MenuItemSwitchDownloadComponent";
import TrackListItemRenderer, { type TrackDisplayMode } from "../renderers/TrackListItemRenderer";

const CurrentTrackListComponent: FC = () => {
    const navigate = useCustomNavigate();
    const { dispatch, embeddedPlayerState } = useMusicPlayerContext();


    const [trackDisplayMode, setTrackDisplayMode] = useState<TrackDisplayMode>("LibraryItemArts");


    return (
        <FadeInPage
            label={`Playlist en cours (${embeddedPlayerState.currentIndex + 1} / ${embeddedPlayerState.trackList.tracks.length})`}
            icon={<List />}
            onClick={() => navigate(Paths.PLAYER.resolve())}
        >
            {embeddedPlayerState.trackList.tracks.map((aTrack) => (
                <TrackListItemRenderer
                    track={aTrack}
                    key={aTrack.id}
                    size="xsmall"
                    displayMode={trackDisplayMode}
                    onPlay={() => {
                        dispatch(new SelectSpecificTrackAction(aTrack))
                    }}
                />
            ))}
            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.PLAYER.resolve())}
                />
                <MenuItemComponent
                    icon={<Shuffle size={40} />}
                    onClick={() => dispatch(new ShuffleTrackListAction())}
                />
                <MenuItemSwitchDownloadComponent on={trackDisplayMode === "Download"} onClick={() => {
                    trackDisplayMode === "Download" ? setTrackDisplayMode("LibraryItemArts") : setTrackDisplayMode("Download")
                }} />

            </MenuComponent>
        </FadeInPage>
    );
}


export default CurrentTrackListComponent;