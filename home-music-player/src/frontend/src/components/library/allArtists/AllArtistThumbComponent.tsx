import {FC} from "react";
import {Artist} from "../../../api/model/library/Artist";
import {Card} from "react-bootstrap";

import "./AllArtistThumbComponent.css";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {SelectArtistAction} from "../../../state/actions/artistViewActions/SelectArtistAction";

export type AllArtistThumbComponentProps = {
    artist: Artist
}


const AllArtistThumbComponent: FC<AllArtistThumbComponentProps> = ({artist}) => {


    const { dispatch } = useMusicPlayerContext();

    return <div className="clickable" onClick={() => dispatch(new SelectArtistAction(artist))}>
        <Card border="light" bg="dark" text="light" className="text-center artist-thumb">
            <Card.Img variant="top" src={`musicplayer/localresources/thumbs/${artist.libraryItemArts.thumbPath}`} className="artist-thumb-image" />
            <Card.Body>
                <Card.Text className="artist-thumb-name">
                    {artist.name.toUpperCase()}
                </Card.Text>
            </Card.Body>
        </Card>
    </div>

}

export default AllArtistThumbComponent;
