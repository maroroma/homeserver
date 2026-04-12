import { useEffect, useState, type FC } from "react";
import { useAllPlayLists, useCustomNavigate } from "../hooks/CustomHooks";
import FadeInPage from "../FadeInPage";
import { BookmarkPlus } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { PlayerStatusEvent } from "../../api/model/player/PlayerStatusEvent";
import { PlayerRequester } from "../../api/requesters/PlayerRequester";
import { Button, Form, InputGroup } from "react-bootstrap";

import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import { PlayListRequester } from "../../api/requesters/PlayListRequester";
import ListItemRenderer from "../renderers/ListItemRenderer";
import type { PlayList } from "../../api/model/playlists/PlayList";

const AddToPlayList: FC = () => {
    const navigate = useCustomNavigate();


    const [fullPlayerStatus, setFullPlayerStatus] = useState(PlayerStatusEvent.empty());

    const { allPlayLists } = useAllPlayLists();

    const [newPlayListName, setNewPlayListName] = useState("");
    const [validNewPlayListName, setValidNewPlayListName] = useState(false);


    useEffect(() => {
        setNewPlayListName("");
        PlayerRequester.getFullPlayerStatus()
            .then(fullPlayerStatus => setFullPlayerStatus(fullPlayerStatus));
    }, [])

    useEffect(() => {
        setValidNewPlayListName(newPlayListName !== "")
    }, [newPlayListName])


    const createAndAddToPlayList = () => {
        PlayListRequester.createNewPlayList(newPlayListName)
            .then(newPlayList => PlayListRequester.addTrackToPlayList(newPlayList.playListId, fullPlayerStatus.track.id))
            .then(() => navigate(Paths.PLAYER.resolve()))
    }

    const addToExistingPlayList = (existingPlayList: PlayList) => {
        PlayListRequester.addTrackToPlayList(existingPlayList.playListId, fullPlayerStatus.track.id)
            .then(() => navigate(Paths.PLAYER.resolve()))
    }

    return (
        <FadeInPage
            label={`Ajouter à une playlist`}
            icon={<BookmarkPlus />}
            onClick={() => navigate(Paths.PLAYER.resolve())}
        >
            <LibraryListItemRenderer
                label={`Choisir une playlist pour <${fullPlayerStatus.track.name}>`}
                libraryItemArts={fullPlayerStatus.album.libraryItemArts}
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