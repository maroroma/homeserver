import { useState, type FC } from "react";
import { useCustomNavigate } from "../hooks/CustomHooks";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import FadeInPage from "../FadeInPage";
import { List, Shuffle } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import { NameTransformer } from "../../tools/NameTransformer";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import EqualizerComponent from "../thumb/EqualizerComponent";
import { SelectSpecificTrackAction } from "../../state/actions/SelectSpecificTrackAction";
import MenuItemComponent from "../menu/MenuItemComponent";
import { ShuffleTrackListAction } from "../../state/actions/ShuffleTrackListAction";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import { Stack } from "react-bootstrap";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";
import MenuItemSwitchDownloadComponent from "../menu/MenuItemSwitchDownloadComponent";

const CurrentTrackListComponent: FC = () => {
    const navigate = useCustomNavigate();
    const { dispatch, embeddedPlayerState } = useMusicPlayerContext();

    const [downloadMode, setDownloadMode] = useState(false);

    return (
        <FadeInPage
            label={`Playlist en cours (${embeddedPlayerState.currentIndex + 1} / ${embeddedPlayerState.trackList.tracks.length})`}
            icon={<List />}
            onClick={() => navigate(Paths.PLAYER.resolve())}
        >
            {embeddedPlayerState.trackList.tracks.map((aTrack, index) => (
                <Stack direction="horizontal">
                    {
                        embeddedPlayerState.currentIndex === index
                            ?
                            <EqualizerComponent />
                            :
                            <></>

                    }
                    <LibraryListItemRenderer
                        label={NameTransformer.trackName(aTrack)}
                        key={aTrack.id}
                        enableDownload={downloadMode}
                        downloadLink={EmbeddedPlayerRequester.trackDownloadUrl(aTrack)}
                        downloadTitle={aTrack.shortFileName}
                        size="medium"
                        libraryItemArts={new LibraryItemArts(null, null, aTrack.albumId)}
                        onClick={() => {
                            dispatch(new SelectSpecificTrackAction(aTrack))
                        }}
                    />
                </Stack>
            ))}
            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.PLAYER.resolve())}
                />
                <MenuItemComponent
                    icon={<Shuffle size={40} />}
                    onClick={() => dispatch(new ShuffleTrackListAction())}
                />
                <MenuItemSwitchDownloadComponent on={downloadMode} onClick={() => setDownloadMode(!downloadMode)}/>

            </MenuComponent>
        </FadeInPage>
    );
}


export default CurrentTrackListComponent;