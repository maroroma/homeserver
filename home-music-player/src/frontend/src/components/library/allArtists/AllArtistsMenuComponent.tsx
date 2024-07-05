import {FC} from "react";

import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {SwitchDisplaySearchArtistsAction} from "../../../state/actions/allArtistsActions/SwitchDisplaySearchArtistsAction";
import UpdateSearchArtistAction from "../../../state/actions/allArtistsActions/UpdateSearchArtistAction";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import MenuPlusButton from "../../common/menu/MenuPlusButton";
import MenuReloadButton from "../../common/menu/MenuReloadButton";
import MenuSearchField from "../../common/menu/MenuSearchField";
import HeaderMenuComponent from "../../common/menu/HeaderMenuComponent";

const AllArtistsMenuComponent: FC = () => {

    const { allArtistsSubState, dispatch } = useMusicPlayerContext();


    const switchSearchField = () => {
        dispatch(new SwitchDisplaySearchArtistsAction());
    }

    const reloadArtists = () => {
        LibraryRequester.getAllArtistsAndDispatchUpdate(dispatch);
    }

    const addNewArtist = () => {
        LibraryRequester.getArtistCandidatesAndDispatch(dispatch);
    }



    return <HeaderMenuComponent>
        {/* <MenuHomeButton onClick={() => {}}></MenuHomeButton> */}
        <MenuPlusButton onClick={() => addNewArtist()}></MenuPlusButton>
        <MenuReloadButton onClick={() => reloadArtists()}></MenuReloadButton>
        <MenuSearchField
            placeHolder="Trouver un Artiste"
            displaySearchField={allArtistsSubState.displaySearchBar}
            onSwitch={() => switchSearchField()}
            searchText={allArtistsSubState.searchText}
            onChange={(newSearch) => dispatch(new UpdateSearchArtistAction(newSearch))}></MenuSearchField>
    </HeaderMenuComponent>
}

export default AllArtistsMenuComponent;