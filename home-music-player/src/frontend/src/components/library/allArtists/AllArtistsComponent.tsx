import {FC, useEffect} from "react";
import "./AllArtistsComponent.css";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import AllArtistThumbComponent from "./AllArtistThumbComponent";
import AllArtistsMenuComponent from "./AllArtistsMenuComponent";
import {UpdateAllArtistsAction} from "../../../state/actions/UpdateAllArtistsAction";


const AllArtistsComponent: FC = () => {

    const { artists, allArtistsSubState, dispatch } = useMusicPlayerContext();

    useEffect(() => {

        if (artists?.length === 0) {

            // conditionne le démarrage de l'appli
            LibraryRequester.getAllArtists()
                .then(allArtists => {
                    // si pas d'artiste, on affiche la popup de sélection d'artistes à ajouter
                    if (allArtists.length === 0) {
                        LibraryRequester.getArtistCandidatesAndDispatch(dispatch);
                    } else {
                        // sinon on lance un chargement et on redirige vers cette page
                        dispatch(new UpdateAllArtistsAction(allArtists));
                    }
                })

        }

    }, [artists, dispatch]);


    return <>
        <AllArtistsMenuComponent></AllArtistsMenuComponent>
        <div className="artists-grid">
            {allArtistsSubState.displayedArtists.map((anArtist, key) => <AllArtistThumbComponent key={key} artist={anArtist}></AllArtistThumbComponent>)}
        </div>
    </>
}

export default AllArtistsComponent;
