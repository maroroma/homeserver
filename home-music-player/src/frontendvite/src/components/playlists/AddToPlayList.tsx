import { useEffect, useState, type FC } from "react";
import { useAllPlayLists, useCustomNavigate } from "../hooks/CustomHooks";
import FadeInPage from "../FadeInPage";
import { BookmarkPlus } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { Button, Form, InputGroup } from "react-bootstrap";

import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import { PlayListRequester } from "../../api/requesters/PlayListRequester";
import ListItemRenderer from "../renderers/ListItemRenderer";
import type { PlayList } from "../../api/model/playlists/PlayList";
import { Track } from "../../api/model/library/Track";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import { useParams } from "react-router";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";

const AddToPlayList: FC = () => {
    const navigate = useCustomNavigate();

    const { trackIdToAdd } = useParams();

    const [track, setTrack] = useState(Track.empty());

    const { allPlayLists } = useAllPlayLists();

    const { artistId, albumId } = useParams();

    const [newPlayListName, setNewPlayListName] = useState("");
    const [validNewPlayListName, setValidNewPlayListName] = useState(false);

    useEffect(() => {
        setNewPlayListName("");
        if (trackIdToAdd) {
            LibraryRequester.getTrack(trackIdToAdd)
                .then(response => setTrack(response));
        }

    }, [trackIdToAdd])

    useEffect(() => {
        setValidNewPlayListName(newPlayListName !== "")
    }, [newPlayListName])


    const createAndAddToPlayList = () => {
        PlayListRequester.createNewPlayList(newPlayListName)
            .then(newPlayList => PlayListRequester.addTrackToPlayList(newPlayList.playListId, track.id))
            .then(() => gotoWhenFinish())
    }

    const addToExistingPlayList = (existingPlayList: PlayList) => {
        PlayListRequester.addTrackToPlayList(existingPlayList.playListId, track.id)
            .then(() => gotoWhenFinish())
    }

    const gotoWhenFinish = () => {
        if (artistId && albumId) {
            navigate(Paths.ONE_ALBUM.resolve([artistId, albumId]))
        } else {
            navigate(Paths.PLAYER.resolve())
        }
    }

    return (
        <FadeInPage
            label={`Ajouter à une playlist`}
            icon={<BookmarkPlus />}
            onClick={() => navigate(Paths.PLAYER.resolve())}
        >

            {Paths.ONE_ALBUM.resolve([artistId, albumId])}



            <LibraryListItemRenderer
                label={`Choisir une playlist pour <${track.name}>`}
                libraryItemArts={new LibraryItemArts(null, null, track.albumId)}
            />

            {allPlayLists.map((playList) => (
                <ListItemRenderer
                    key={playList.playListId}
                    label={playList.name}
                    onClick={() => addToExistingPlayList(playList)}
                />
            ))}

            <InputGroup className="mb-3">
                <Form.Control
                    placeholder="Nouvelle PlayList"
                    value={newPlayListName}
                    onChange={(event) => setNewPlayListName(event.target.value)}
                    isValid={validNewPlayListName}
                />
                <Button
                    variant="outline-secondary"
                    id="button-addon2"
                    disabled={!validNewPlayListName}
                    onClick={() => createAndAddToPlayList()}
                >
                    Créer et Ajouter
                </Button>
            </InputGroup>

            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.PLAYER.resolve())}
                />
            </MenuComponent>
        </FadeInPage >

    );

}

export default AddToPlayList;