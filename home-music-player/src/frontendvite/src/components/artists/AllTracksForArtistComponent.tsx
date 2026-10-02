import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useArtist, useCustomNavigate, useLoadingEffect, } from "../hooks/CustomHooks";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import type { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import FanArtComponent from "../fanart/FanArtComponent";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { Comparators } from "../../tools/Comparators";
import MenuItemAddToPlayListComponent from "../menu/MenuItemAddToPlayListComponent";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";
import { LoadNewTrackListAction } from "../../state/actions/LoadNewTrackListAction";
import { AddTracksToTrackListAction } from "../../state/actions/AddTracksToTrackListAction";
import MenuItemSwitchDownloadComponent from "../menu/MenuItemSwitchDownloadComponent";
import type { TrackDisplayMode } from "../renderers/TrackListItemRenderer";
import TrackListItemRenderer from "../renderers/TrackListItemRenderer";

const AllTracksForArtistComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { dispatch } = useMusicPlayerContext();
  const { artist } = useArtist();

  const [allTracks, setAllTracks] = useState<Track[]>([]);
  const [trackDisplayMode, setTrackDisplayMode] = useState<TrackDisplayMode>("Play");



  useLoadingEffect(
    "Morceaux en cours de chargement",
    async () => {
      if (artist && artist.id) {
        const results = await LibraryRequester.getAllTracksForArtist(artist);
        return setAllTracks(results.sort(Comparators.byTrackName()));
      }
    },
    [artist]
  );

  const addAllTracksToPlayList = () => {
    EmbeddedPlayerRequester.generareTrackListFromArtist(artist)
      .then(response => dispatch(new AddTracksToTrackListAction(response)));
  };

  return (
    <FadeInPage
      label={artist.name}
      libraryItemArts={artist.libraryItemArts}
      onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
    >
      {allTracks.map((aTrack) => (
        <TrackListItemRenderer
          size="xsmall"
          track={aTrack}
          key={aTrack.id}
          displayMode={trackDisplayMode}
          onPlay={() => {
            EmbeddedPlayerRequester.generareTrackListFromArtist(artist)
              .then(response => dispatch(new LoadNewTrackListAction(response, aTrack.id)))
          }}
        />
      ))}
      <FanArtComponent fanart={artist.libraryItemArts} />
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
        />
        <MenuItemAddToPlayListComponent
          onClick={() => addAllTracksToPlayList()}
        />
        <MenuItemSwitchDownloadComponent
          on={trackDisplayMode === "Download"}
          onClick={() => {
            trackDisplayMode === "Download" ? setTrackDisplayMode("Play") : setTrackDisplayMode("Download")
          }}
        />
      </MenuComponent>
    </FadeInPage>
  );
};

export default AllTracksForArtistComponent;
