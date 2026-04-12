import { useEffect, useState, type FC } from "react";
import { useCustomNavigate } from "../hooks/CustomHooks";
import FadeInPage from "../FadeInPage";
import { Tools, Trash2Fill } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { PlayerStatusEvent } from "../../api/model/player/PlayerStatusEvent";
import { PlayerRequester } from "../../api/requesters/PlayerRequester";
import { Button, Table } from "react-bootstrap";

import "./AdminComponent.css"
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { ToastAction } from "../../state/actions/ToastAction";

const AdminComponent: FC = () => {
    const navigate = useCustomNavigate();
    const { dispatch } = useMusicPlayerContext();


    const [fullPlayerStatus, setFullPlayerStatus] = useState(PlayerStatusEvent.empty())

    useEffect(() => {
        PlayerRequester.getFullPlayerStatus()
            .then(fullPlayerStatus => setFullPlayerStatus(fullPlayerStatus));
    }, [])

    return (
        <FadeInPage
            label="Administration"
            icon={<Tools />}
            onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
        >
            <Table striped bordered hover className="all-status">
                <tr>
                    <td>STATUS</td>
                    <td>{fullPlayerStatus.playerStatus}</td>
                </tr>
                <tr>
                    <td>VOLUME</td>
                    <td>{fullPlayerStatus.volume}</td>
                </tr>
                <tr>
                    <td>TRACK</td>
                    <td>{fullPlayerStatus.track ? fullPlayerStatus.track.name : "-"}</td>
                </tr>
                <tr>
                    <td>ARTIST</td>
                    <td>{fullPlayerStatus.artist ? fullPlayerStatus.artist.name : "-"}</td>
                </tr>
                <tr>
                    <td>ALBUM</td>
                    <td>{fullPlayerStatus.album ? fullPlayerStatus.album.name : "-"}</td>
                </tr>
                <tr>
                    <td>MEMORY</td>
                    <td>{`${fullPlayerStatus.memoryStatus.percentageUsedMemory} %`}</td>
                </tr>
                <tr>
                    <td>CLEAR CACHE</td>
                    <td>
                        <Button variant="danger" onClick={() => {
                            dispatch(ToastAction.clearCache())
                            PlayerRequester.clearCache().then(() => { dispatch(ToastAction.close()); });
                        }}>
                            <Trash2Fill size={30} />
                            clear local cache content
                        </Button>
                    </td>
                </tr>
            </Table>

            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
                />
            </MenuComponent>
        </FadeInPage >

    );

}

export default AdminComponent;