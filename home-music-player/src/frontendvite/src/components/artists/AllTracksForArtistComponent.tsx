import {type FC, useState} from "react";
import Paths from "../../tools/routes/Paths";
import {useArtist, useCustomNavigate, useLoadingEffect,} from "../hooks/CustomHooks";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import type {Track} from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import FanArtComponent from "../fanart/FanArtComponent";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import {Play} from "react-bootstrap-icons";
import {NameTransformer} from "../../tools/NameTransformer";
import {Comparators} from "../../tools/Comparators";
import MenuItemAddToPlayListComponent from "../menu/MenuItemAddToPlayListComponent";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ToastAction} from "../../state/actions/ToastAction";
import {PlayerRequester} from "../../api/requesters/PlayerRequester";

const AllTracksForArtistComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { dispatch } = useMusicPlayerContext();
  const { artist } = useArtist();

  const [allTracks, setAllTracks] = useState<Track[]>([]);

  useLoadingEffect(
    "Morceaux en cours de chargement",
    async () => {
      const results = await LibraryRequester.getAllTracksForArtist(artist);
      return setAllTracks(results.sort(Comparators.byTrackName()));
    },
    [artist]
  );

  const addAllTracksToPlayList = () => {
    dispatch(ToastAction.autoHide("Morceaux en cours d'ajout à la playlist"));
    PlayerRequester.addAllTracksFromArtistToPlayList(artist).then(() => {}
      // dispatch(ToastAction.close())
    );
  };

  return (
    <FadeInPage
      label={artist.name}
      libraryItemArts={artist.libraryItemArts}
      onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
    >
      {allTracks.map((aTrack) => (
        <IconListItemRenderer
          size="xsmall"
          icon={<Play />}
          label={NameTransformer.trackName(aTrack)}
          key={aTrack.id}
          onClick={() => {
            dispatch(ToastAction.loadingTrack());
            PlayerRequester.startPlayerForArtist(artist, aTrack).then(() =>
              dispatch(ToastAction.close())
            );
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
      </MenuComponent>
    </FadeInPage>
  );
};

export default AllTracksForArtistComponent;
