import { type FC } from "react";
import { useAllPlayLists, useCustomNavigate } from "../hooks/CustomHooks";
import FadeInPage from "../FadeInPage";
import { Bookmark, Bookmarks } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";

import ListItemRenderer from "../renderers/ListItemRenderer";
import IconListItemRenderer from "../renderers/IconListItemRenderer";

const AllPlayListComponent: FC = () => {
    const navigate = useCustomNavigate();

    const { allPlayLists } = useAllPlayLists();

    return (
        <FadeInPage
            label={`Custom PlayLists`}
            icon={<Bookmarks />}
            onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
        >
            {allPlayLists.map((anArtist) => (
                <IconListItemRenderer
                    size="xsmall"
                    icon={<Bookmark />}
                    key={anArtist.playListId}
                    label={anArtist.name}
                    onClick={() => navigate(Paths.ONE_PLAYLIST.resolve(anArtist.playListId))}
                />
            ))}

            {allPlayLists.length === 0 ? <strong>Aucune PlayLists !</strong> : <></>}

            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
                />
            </MenuComponent>
        </FadeInPage >

    );

}

export default AllPlayListComponent;