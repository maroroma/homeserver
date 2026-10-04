import { useEffect, useState, type FC } from "react";
import { useCustomNavigate } from "../hooks/CustomHooks";
import FadeInPage from "../FadeInPage";
import { Tools, Trash2Fill } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { PlayerRequester } from "../../api/requesters/PlayerRequester";
import { Button, Table } from "react-bootstrap";

import "./AdminComponent.css"
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { ToastAction } from "../../state/actions/ToastAction";
import { AlbumProjectRequester } from "../../api/requesters/AlbumProjectRequester";
import { ApplicationStats } from "../../api/model/administration/ApplicationStats";
import { AdministrationRequester } from "../../api/requesters/AdministrationRequester";

const AdminComponent: FC = () => {
    const navigate = useCustomNavigate();
    const { dispatch } = useMusicPlayerContext();


    const [applicationStats, setApplicationStats] = useState(ApplicationStats.empty())

    useEffect(() => {
        AdministrationRequester.getApplicationStats()
            .then(fullPlayerStatus => setApplicationStats(fullPlayerStatus));
    }, [])

    return (
        <FadeInPage
            label="Administration"
            icon={<Tools />}
            onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
        >
            <Table striped bordered hover className="all-status">
                <tr>
                    <td>NB TRACKS</td>
                    <td>{applicationStats.nbTracks}</td>
                </tr>
                <tr>
                    <td>NB ALBUMS</td>
                    <td>{applicationStats.nbAlbums}</td>
                </tr>
                <tr>
                    <td>NB ARTISTS</td>
                    <td>{applicationStats.nbArtists}</td>
                </tr>
                <tr>
                    <td>LOCAL CACHE</td>
                    <td>
                        {`${applicationStats.nbLocalCacheItems}/${applicationStats.nbMaxCacheItems}`}
                    </td>
                </tr>
                <tr>
                    <td>MEMORY</td>
                    <td>{`${applicationStats.memoryStatus.percentageUsedMemory} %`}</td>
                </tr>
            </Table>

            <div className="all-status">
                <div className="all-status">
                    <Button variant="danger" onClick={() => {
                        dispatch(ToastAction.clearCache())
                        PlayerRequester.clearCache().then(() => { dispatch(ToastAction.close()); });
                    }}>
                        <Trash2Fill size={30} />
                        Clear local track cache
                    </Button>
                </div>
                <div className="all-status">
                    <Button variant="danger" onClick={() => {
                        dispatch(ToastAction.clearProjects())
                        AlbumProjectRequester.deleteAllProject().then(() => { dispatch(ToastAction.close()); });
                    }}>
                        <Trash2Fill size={30} />
                        Clear Dead Projects
                    </Button>
                </div>
            </div>
            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
                />
            </MenuComponent>
        </FadeInPage >

    );

}

export default AdminComponent;