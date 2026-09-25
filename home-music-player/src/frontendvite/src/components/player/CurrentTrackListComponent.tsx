import type { FC } from "react";
import { useCustomNavigate } from "../hooks/CustomHooks";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import FadeInPage from "../FadeInPage";
import { List, MusicNoteBeamed, Shuffle } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import { NameTransformer } from "../../tools/NameTransformer";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import EqualizerComponent from "../thumb/EqualizerComponent";
import { SelectSpecificTrackAction } from "../../state/actions/SelectSpecificTrackAction";
import MenuItemComponent from "../menu/MenuItemComponent";
import { ShuffleTrackListAction } from "../../state/actions/ShuffleTrackListAction";

const CurrentTrackListComponent: FC = () => {
    const navigate = useCustomNavigate();
    const { dispatch, embeddedPlayerState } = useMusicPlayerContext();

    return (
        <FadeInPage
            label="Playlist en cours"
            icon={<List />}
            onClick={() => navigate(Paths.PLAYER.resolve())}
        >
            {embeddedPlayerState.trackList.tracks.map((aTrack, index) => (
                <IconListItemRenderer
                    size="xsmall"
                    icon={embeddedPlayerState.currentIndex === index ? <EqualizerComponent /> : <MusicNoteBeamed />}
                    label={NameTransformer.trackName(aTrack)}
                    key={aTrack.id}
                    onClick={() => {
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

            </MenuComponent>
        </FadeInPage>
    );
}


export default CurrentTrackListComponent;