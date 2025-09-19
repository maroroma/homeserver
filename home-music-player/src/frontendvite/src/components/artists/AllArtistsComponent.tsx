import {type FC, useState} from "react";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import type {Artist} from "../../api/model/library/Artist";
import {useCustomNavigate, useLoadingEffect} from "../hooks/CustomHooks";
import MenuComponent from "../menu/MenuComponent";
import Paths from "../../tools/routes/Paths";
import FadeInPage from "../FadeInPage";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import {Comparators} from "../../tools/Comparators";
import MenuItemAddArtistComponent from "../menu/MenuItemAddArtistComponent";

const AllArtistsComponent: FC = () => {
  const navigate = useCustomNavigate();

  const [allArtists, setAllArtists] = useState<Artist[]>([]);

  useLoadingEffect("Artistes en cours de chargement", () =>
    LibraryRequester.getAllArtists().then((result) =>
      setAllArtists(result.sort(Comparators.byArtistsName()))
    )
  );

  return (
    <FadeInPage>
      {allArtists.map((anArtist) => (
        <LibraryListItemRenderer
          key={anArtist.id}
          label={anArtist.name}
          libraryItemArts={anArtist.libraryItemArts}
          onClick={() => navigate(Paths.ONE_ARTIST.resolve(anArtist.id))}
        />
      ))}
      <MenuComponent>
        <MenuItemAddArtistComponent onClick={() => navigate(Paths.ADD_ARTIST.resolve())}/>
      </MenuComponent>
    </FadeInPage>
  );
};

export default AllArtistsComponent;
