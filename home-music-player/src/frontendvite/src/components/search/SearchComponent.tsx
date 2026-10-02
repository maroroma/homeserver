import { useEffect, useState, type FC } from "react";
import { useCustomNavigate, useDebounce } from "../hooks/CustomHooks";
import FadeInPage from "../FadeInPage";
import { Disc, MusicNoteBeamed, PeopleFill, Search } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";

import { Accordion, Form, InputGroup, Stack } from "react-bootstrap";

import "./SearchComponent.css"
import ThumbIconComponent from "../thumb/ThumbIconComponent";
import { SearchResponse } from "../../api/model/search/SearchResponse";
import { SearchRequester } from "../../api/requesters/SearchRequester";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { LoadNewTrackListAction } from "../../state/actions/LoadNewTrackListAction";
import { Comparators } from "../../tools/Comparators";
import MenuItemSwitchPlayListComponent from "../menu/MenuItemSwitchPlayListComponent";
import MenuItemSwitchDownloadComponent from "../menu/MenuItemSwitchDownloadComponent";
import type { TrackDisplayMode } from "../renderers/TrackListItemRenderer";
import TrackListItemRenderer from "../renderers/TrackListItemRenderer";

const SearchComponent: FC = () => {
    const navigate = useCustomNavigate();
    const { dispatch } = useMusicPlayerContext();
    const [displayedSearchString, setDisplayedSearchString] = useState("");
    const debounceSearchString = useDebounce(displayedSearchString);
    const [searchResponse, setSearchResponse] = useState(SearchResponse.empty());
    const [trackDisplayMode, setTrackDisplayMode] = useState<TrackDisplayMode>("LibraryItemArts");


    useEffect(() => {
        SearchRequester.search(debounceSearchString)
            .then(response => setSearchResponse(response));
    }, [debounceSearchString])


    return (
        <FadeInPage
            label="search"
            customHeader={
                <Stack direction="horizontal">
                    <ThumbIconComponent
                        icon={<Search />}
                        size="small"
                        className="thumb-scroll-transitioning" />
                    <InputGroup>
                        <Form.Control
                            autoFocus
                            placeholder="search track name, artist or album"
                            size="lg"
                            value={displayedSearchString}
                            onChange={(event) => setDisplayedSearchString(event.target.value)}
                        />
                    </InputGroup>
                </Stack>
            }
            onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
        >
            <Accordion defaultActiveKey={['0']} alwaysOpen>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>
                        <IconListItemRenderer
                            icon={<MusicNoteBeamed />}
                            label={`${searchResponse.tracks.length} track(s)`}
                            size="xsmall"
                        />
                    </Accordion.Header>
                    <Accordion.Body>
                        {searchResponse.tracks.sort(Comparators.byTrackName()).map((aTrack) => (
                            <TrackListItemRenderer
                                track={aTrack}
                                key={aTrack.id}
                                size="xsmall"
                                displayMode={trackDisplayMode}
                                onPlay={() => {
                                    EmbeddedPlayerRequester.generareTrackListFromAlbumId(aTrack.albumId)
                                        .then(response => dispatch(new LoadNewTrackListAction(response, aTrack.id)));
                                }}
                                onAddToFavorite={() => {
                                    navigate(Paths.ADD_TO_PLAYLIST.resolve([aTrack.id]))
                                }}
                            />
                        ))}
                    </Accordion.Body>
                </Accordion.Item>
                <Accordion.Item eventKey="1">
                    <Accordion.Header>
                        <IconListItemRenderer
                            icon={<PeopleFill />}
                            label={`${searchResponse.artists.length} artist(s)`}
                            size="xsmall"
                        /></Accordion.Header>
                    <Accordion.Body>
                        {searchResponse.artists.sort(Comparators.byArtistsName()).map((anArtist) => (
                            <LibraryListItemRenderer
                                key={anArtist.id}
                                label={anArtist.name}
                                size="xsmall"
                                libraryItemArts={anArtist.libraryItemArts}
                                onClick={() => navigate(Paths.ONE_ARTIST.resolve(anArtist.id))}
                            />
                        ))}
                    </Accordion.Body>
                </Accordion.Item>
                <Accordion.Item eventKey="2">
                    <Accordion.Header>
                        <IconListItemRenderer
                            icon={<Disc />}
                            label={`${searchResponse.albums.length} album(s)`}
                            size="xsmall"
                        /></Accordion.Header>
                    <Accordion.Body>
                        {searchResponse.albums.sort(Comparators.by(album => album.name)).map((anAlbum) => (
                            <LibraryListItemRenderer
                                label={anAlbum.name}
                                libraryItemArts={anAlbum.libraryItemArts}
                                key={anAlbum.id}
                                size="xsmall"
                                onClick={() =>
                                    navigate(Paths.ONE_ALBUM.resolve([anAlbum.artistId, anAlbum.id]))
                                }
                            />
                        ))}
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>

            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
                />
                <MenuItemSwitchPlayListComponent
                    on={trackDisplayMode === "Favorite"}
                    onClick={() => {
                        trackDisplayMode === "Favorite" ? setTrackDisplayMode("LibraryItemArts") : setTrackDisplayMode("Favorite");
                    }}
                />

                <MenuItemSwitchDownloadComponent
                    on={trackDisplayMode === "Download"}
                    onClick={() => {
                        trackDisplayMode === "Download" ? setTrackDisplayMode("LibraryItemArts") : setTrackDisplayMode("Download");
                    }}
                />
            </MenuComponent>
        </FadeInPage >

    );

}

export default SearchComponent;